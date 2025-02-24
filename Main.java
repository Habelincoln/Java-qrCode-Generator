import java.util.*;

@SuppressWarnings("unused")
public class Main {
    
    public class MyClass {
    // Public static variable
    public static String myNumber;
    
    public MyClass(String num) {
        myNumber = num;
    }
}

    static boolean isOneGroup = false; // for turning data into array
    public static int byteCapacity;
    public static int totalDataCodewords;
    public static int ecCodewordsPerBlock;
    public static int numBlocksGroup1;
    public static int dataCodewordsGroup1;
    public static int numBlocksGroup2;
    public static int dataCodewordsGroup2;
    public static int version = 0;
    public static int ecLevel = 3;
    public static int codewordsPerBlockGroup1;
    public static int codewordsPerBlockGroup2;
    public static Scanner userInput = new Scanner(System.in);
    private static final int[][] BYTE_CAPACITIES = {
        {17, 14, 11, 7}, {32, 26, 20, 14}, {53, 42, 32, 24}, {78, 62, 46, 34}, {106, 84, 60, 44},
        {134, 106, 74, 58}, {154, 122, 86, 64}, {192, 152, 108, 84}, {230, 180, 130, 98}, {271, 213, 151, 119},
        {321, 251, 177, 137}, {367, 287, 203, 155}, {425, 331, 241, 177}, {458, 362, 258, 194}, {520, 412, 292, 220},
        {586, 450, 322, 250}, {644, 504, 364, 280}, {718, 560, 394, 310}, {792, 624, 442, 338}, {858, 666, 482, 382},
        {929, 711, 509, 403}, {1003, 779, 565, 439}, {1091, 857, 611, 461}, {1171, 911, 661, 511}, {1273, 997, 715, 535},
        {1367, 1059, 751, 593}, {1465, 1125, 805, 625}, {1528, 1190, 868, 658}, {1628, 1264, 908, 698}, {1732, 1370, 982, 742},
        {1840, 1452, 1030, 790}, {1952, 1538, 1112, 842}, {2068, 1628, 1168, 898}, {2188, 1722, 1228, 958}, {2303, 1809, 1283, 983},
        {2431, 1911, 1351, 1051}, {2563, 1989, 1423, 1093}, {2699, 2099, 1499, 1139}, {2809, 2213, 1579, 1219}, {2953, 2331, 1663, 1273}
    };

