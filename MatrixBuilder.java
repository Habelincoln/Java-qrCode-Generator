import static java.lang.Math.*;
import java.util.*;

public class MatrixBuilder {
    static boolean[][] filled;
    static int[][] matrix;
    private static String message;
    private static int printSize;
    private static int maskType;
    final private static String[] formatInfo0 = {
        "111011111000100", // L, 0
         "111001011110011", // L, 1
         "111110110101010", // L, 2
         "111100010011101", // L, 3
         "110011000101111", // L, 4
         "110001100011000", // L, 5
         "110110001000001", // L, 6
         "110100101110110" // L, 7
    };
    final private static String[] formatInfo1 = {
         "101010000010010", // M, 0
         "101000100100101", // M, 1
         "101111001111100", // M, 2
         "101101101001011", // M, 3
         "100010111111001", // M, 4
         "100000011001110", // M, 5
         "100111110010111", // M, 6
         "100101010100000" // M, 7
     };
     final private static String[] formatInfo2 = {
         "011010101011111", // Q, 0
         "011000001101000", // Q, 1
         "011111100110001", // Q, 2
         "011101000000110", // Q, 3
         "010010010110100", // Q, 4
         "010000110000011", // Q, 5
         "010111011011010", // Q, 6
         "010101111101101" // Q, 7
     };
     final private static String[] formatInfo3 = {
         "001011010001001", // H, 0
         "001001110111110", // H, 1
         "001110011100111", // H, 2
         "001100111010000", // H, 3
         "000011101100010", // H, 4
         "000001001010101", // H, 5
         "000110100001100", // H, 6
         "000100000111011"  // H, 7
    };

    private static final Map<Integer, String> versionFormats = new HashMap<>();

    static {
        versionFormats.put(7, "000111110010010100");
        versionFormats.put(8, "001000010110111100");
        versionFormats.put(9, "001001101010011001");
        versionFormats.put(10, "001010010011010011");
        versionFormats.put(11, "001011101111110110");
        versionFormats.put(12, "001100011101100010");
        versionFormats.put(13, "001101100001000111");
        versionFormats.put(14, "001110011000001101");
        versionFormats.put(15, "001111100100101000");
        versionFormats.put(16, "010000101101111000");
        versionFormats.put(17, "010001010001011101");
        versionFormats.put(18, "010010101000010111");
        versionFormats.put(19, "010011010100110010");
        versionFormats.put(20, "010100100110100110");
        versionFormats.put(21, "010101011010000011");
        versionFormats.put(22, "010110100011001001");
        versionFormats.put(23, "010111011111101100");
        versionFormats.put(24, "011000111011000100");
        versionFormats.put(25, "011001000111100001");
        versionFormats.put(26, "011010111110101011");
        versionFormats.put(27, "011011000010001110");
        versionFormats.put(28, "011100110000011010");
        versionFormats.put(29, "011101001100111111");
        versionFormats.put(30, "011110110101110101");
        versionFormats.put(31, "011111001001010000");
        versionFormats.put(32, "100000100111010101");
        versionFormats.put(33, "100001011011110000");
        versionFormats.put(34, "100010100010111010");
        versionFormats.put(35, "100011011110011111");
        versionFormats.put(36, "100100101100001011");
        versionFormats.put(37, "100101010000101110");
        versionFormats.put(38, "100110101001100100");
        versionFormats.put(39, "100111010101000001");
        versionFormats.put(40, "101000110001101001");
    }
   
    public MatrixBuilder(String text, int versionInput, int ecLevel) {
       
        int version = versionInput;
        int size = 21 + ((version - 1) * 4);
        int lowestScore = 10000000;
        int score;
       
        message = text;
        printSize = size;
       
        for (int i = 0; i <= 7; i++){
           
        matrix = new int[size][size];
        filled = new boolean[size][size];

        addFinderPatterns();
        addSeparators();
        addTimingPatterns();
        addDarkModule(version);
        addAlignmentPatterns(version);
        addReservedAreas(version, size);
        fill(size -1, size -1, 0, true);
       
        addFormatInfo(version, size, ecLevel, i);
       
       
         score = calculatePenalty(matrix);
       
         
       
             if (score < lowestScore){
                 lowestScore = score;
                   maskType = i;
             }
       
         }
       
            matrix = new int[size][size];
            filled = new boolean[size][size];

            addFinderPatterns();
            addSeparators();
            addTimingPatterns();
            addDarkModule(version);
            addAlignmentPatterns(version);
            addReservedAreas(version, size);
            fill(size -1, size -1, 0, true);
       
            addFormatInfo(version, size, ecLevel, maskType);
           
            new Display(matrix);
           
            System.out.println("Selected Mask: " + maskType);

    }

