import java.util.*;
import javax.swing.*;
import java.awt.*;
@SuppressWarnings("unused")
public class Main {
    
    public class MyClass {
    // Public static variable
    public static String myNumber;
    
    public MyClass(String num) {
        myNumber = num;
    }
}
    
    private static final int ERROR_CORRECTION_LENGTH = 15; // Error correction codewords for level L (15 codewords)
    public static int byteCapacity; 
    public static int totalDataCodewords;
    public static int ecCodewordsPerBlock;
    public static int numBlocksGroup1;
    public static int dataCodewordsGroup1;
    public static int numBlocksGroup2;
    public static int dataCodewordsGroup2;
    public static int version = 0;
    public static int ecLevel = 3;
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
        {19, 7, 1, 19, 0, 0}, {34, 10, 1, 34, 0, 0}, {55, 15, 1, 55, 0, 0}, {80, 20, 1, 80, 0, 0},
        {108, 26, 1, 108, 0, 0}, {136, 18, 2, 68, 0, 0}, {156, 20, 2, 78, 0, 0}, {194, 24, 2, 97, 0, 0},
        {232, 30, 2, 116, 0, 0}, {274, 18, 2, 68, 2, 69}
    };

    public static void main(String[] args) {
        
        Scanner userInput = new Scanner(System.in);
        
        System.out.print("Please input text:");
        String input = userInput.nextLine(); // Input string
        
        if (input.length() > 151){
            System.out.println("Text must be under 152 characters.");
            userInput.close();
            return;
        }

        // Step 1: Set the version based on the input length
        setVersion(input);
        if (version != -1) {
            int byteCapacity = BYTE_CAPACITIES[version - 1][ecLevel];
            int totalDataCodewords = ERROR_CORRECTION_TABLE[version - 1][0];
            int ecCodewordsPerBlock = ERROR_CORRECTION_TABLE[version - 1][1];
            int numBlocksGroup1 = ERROR_CORRECTION_TABLE[version - 1][2];
            int dataCodewordsGroup1 = ERROR_CORRECTION_TABLE[version - 1][3];
            int numBlocksGroup2 = ERROR_CORRECTION_TABLE[version - 1][4];
            int dataCodewordsGroup2 = ERROR_CORRECTION_TABLE[version - 1][5];
            
            System.out.println("Version: " + version);
            System.out.println("Byte Capacity: " + byteCapacity);
            System.out.println("Total bits: " + totalDataCodewords * 8);
        } else {
            System.out.println("too long");
        }
        
        String encodedData = encodeToBinary(input);
        System.out.println("Encoded Data: "+encodedData);
        //System.out.println(formatBinaryWithSpaces(encodedData));

        // Step 2: Generate error correction codewords
        int[] data = convertBinaryToIntArray(encodedData); // Convert the binary string to an integer array
        int[] errorCorrection = ReedSolomon.reedSolomonEncode(data, ERROR_CORRECTION_LENGTH);

        // Step 3: Combine the data and error correction codewords
        String combinedBinaryData = combineDataAndErrorCorrection(encodedData, errorCorrection);
       // System.out.println("\nCombined Data with Error Correction: ");
       //System.out.println(combinedBinaryData);
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

        // Step 4: Add Terminator Bits
        while ((totalDataCodewords * 8) - result.length() > 0) {
            if ((totalDataCodewords * 8) - result.length() >= 4) {
                result.append("0000");
                break;
            } else {
                result.append('0');
            }
            
        }
        // int terminatorBits = Math.min(4, (totalDataCodewords * 8) - result.length()); 
        // for (int i = 0; i < terminatorBits; i++) {
        //     result.append('0');
        // }

        // Step 5: Pad to full byte (8-bit boundary)
        while (result.length() % 8 != 0) {
            result.append('0');
        }
        
        result.append("00000000");

        // Step 6: Add Padding Bytes (0xEC and 0x11 alternately)
        String paddingByte1 = "11101100"; // 0xEC
        String paddingByte2 = "00010001"; // 0x11
        int currentBitLength = result.length();

        while (currentBitLength < (totalDataCodewords * 8)) {
            result.append(paddingByte1);
            currentBitLength += 8;
            if (currentBitLength < (totalDataCodewords * 8)) {
                result.append(paddingByte2);
                currentBitLength += 8;
            }
        }

        return result.toString();
    }

     // Converts a binary string into an integer array.

    public static int[] convertBinaryToIntArray(String binaryData) {
        int length = binaryData.length() / 8;
        int[] intArray = new int[length];

        for (int i = 0; i < length; i++) {
            intArray[i] = Integer.parseInt(binaryData.substring(i * 8, (i + 1) * 8), 2);
        }

        return intArray;
    }

     //Converts an integer to a binary string with a fixed number of bits.

    private static String toBinary(int value, int bitLength) {
        String binaryString = Integer.toBinaryString(value);
        while (binaryString.length() < bitLength) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

     //Formats the binary string by adding spaces between every byte (8 bits).

    private static String formatBinaryWithSpaces(String binary) {
        StringBuilder formattedBinary = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            if (i > 0) {
                formattedBinary.append(' ');
            }
            formattedBinary.append(binary.substring(i, i + 8));
        }
        return formattedBinary.toString();
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