    private static final int[][] ERROR_CORRECTION_TABLE = {
    {19, 7, 1, 19, 0, 0}, {16, 10, 1, 16, 0, 0}, {13, 13, 1, 13, 0, 0}, {9, 17, 1, 9, 0, 0}, {34, 10, 1, 34, 0, 0}, {28, 16, 1, 28, 0, 0}, {22, 22, 1, 22, 0, 0}, {16, 28, 1, 16, 0, 0}, {55, 15, 1, 55, 0, 0}, {44, 26, 1, 44, 0, 0}, {34, 18, 2, 17, 0, 0}, {26, 22, 2, 13, 0, 0}, {80, 20, 1, 80, 0, 0}, {64, 18, 2, 32, 0, 0}, {48, 26, 2, 24, 0, 0}, {36, 16, 4, 9, 0, 0}, {108, 26, 1, 108, 0, 0}, {86, 24, 2, 43, 0, 0}, {62, 18, 2, 15, 2, 16}, {46, 22, 2, 11, 2, 12}, {136, 18, 2, 68, 0, 0}, {108, 16, 4, 27, 0, 0}, {76, 24, 4, 19, 0, 0}, {60, 28, 4, 15, 0, 0}, {156, 20, 2, 78, 0, 0}, {124, 18, 4, 31, 0, 0}, {88, 18, 2, 14, 4, 15}, {66, 26, 4, 13, 1, 14}, {194, 24, 2, 97, 0, 0}, {154, 22, 2, 38, 2, 39}, {110, 22, 4, 18, 2, 19}, {86, 26, 4, 14, 2, 15}, {232, 30, 2, 116, 0, 0}, {182, 22, 3, 36, 2, 37}, {132, 20, 4, 16, 4, 17}, {100, 24, 4, 12, 4, 13}, {274, 18, 2, 68, 2, 69}, {216, 26, 4, 43, 1, 44}, {154, 24, 6, 19, 2, 20}, {122, 28, 6, 15, 2, 16}, {324, 20, 4, 81, 0, 0}, {254, 18, 1, 50, 4, 51}, {180, 24, 4, 22, 4, 23}, {140, 22, 3, 12, 8, 13}, {370, 24, 2, 92, 2, 93}, {290, 22, 6, 36, 2, 37}, {206, 20, 4, 20, 6, 21}, {158, 26, 7, 14, 4, 15}, {428, 26, 4, 107, 0, 0}, {334, 22, 8, 37, 1, 38}, {244, 24, 8, 20, 4, 21}, {180, 22, 12, 11, 4, 12}, {461, 30, 3, 115, 1, 116}, {365, 24, 4, 40, 5, 41}, {261, 20, 11, 16, 5, 17}, {197, 24, 11, 12, 5, 13}, {523, 22, 5, 87, 1, 88}, {415, 24, 5, 41, 5, 42}, {295, 30, 5, 24, 7, 25}, {223, 24, 11, 12, 7, 13}, {589, 24, 5, 98, 1, 99}, {453, 28, 7, 45, 3, 46}, {325, 24, 15, 19, 2, 20}, {253, 30, 3, 15, 13, 16},{647, 28, 1, 107, 5, 108}, {507, 28, 10, 46, 1, 47}, {367, 28, 1, 22, 15, 23}, {283, 28, 2, 14, 17, 15}, {721, 30, 5, 120, 1, 121}, {563, 26, 9, 43, 4, 44}, {397, 28, 17, 22, 1, 23}, {313, 28, 2, 14, 19, 15}, {795, 28, 3, 113, 4, 114}, {627, 26, 3, 44, 11, 45}, {445, 26, 17, 21, 4, 22}, {341, 26, 9, 13, 16, 14}, {861, 28, 3, 107, 5, 108}, {669, 26, 3, 41, 13, 42}, {485, 30, 15, 24, 5, 25}, {385, 28, 15, 15, 10, 16}, {932, 28, 4, 116, 4, 117}, {714, 26, 17, 42, 0, 0}, {512, 28, 17, 22, 6, 23}, {406, 30, 19, 16, 6, 17}, {1006, 28, 2, 111, 7, 112}, {782, 28, 17, 46, 0, 0}, {568, 30, 7, 24, 16, 25}, {442, 24, 34, 13, 0, 0}, {1094, 30, 4, 121, 5, 122}, {860, 28, 4, 47, 14, 48}, {614, 30, 11, 24, 14, 25}, {464, 30, 16, 15, 14, 16}, {1174, 30, 6, 117, 4, 118}, {914, 28, 6, 45, 14, 46}, {664, 30, 11, 24, 16, 25}, {514, 30, 30, 16, 2, 17}, {1276, 26, 8, 106, 4, 107}, {1000, 28, 8, 47, 13, 48}, {718, 30, 7, 24, 22, 25}, {538, 30, 22, 15, 13, 16}, {1370, 28, 10, 114, 2, 115}, {1062, 28, 19, 46, 4, 47}, {754, 28, 28, 22, 6, 23}, {596, 30, 33, 16, 4, 17}, {1468, 30, 8, 122, 4, 123}, {1128, 28, 22, 45, 3, 46}, {808, 30, 8, 23, 26, 24}, {628, 30, 12, 15, 28, 16}, {1531, 30, 3, 117, 10, 118}, {1193, 28, 3, 45, 23, 46}, {871, 30, 4, 24, 31, 25}, {661, 30, 11, 15, 31, 16}, {1631, 30, 7, 115, 7, 116}, {1267, 28, 21, 45, 7, 46}, {911, 30, 1, 23, 37, 24}, {701, 30, 19, 15, 26, 16}, {1735, 30, 5, 115, 10, 116}, {1373, 28, 19, 47, 10, 48}, {985, 30, 15, 24, 25, 25}, {745, 30, 23, 15, 25, 16}, {1843, 30, 13, 115, 3, 116}, {1455, 28, 2, 46, 29, 47}, {1033, 30, 42, 24, 1, 25}, {793, 30, 23, 15, 28, 16}, {1955, 30, 17, 115, 0, 0}, 
    {1541, 28, 10, 46, 23, 47}, {1115, 30, 10, 24, 35, 25}, {845, 30, 19, 15, 35, 16},{2071, 30, 17, 115, 1, 116}, {1631, 28, 14, 46, 24, 47}, {1171, 30, 29, 24, 19, 25}, {901, 30, 11, 15, 46, 16}, {2191, 30, 13, 115, 6, 116}, {1725, 28, 14, 46, 28, 47}, {1231, 30, 44, 24, 7, 25}, {961, 30, 59, 16, 1, 17}, {2306, 30, 12, 121, 7, 122}, {1812, 28, 12, 47, 26, 48}, {1286, 30, 39, 24, 14, 25}, {986, 30, 22, 15, 41, 16}, {2434, 30, 6, 121, 14, 122}, {1914, 28, 6, 47, 34, 48}, {1354, 30, 46, 24, 10, 25}, {1054, 30, 2, 15, 64, 16}, {2566, 30, 17, 122, 4, 123}, {1992, 28, 29, 46, 14, 47}, {1426, 30, 49, 24, 10, 25}, {1096, 30, 24, 15, 46, 16}, {2702, 30, 4, 122, 18, 123}, {2102, 28, 13, 46, 32, 47}, {1502, 30, 48, 24, 14, 25}, {1142, 30, 42, 15, 32, 16}, {2812, 30, 20, 117, 4, 118}, {2216, 28, 40, 47, 7, 48}, {1582, 30, 43, 24, 22, 25}, {1222, 30, 10, 15, 67, 16}, {2956, 30, 19, 118, 6, 119}, {2334, 28, 18, 47, 31, 48}, {1666, 30, 34, 24, 34, 25}, {1276, 30, 20, 15, 61, 16}
};