    private static void addFinderPatterns() {
        int size = matrix.length;
        addFinderPattern(0, 0);
        addFinderPattern(0, size - 7);
        addFinderPattern(size - 7, 0);
    }

    private static void addFinderPattern(int startRow, int startCol) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == 0 || i == 6 || j == 0 || j == 6 || (i >= 2 && i <= 4 && j >= 2 && j <= 4)) {
                    matrix[startRow + i][startCol + j] = 1;
                   filled[startRow + i][startCol + j] = true;
                } else {
                    matrix[startRow + i][startCol + j] = 0;
                    filled[startRow + i][startCol + j] = true;
                }
            }
        }
    }

    private static void addSeparators() {
        int size = matrix.length;
        for (int i = 0; i < 8; i++) {
            matrix[7][i] = 0;
            filled[7][i] = true;

            matrix[i][7] = 0;
            filled[i][7] = true;

            matrix[7][size - 1 - i] = 0;
            filled[7][size - 1 - i] = true;

            matrix[i][size - 8] = 0;
            filled[i][size - 8] = true;

            matrix[size - 8][i] = 0;
            filled[size - 8][i] = true;

            matrix[size - 1 - i][7] = 0;
            filled[size - 1 - i][7] = true;
        }
    }

    private static void addTimingPatterns() {
        int size = matrix.length;
        for (int i = 8; i < size - 8; i++) {
            if (i % 2 == 0) {
            matrix[6][i] = 1;
            filled[6][i] = true;

            matrix[i][6] = 1;
            filled[i][6] = true;
            } else {
            matrix[6][i] = 0;
            filled[6][i] = true;

            matrix[i][6] = 0;
            filled[i][6] = true;
            }
        }

        // Vertical timing pattern in the 6th column between the top left and bottom left finding patterns
        for (int i = 8; i < size - 8; i++) {
            if (i % 2 == 0) {
            matrix[i][6] = 1;
            filled[i][6] = true;
            } else {
            matrix[i][6] = 0;
            filled[i][6] = true;
            }
        }

    }

    private static void addDarkModule(int version) {
        matrix[(4 * version) + 9][8] = 1;
        filled[(4 * version) + 9][8] = true;
    }

    private static void addAlignmentPatterns(int version) {
        if (version < 2) return;
        int[] alignmentPositions = getAlignmentPositions(version);
        for (int row : alignmentPositions) {
            for (int col : alignmentPositions) {
                if (!isInFinderPatternArea(row, col, matrix.length)) {
                    addAlignmentPattern(row, col);
                }
            }
        }
    }

    private static boolean isInFinderPatternArea(int row, int col, int size) {
        return (row < 7 && col < 7) || (row < 7 && col >= size - 7) || (row >= size - 7 && col < 7);
    }

    private static void addAlignmentPattern(int centerRow, int centerCol) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(i) == 2 || Math.abs(j) == 2 || (i == 0 && j == 0)) {
                    matrix[centerRow + i][centerCol + j] = 1;
                    filled[centerRow + i][centerCol + j] = true;
                } else {
                    matrix[centerRow + i][centerCol + j] = 0;
                    filled[centerRow + i][centerCol + j] = true;
                }
            }
        }
    }

    private static int[] getAlignmentPositions(int version) {
        switch (version) {
            case 2: return new int[]{6, 18};
            case 3: return new int[]{6, 22};
            case 4: return new int[]{6, 26};
            case 5: return new int[]{6, 30};
            case 6: return new int[]{6, 34};
            case 7: return new int[]{6, 22, 38};
            case 8: return new int[]{6, 24, 42};
            case 9: return new int[]{6, 26, 46};
            case 10: return new int[]{6, 28, 50};
            case 11: return new int[]{6, 30, 54};
            case 12: return new int[]{6, 32, 58};
            case 13: return new int[]{6, 34, 62};
            case 14: return new int[]{6, 26, 46, 66};
            case 15: return new int[]{6, 26, 48, 70};
            case 16: return new int[]{6, 26, 50, 74};
            case 17: return new int[]{6, 30, 54, 78};
            case 18: return new int[]{6, 30, 56, 82};
            case 19: return new int[]{6, 30, 58, 86};
            case 20: return new int[]{6, 34, 62, 90};
            case 21: return new int[]{6, 28, 50, 72, 94};
            case 22: return new int[]{6, 26, 50, 74, 98};
            case 23: return new int[]{6, 30, 54, 78, 102};
            case 24: return new int[]{6, 28, 54, 80, 106};
            case 25: return new int[]{6, 32, 58, 84, 110};
            case 26: return new int[]{6, 30, 58, 86, 114};
            case 27: return new int[]{6, 34, 62, 90, 118};
            case 28: return new int[]{6, 26, 50, 74, 98, 122};
            case 29: return new int[]{6, 30, 54, 78, 102, 126};
            case 30: return new int[]{6, 26, 52, 78, 104, 130};
            case 31: return new int[]{6, 30, 56, 82, 108, 134};
            case 32: return new int[]{6, 34, 60, 86, 112, 138};
            case 33: return new int[]{6, 30, 58, 86, 114, 142};
            case 34: return new int[]{6, 34, 62, 90, 118, 146};
            case 35: return new int[]{6, 30, 54, 78, 102, 126, 150};
            case 36: return new int[]{6, 24, 50, 76, 102, 128, 154};
            case 37: return new int[]{6, 28, 54, 80, 106, 132, 158};
            case 38: return new int[]{6, 32, 58, 84, 110, 136, 162};
            case 39: return new int[]{6, 26, 54, 82, 110, 138, 166};
            case 40: return new int[]{6, 30, 58, 86, 114, 142, 170};
            default: return new int[]{};
        }
    }

    private static void addReservedAreas(int version, int size) {

       
        for (int i = 0; i < 8; i++) {
            filled[8][matrix.length - 8 + i] = true;
        }

     
        for (int i = (4 * version) + 10; i < matrix.length; i++) {
            filled[i][8] = true;
        }

       
        for (int i = 0; i < 8; i++) {
            filled[8][i] = true;
        }

       
        for (int i = 0; i < 8; i++) {
            filled[i][8] = true;
        }

       
        filled[8][8] = true;
       
       
        if(version > 7){
            for (int i = size - 12; i <= size - 9; i++) {
                for (int j = 0; j <= 6; j++) {
                    filled[i][j] = true;
                   
                }
               
            }
           
            for (int i = 0; i <= 6; i++) {
                for (int j = size - 11; j <= size -9; j++) {
                    filled[i][j] = true;
                }
            }
        }
       
    }

    private static void fill(int startRow, int startCol, int startIndex, boolean startUp) {
        int row = startRow;
        int col = startCol;
        int index = startIndex;
        boolean up = startUp;
        // Track whether we're in fill() or fillAfterTiming() logic
        boolean isAfterTiming = false;
   
        while (true) {
            // Exit condition checks (mimicking the base case of recursion)
            if (col < 0 || index > (isAfterTiming ? message.length() - 2 : message.length())) {
                break;
            }
   
            int bit = Character.getNumericValue(message.charAt(index));
   
            if (isAfterTiming) {
                // --- fillAfterTiming() logic ---
                if (up) {
                    if (!filled[row][col]) {
                        placeBitInPos(row, col, bit);
                        filled[row][col] = true;
                    } else {
                        index--;
                    }
   
                    if (row == 0 && (col + 1) % 2 != 0) {
                        up = false;
                        col--;
                        index++;
                        continue;
                    }
   
                    if ((col + 1) % 2 == 0) {
                        col--;
                    } else {
                        row--;
                        col++;
                    }
                } else {
                    if (!filled[row][col]) {
                        placeBitInPos(row, col, bit);
                        filled[row][col] = true;
                    } else {
                        index--;
                    }
   
                    if (row == printSize - 1 && (col + 1) % 2 != 0) {
                        up = true;
                        col--;
                        index++;
                        continue;
                    }
   
                    if ((col + 1) % 2 == 0) {
                        col--;
                    } else {
                        row++;
                        col++;
                    }
                }
            } else {
                // --- fill() logic ---
                if (up) {
                    if (!filled[row][col]) {
                        placeBitInPos(row, col, bit);
                        filled[row][col] = true;
                    } else {
                        index--;
                    }
   
                    if (row == 0 && (col + 1) % 2 == 0) {
                        up = false;
                        if (col == 7) {
                            isAfterTiming = true; // Switch to fillAfterTiming mode
                            col -= 2;
                        } else {
                            col--;
                        }
                        index++;
                        continue;
                    }
   
                    if ((col + 1) % 2 != 0) {
                        col--;
                    } else {
                        row--;
                        col++;
                    }
                } else {
                    if (!filled[row][col]) {
                        placeBitInPos(row, col, bit);
                        filled[row][col] = true;
                    } else {
                        index--;
                    }
   
                    if (row == printSize - 1 && (col + 1) % 2 == 0) {
                        up = true;
                        if (col == 7) {
                            isAfterTiming = true; // Switch to fillAfterTiming mode
                            col -= 2;
                        } else {
                            col--;
                        }
                        index++;
                        continue;
                    }
   
                    if ((col + 1) % 2 != 0) {
                        col--;
                    } else {
                        row++;
                        col++;
                    }
                }
            }
   
            index++; // Move to the next bit for the next position
        }
    }

    private static void placeBitInPos(int row, int col, int bit) {
           
            switch(maskType) {

                case 0:
                if ((row + col) % 2 == 0) {
                    if (bit == 1) bit = 0;
                    else if (bit == 0) bit = 1;
                }
                matrix[row][col] = bit;
                break;

                case 1:
                if ((row) % 2 == 0) {
                    if (bit == 1) bit = 0;
                    else if (bit == 0) bit = 1;
                   
                }
                matrix[row][col] = bit;
                break;


                case 2:
                if ((col % 3 == 0)) {
                    if (bit == 1) bit = 0;
                    else if (bit == 0) bit = 1;
                }
                matrix[row][col] = bit;
                    break;

                case 3:
                if ((row + col) % 3 == 0) {
                    if (bit == 1) bit = 0;
                    else if (bit == 0) bit = 1;
                }
                matrix[row][col] = bit;
                    break;

                case 4:
                if (( floor(row / 2) + floor(col / 3) ) % 2 == 0) {
                    if (bit == 1) bit = 0;
                    else if (bit == 0) bit = 1;
                }
                matrix[row][col] = bit;
                    break;

                case 5:
                if (((row * col) % 2) + ((row * col) % 3) == 0){
                    if (bit == 1) bit = 0;
                    else if (bit == 0) bit = 1;
                }
                matrix[row][col] = bit;
                    break;

                case 6:
                if ((((row * col) % 2) + ((row * col) % 3) ) % 2 == 0) {
                    if (bit == 1) bit = 0;
                    else if (bit == 0) bit = 1;
                }
                matrix[row][col] = bit;
                    break;

                case 7:
                if (( ((row + col) % 2) + ((row * col) % 3) ) % 2 == 0) {
                    if (bit == 1) bit = 0;
                    else if (bit == 0) bit = 1;
                }
                matrix[row][col] = bit;
                    break;

                default:
                matrix[row][col] = bit;
            }
    }

    private static void addFormatInfo(int version, int size, int ecLevel, int maskType) {
       
        addMaskAndECInfo(ecLevel, maskType, size);
        addVersionInfo(version, size);

    }
   
    private static void addMaskAndECInfo(int ecLevel, int maskType, int size) {
        String formatString = "";
        if (ecLevel < 0 || ecLevel > 3) {
            System.out.println("Invalid error correction level");
            return;
        }
        if (ecLevel == 0) {
            switch (maskType) {
                case 0:
                    formatString = formatInfo0[0];
                    break;
               
                case 1:
                    formatString = formatInfo0[1];
                    break;

                case 2:
                    formatString = formatInfo0[2];
                    break;

                case 3:
                    formatString = formatInfo0[3];
                    break;

                case 4:
                    formatString = formatInfo0[4];
                    break;

                case 5:
                    formatString = formatInfo0[5];
                    break;

                case 6:
                    formatString = formatInfo0[6];
                    break;

                case 7:
                    formatString = formatInfo0[7];
                    break;

                default:
                System.out.println("invalid mask type");
                return;
            }
        }

        if (ecLevel == 1) {
            switch (maskType) {
                case 0:
                    formatString = formatInfo1[0];
                    break;
               
                case 1:
                    formatString = formatInfo1[1];
                    break;

                case 2:
                    formatString = formatInfo1[2];
                    break;

                case 3:
                    formatString = formatInfo1[3];
                    break;

                case 4:
                    formatString = formatInfo1[4];
                    break;

                case 5:
                    formatString = formatInfo1[5];
                    break;

                case 6:
                    formatString = formatInfo1[6];
                    break;

                case 7:
                    formatString = formatInfo1[7];
                    break;

                default:
                return;
            }
        }

        if (ecLevel == 2) {
            switch (maskType) {
                case 0:
                    formatString = formatInfo2[0];
                    break;
               
                case 1:
                    formatString = formatInfo2[1];
                    break;

                case 2:
                    formatString = formatInfo2[2];
                    break;

                case 3:
                    formatString = formatInfo2[3];
                    break;

                case 4:
                    formatString = formatInfo2[4];
                    break;

                case 5:
                    formatString = formatInfo2[5];
                    break;

                case 6:
                    formatString = formatInfo2[6];
                    break;

                case 7:
                    formatString = formatInfo2[7];
                    break;

                default:
                System.out.println("invalid mask type");
                return;
            }
        }

        if (ecLevel == 3) {
            switch (maskType) {
                case 0:
                    formatString = formatInfo3[0];
                    
                    break;
               
                case 1:
                    formatString = formatInfo3[1];
                    
                    break;

                case 2:
                    formatString = formatInfo3[2];
                    
                    break;

                case 3:
                    formatString = formatInfo3[3];
                    
                    break;

                case 4:
                    formatString = formatInfo3[4];
                    
                    break;

                case 5:
                    formatString = formatInfo3[5];
                    
                    break;

                case 6:
                    formatString = formatInfo3[6];
                    
                    break;

                case 7:
                    formatString = formatInfo3[7];
                    
                    break;

                default:
                System.out.println("invalid mask type");
                return;
            }
        }

        if (formatString.length() != 15) {
            throw new IllegalArgumentException("Format string must be 15 bits long.");
        }
   
        matrix[8][0] = Character.getNumericValue(formatString.charAt(0));
        matrix[8][1] = Character.getNumericValue(formatString.charAt(1));
        matrix[8][2] = Character.getNumericValue(formatString.charAt(2));
        matrix[8][3] = Character.getNumericValue(formatString.charAt(3));
        matrix[8][4] = Character.getNumericValue(formatString.charAt(4));
        matrix[8][5] = Character.getNumericValue(formatString.charAt(5));
        matrix[8][7] = Character.getNumericValue(formatString.charAt(6));
        matrix[8][8] = Character.getNumericValue(formatString.charAt(7));
        matrix[7][8] = Character.getNumericValue(formatString.charAt(8));
        matrix[5][8] = Character.getNumericValue(formatString.charAt(9));
        matrix[4][8] = Character.getNumericValue(formatString.charAt(10));
        matrix[3][8] = Character.getNumericValue(formatString.charAt(11));
        matrix[2][8] = Character.getNumericValue(formatString.charAt(12));
        matrix[1][8] = Character.getNumericValue(formatString.charAt(13));
        matrix[0][8] = Character.getNumericValue(formatString.charAt(14));


        matrix[size-1][8] = Character.getNumericValue(formatString.charAt(0));
        matrix[size-2][8] = Character.getNumericValue(formatString.charAt(1));
        matrix[size-3][8] = Character.getNumericValue(formatString.charAt(2));
        matrix[size-4][8] = Character.getNumericValue(formatString.charAt(3));
        matrix[size-5][8] = Character.getNumericValue(formatString.charAt(4));
        matrix[size-6][8] = Character.getNumericValue(formatString.charAt(5));
        matrix[size-7][8] = Character.getNumericValue(formatString.charAt(6));

        matrix[8][size - 8] = Character.getNumericValue(formatString.charAt(7));
        matrix[8][size - 7] = Character.getNumericValue(formatString.charAt(8));
        matrix[8][size - 6] = Character.getNumericValue(formatString.charAt(9));
        matrix[8][size - 5] = Character.getNumericValue(formatString.charAt(10));
        matrix[8][size - 4] = Character.getNumericValue(formatString.charAt(11));
        matrix[8][size - 3] = Character.getNumericValue(formatString.charAt(12));
        matrix[8][size - 2] = Character.getNumericValue(formatString.charAt(13));
        matrix[8][size - 1] = Character.getNumericValue(formatString.charAt(14));
    }
    private static void addVersionInfo(int version, int size) {
        if (version < 7) return;

        String vfString = new StringBuilder(versionFormats.get(version)).reverse().toString();

        matrix[0][size-11] = Character.getNumericValue(vfString.charAt(0));
        matrix[0][size-10] = Character.getNumericValue(vfString.charAt(1));
        matrix[0][size-9] = Character.getNumericValue(vfString.charAt(2));
        matrix[1][size-11] = Character.getNumericValue(vfString.charAt(3));
        matrix[1][size-10] = Character.getNumericValue(vfString.charAt(4));
        matrix[1][size-9] = Character.getNumericValue(vfString.charAt(5));
        matrix[2][size-11] = Character.getNumericValue(vfString.charAt(6));
        matrix[2][size-10] = Character.getNumericValue(vfString.charAt(7));
        matrix[2][size-9] = Character.getNumericValue(vfString.charAt(8));
        matrix[3][size-11] = Character.getNumericValue(vfString.charAt(9));
        matrix[3][size-10] = Character.getNumericValue(vfString.charAt(10));
        matrix[3][size-9] = Character.getNumericValue(vfString.charAt(11));
        matrix[4][size-11] = Character.getNumericValue(vfString.charAt(12));
        matrix[4][size-10] = Character.getNumericValue(vfString.charAt(13));
        matrix[4][size-9] = Character.getNumericValue(vfString.charAt(14));
        matrix[5][size-11] = Character.getNumericValue(vfString.charAt(15));
        matrix[5][size-10] = Character.getNumericValue(vfString.charAt(16));
        matrix[5][size-9] = Character.getNumericValue(vfString.charAt(17));

        matrix[size-11][0] = Character.getNumericValue(vfString.charAt(0));
        matrix[size-10][0] = Character.getNumericValue(vfString.charAt(1));
        matrix[size-9][0] = Character.getNumericValue(vfString.charAt(2));
        matrix[size-11][1] = Character.getNumericValue(vfString.charAt(3));
        matrix[size-10][1] = Character.getNumericValue(vfString.charAt(4));
        matrix[size-9][1] = Character.getNumericValue(vfString.charAt(5));
        matrix[size-11][2] = Character.getNumericValue(vfString.charAt(6));
        matrix[size-10][2] = Character.getNumericValue(vfString.charAt(7));
        matrix[size-9][2] = Character.getNumericValue(vfString.charAt(8));
        matrix[size-11][3] = Character.getNumericValue(vfString.charAt(9));
        matrix[size-10][3] = Character.getNumericValue(vfString.charAt(10));
        matrix[size-9][3] = Character.getNumericValue(vfString.charAt(11));
        matrix[size-11][4] = Character.getNumericValue(vfString.charAt(12));
        matrix[size-10][4] = Character.getNumericValue(vfString.charAt(13));
        matrix[size-9][4] = Character.getNumericValue(vfString.charAt(14));
        matrix[size-11][5] = Character.getNumericValue(vfString.charAt(15));
        matrix[size-10][5] = Character.getNumericValue(vfString.charAt(16));
        matrix[size-9][5] = Character.getNumericValue(vfString.charAt(17));

    }
   
    private static int evaluateCondition1(int[][] matrix) {
        int penalty = 0;

        // Check rows
        for (int[] row : matrix) {
            penalty += evaluateLine(row);
        }

        // Check columns
        for (int col = 0; col < matrix[0].length; col++) {
            int[] column = new int[matrix.length];
            for (int row = 0; row < matrix.length; row++) {
                column[row] = matrix[row][col];
            }
            penalty += evaluateLine(column);
        }

        return penalty;
    }

    private static int evaluateLine(int[] line) {
        int penalty = 0;
        int count = 1;
        int current = line[0];

        for (int i = 1; i < line.length; i++) {
            if (line[i] == current) {
                count++;
                if (count == 5) {
                    penalty += 3;
                } else if (count > 5) {
                    penalty += 1;
                }
            } else {
                current = line[i];
                count = 1;
            }
        }

        return penalty;
    }
   
    private static int evaluateCondition2(int[][] matrix) {
        int penalty = 0;

        // Iterate through the matrix to find 2x2 blocks
        for (int row = 0; row < matrix.length - 1; row++) {
            for (int col = 0; col < matrix[0].length - 1; col++) {
                // Check if the current 2x2 block has the same color
                if (matrix[row][col] == matrix[row][col + 1] &&
                    matrix[row][col] == matrix[row + 1][col] &&
                    matrix[row][col] == matrix[row + 1][col + 1]) {
                    penalty += 3; // Add penalty for each 2x2 block
                }
            }
        }

        return penalty;
    }

    private static int evaluateCondition3(int[][] matrix) {
        int penalty = 0;

        // Define the two patterns to look for
        int[] pattern1 = {1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0};
        int[] pattern2 = {0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1};

        // Check rows for the patterns
        for (int[] row : matrix) {
            penalty += countPatternInLine(row, pattern1);
            penalty += countPatternInLine(row, pattern2);
        }

        // Check columns for the patterns
        for (int col = 0; col < matrix[0].length; col++) {
            int[] column = new int[matrix.length];
            for (int row = 0; row < matrix.length; row++) {
                column[row] = matrix[row][col];
            }
            penalty += countPatternInLine(column, pattern1);
            penalty += countPatternInLine(column, pattern2);
        }

        return penalty;
    }

    private static int countPatternInLine(int[] line, int[] pattern) {
        int penalty = 0;
        int patternLength = pattern.length;

        // Slide the pattern over the line
        for (int i = 0; i <= line.length - patternLength; i++) {
            boolean match = true;
            for (int j = 0; j < patternLength; j++) {
                if (line[i + j] != pattern[j]) {
                    match = false;
                    break;
                }
            }
            if (match) {
                penalty += 40; // Add penalty for each match
            }
        }

        return penalty;
    }
   
    private static int evaluateCondition4(int[][] matrix) {
        int totalModules = matrix.length * matrix[0].length;
        int darkModules = 0;

        // Count the number of dark modules (1 represents dark)
        for (int[] row : matrix) {
            for (int module : row) {
                if (module == 1) {
                    darkModules++;
                }
            }
        }

        // Calculate the percentage of dark modules
        double darkPercent = ((double) darkModules / totalModules) * 100;

        // Find the previous and next multiples of 5
        int prevMultiple = (int) (Math.floor(darkPercent / 5) * 5);
        int nextMultiple = prevMultiple + 5;

        // Calculate the absolute differences from 50
        int diffPrev = Math.abs(prevMultiple - 50);
        int diffNext = Math.abs(nextMultiple - 50);

        // Divide by 5 and take the smaller result
        int penaltyPrev = diffPrev / 5;
        int penaltyNext = diffNext / 5;
        int penalty = Math.min(penaltyPrev, penaltyNext) * 10;

        return penalty;
    }
   
    private static int calculatePenalty(int[][] maskedMatrix){
        return (evaluateCondition1(maskedMatrix) + evaluateCondition2(maskedMatrix) + evaluateCondition3(maskedMatrix) + evaluateCondition4(maskedMatrix));
    }
       
           
    }