    public static void main(String[] args) {
        
        Scanner userInput = new Scanner(System.in);
        
        // System.out.print("Please input text:");
        // String input = userInput.nextLine(); // Input string
        String input = "hello";
        
        if (input.length() > 151){
            System.out.println("Text must be under 152 characters.");
            userInput.close();
            return;
        }

        setVersion(input);
        if (version != -1) {
            int index = (version - 1) * 4 + ecLevel;
            
            byteCapacity = BYTE_CAPACITIES[index][ecLevel];
            totalDataCodewords = ERROR_CORRECTION_TABLE[index][0];
            ecCodewordsPerBlock = ERROR_CORRECTION_TABLE[index][1];
            numBlocksGroup1 = ERROR_CORRECTION_TABLE[index][2];
            dataCodewordsGroup1 = ERROR_CORRECTION_TABLE[index][3];
            numBlocksGroup2 = ERROR_CORRECTION_TABLE[index][4];
            dataCodewordsGroup2 = ERROR_CORRECTION_TABLE[index][5];
            codewordsPerBlockGroup1 = ERROR_CORRECTION_TABLE[index][3];
            codewordsPerBlockGroup2 = ERROR_CORRECTION_TABLE[index][5];
            
            System.out.println("Version: " + version);
            System.out.println("Byte Capacity: " + byteCapacity);
            System.out.println("Total bits: " + totalDataCodewords * 8);
        } else {
            System.out.println("too long");
        }
        
        String encodedData = encodeToBinary(input);
        System.out.println("Encoded Data: " + encodedData);
        

        // Step 2: Generate error correction codewords

        //first calc whether one or two groups

        for (int i = 0; i < ERROR_CORRECTION_TABLE.length; i++) {
            if (ERROR_CORRECTION_TABLE[i][0] == totalDataCodewords) {
                isOneGroup = ERROR_CORRECTION_TABLE[i][2] == 0;
                break;
            }
        }

        //then make the array
        int[][][] data = convertBinaryToGroupsAndBlocks(encodedData); // Convert the binary string to an integer array
        int[] errorCorrection = new int[1];

        System.out.println("data as array: ");
        
        for (int[][] x : data) {
            for (int[] y : x) {
                for (int z : y) {
                    System.out.print(z);
                }
                System.out.println();
            }
        }

        String combinedBinaryData = combineDataAndErrorCorrection(encodedData, errorCorrection);
    //    System.out.println("Combined Data with Error Correction: ");
    //    System.out.println(combinedBinaryData);
        MyClass.myNumber = combinedBinaryData;
        
        System.out.println();
        userInput.close();
        
    }

    public static void setVersion(String input) {
        int length = input.getBytes().length; // Byte Mode length
        for (int i = 0; i < BYTE_CAPACITIES.length; i++) {
            if (length <= BYTE_CAPACITIES[i][ecLevel]) {
                version = i + 1;
                break;
            }
        }
        if (version == 0) {
            version = -1;
        }
        
    }
    
    public static void placeBit(int[][] QRCode, int y, int x, int value) {
    String text = MyClass.myNumber;

    // Get the bit value from the text string
    int bitValue = Character.getNumericValue(text.charAt(value));
    
    // Flip the bit if (x + y) is odd
    if (((x) % 2 != 0)) {
        bitValue = 1 - bitValue; // Flip 0 to 1, or 1 to 0
    }

    // Place the bit in the QRCode array
    QRCode[x][y] = bitValue;
}
    
    public static String encodeToBinary(String input) {
        StringBuilder result = new StringBuilder();

        //add mode indicator always
        result.append("0100");
        int bitLength = 0;
        if (version >=1 && version <= 9) {
        bitLength = 8;
        } else if (version >=10 && version <= 40) {
            bitLength = 16;
        }
        result.append(toBinary(input.length(), bitLength));
        
        for (char c : input.toCharArray()) {
            result.append(toBinary(c, bitLength));
        }

        //Step 4: Add Terminator Bits
        while ((totalDataCodewords * 8) - result.length() > 0) {
            if ((totalDataCodewords * 8) - result.length() >= 4) {
                System.out.println("Adding 4 term bits");
                result.append("0000");
                break;
            } else {
                System.out.println("adding 1 term bit");
                result.append('0');
            }
            
        }
        
        int terminatorBits = Math.min(4, (totalDataCodewords * 8) - result.length()); 
        for (int i = 0; i < terminatorBits; i++) {
            result.append('0');
        }

        // Step 5: Pad to full byte (8-bit boundary)
        while (result.length() % 8 != 0) {
            result.append('0');
        }

        // Step 6: Add Padding Bytes (0xEC and 0x11 alternately)
        String paddingByte1 = "11101100"; // 0xEC
        String paddingByte2 = "00010001"; // 0x11
        int currentBitLength = result.length();

        while (currentBitLength < (totalDataCodewords * 8)) {
            System.out.println("added padding byte 1");
            result.append(paddingByte1);
            currentBitLength += 8;
            if (currentBitLength < (totalDataCodewords * 8)) {
                System.out.println("added padding byte 2");
                result.append(paddingByte2);
                currentBitLength += 8;
            }
        }

        return result.toString();
    }

    // Converts a binary string into an integer array.
    public static int[][][] convertBinaryToGroupsAndBlocks(String binaryData) {
        
        int[][][] data;

        int dataCodewords = dataCodewordsGroup1 + dataCodewordsGroup2;

        int totalCodewords = dataCodewords + ecCodewordsPerBlock;

        if (isOneGroup) {

        data = new int[0][numBlocksGroup1][totalCodewords];

        } else {

        data = new int[1][numBlocksGroup1 + numBlocksGroup2][totalCodewords];

        }

        int group1Index = 0;
        int group1block1Index = 0;
        int group1block2Index = 0;
        int group2Index = 0;
        int group2block1Index = 0;
        int group2block2Index = 0;

        for (int i = 0; i < binaryData.length(); i += 8) {
            int codeword = Integer.parseInt(binaryData.substring(i, Math.min(i + 8, binaryData.length())));

            while (group1Index <= numBlocksGroup1) {
                while (group1block1Index <= codewordsPerBlockGroup1) {
                data[0][group1Index][group1block1Index] = codeword;
                group1block1Index++;
                }
                group1Index++;
                while (group1block2Index <= codewordsPerBlockGroup1) {
                    data[0][group1Index][group1block2Index] = codeword;
                    group1block2Index++;
                }
                group1Index++; 
            }
            if (numBlocksGroup2 != 0) {
                while (group2Index <= numBlocksGroup2) {
                    while (group2block1Index <= codewordsPerBlockGroup2) {
                        data[1][group2Index][group2block1Index] = codeword;
                        group2block1Index++;
                    }
                    group2Index++;
                    while (group2block2Index <= codewordsPerBlockGroup2) {
                        data[1][group2Index][group2block2Index] = codeword;
                        group2block2Index++;
                    }
                    group2Index++;
                }
                
            
            }








        }

        return data;
    }

     //Converts an integer to a binary string with a fixed number of bits.

    private static String toBinary(int value, int bitLength) {
        String binaryString = Integer.toBinaryString(value);
        while (binaryString.length() < bitLength) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

     //Combines the original data and the error correction codewords into a final binary string.

    public static String combineDataAndErrorCorrection(String encodedData, int[] errorCorrection) {
        // Convert error correction integers to binary strings
        StringBuilder combinedData = new StringBuilder(encodedData);
        
        for (int ec : errorCorrection) {
            combinedData.append(toBinary(ec, 8)); // Convert each error correction word to 8-bit binary
        }

        combinedData.append("0000000");

        return combinedData.toString();
    }
}

class ReedSolomon {

    private static final int GF256_PRIMITIVE_POLY = 0x11D; // Irreducible polynomial x^8 + x^4 + x^3 + x + 1
    private static final int FIELD_SIZE = 256;  // GF(256) field

    private static final int[] EXP_TABLE = new int[FIELD_SIZE * 2];
    private static final int[] LOG_TABLE = new int[FIELD_SIZE];

    static {
        // Initialize the GF(256) field multiplication and division tables
        int x = 1;
        for (int i = 0; i < FIELD_SIZE; i++) {
            EXP_TABLE[i] = x;
            LOG_TABLE[x] = i;
            x <<= 1;
            if (x >= FIELD_SIZE) x ^= GF256_PRIMITIVE_POLY;
        }
        for (int i = FIELD_SIZE; i < FIELD_SIZE * 2; i++) {
            EXP_TABLE[i] = EXP_TABLE[i - FIELD_SIZE];
        }
    }

    // Addition in GF(256) (XOR)
    public static int gfAdd(int a, int b) {
        return a ^ b;
    }

    // Multiplication in GF(256)
    public static int gfMultiply(int a, int b) {
        if (a == 0 || b == 0) return 0;
        return EXP_TABLE[(LOG_TABLE[a] + LOG_TABLE[b]) % (FIELD_SIZE - 1)];
    }

    // Find the inverse of a number in GF(256)
    public static int gfInverse(int a) {
        if (a == 0) throw new ArithmeticException("Cannot invert zero.");
        return EXP_TABLE[FIELD_SIZE - 1 - LOG_TABLE[a]];
    }

    // Generate the Reed-Solomon codewords (error correction codewords)
    public static int[] reedSolomonEncode(int[] data, int errorCorrectionLength) {
        int[] generator = generateGeneratorPolynomial(errorCorrectionLength);
        int[] encoded = Arrays.copyOf(data, data.length + errorCorrectionLength);

        for (int i = 0; i < data.length; i++) {
            int coef = encoded[i];
            if (coef == 0) continue;

            for (int j = 0; j < generator.length; j++) {
                encoded[i + j] = gfAdd(encoded[i + j], gfMultiply(coef, generator[j]));
            }
        }

        return Arrays.copyOfRange(encoded, data.length, encoded.length);
    }

    // Generate the generator polynomial for the given error correction length
    private static int[] generateGeneratorPolynomial(int errorCorrectionLength) {
        int[] generator = {1};

        for (int i = 0; i < errorCorrectionLength; i++) {
            int[] temp = {1, gfMultiply(EXP_TABLE[i], 1)};
            generator = multiplyPolynomials(generator, temp);
        }

        return generator;
    }

    // Multiply two polynomials in GF(256)
    private static int[] multiplyPolynomials(int[] poly1, int[] poly2) {
        int[] result = new int[poly1.length + poly2.length - 1];

        for (int i = 0; i < poly1.length; i++) {
            for (int j = 0; j < poly2.length; j++) {
                result[i + j] = gfAdd(result[i + j], gfMultiply(poly1[i], poly2[j]));
            }
        }

        return result;
    }
}
// class QRFrame extends JFrame {
//     public QRFrame(int[][] matrix) {
//         setTitle("QR Code");
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setSize(600, 600);
//         setFocusable(true);
//         requestFocusInWindow();

//         QRPanel panel = new QRPanel(matrix);
//         panel.setBackground(Color.WHITE);
//         add(panel);

//         setVisible(true);
//     }
// }

// class QRPanel extends JPanel {
//     private int[][] matrix;

//     public QRPanel(int[][] matrix) {
//         this.matrix = matrix;
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         drawQRCode(g);
//     }

//     private void drawQRCode(Graphics g) {
//         int rows = matrix.length;
//         int cols = matrix[0].length;
//         int cellSize = Math.min(getWidth() / cols, getHeight() / rows);

//         for (int i = 0; i < rows; i++) {
//             for (int j = 0; j < cols; j++) {
//                 if (matrix[i][j] == 1) {
//                     g.setColor(Color.BLACK);
//                 } else {
//                     g.setColor(Color.WHITE);
//                 }
//                 g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
//             }
//         }
//     }
// }