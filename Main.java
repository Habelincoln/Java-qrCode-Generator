import static java.lang.Math.floor;
import java.util.*;


public class Main {
   
    private static String encodedData;
    private static int totalDataCodewords;
    private static int ecCodewordsPerBlock;
    private static int numBlocksGroup1;
    private static int dataCodewordsGroup1;
    private static int numBlocksGroup2;
    private static int dataCodewordsGroup2;
    private static int version = 0;
    private static int ecLevel = 0;
   
   
    
    private static final int[][] byteCapacities = {
        {17, 14, 11, 7}, {32, 26, 20, 14}, {53, 42, 32, 24}, {78, 62, 46, 34}, {106, 84, 60, 44},
        {134, 106, 74, 58}, {154, 122, 86, 64}, {192, 152, 108, 84}, {230, 180, 130, 98}, {271, 213, 151, 119},
        {321, 251, 177, 137}, {367, 287, 203, 155}, {425, 331, 241, 177}, {458, 362, 258, 194}, {520, 412, 292, 220},
        {586, 450, 322, 250}, {644, 504, 364, 280}, {718, 560, 394, 310}, {792, 624, 442, 338}, {858, 666, 482, 382},
        {929, 711, 509, 403}, {1003, 779, 565, 439}, {1091, 857, 611, 461}, {1171, 911, 661, 511}, {1273, 997, 715, 535},
        {1367, 1059, 751, 593}, {1465, 1125, 805, 625}, {1528, 1190, 868, 658}, {1628, 1264, 908, 698}, {1732, 1370, 982, 742},
        {1840, 1452, 1030, 790}, {1952, 1538, 1112, 842}, {2068, 1628, 1168, 898}, {2188, 1722, 1228, 958}, {2303, 1809, 1283, 983},
        {2431, 1911, 1351, 1051}, {2563, 1989, 1423, 1093}, {2699, 2099, 1499, 1139}, {2809, 2213, 1579, 1219}, {2953, 2331, 1663, 1273}
    };

    private static final int[][] ecTable = {
    {19, 7, 1, 19, 0, 0}, {16, 10, 1, 16, 0, 0}, {13, 13, 1, 13, 0, 0}, {9, 17, 1, 9, 0, 0}, {34, 10, 1, 34, 0, 0}, {28, 16, 1, 28, 0, 0}, {22, 22, 1, 22, 0, 0}, {16, 28, 1, 16, 0, 0}, {55, 15, 1, 55, 0, 0}, {44, 26, 1, 44, 0, 0}, {34, 18, 2, 17, 0, 0}, {26, 22, 2, 13, 0, 0}, {80, 20, 1, 80, 0, 0}, {64, 18, 2, 32, 0, 0}, {48, 26, 2, 24, 0, 0}, {36, 16, 4, 9, 0, 0}, {108, 26, 1, 108, 0, 0}, {86, 24, 2, 43, 0, 0}, {62, 18, 2, 15, 2, 16}, {46, 22, 2, 11, 2, 12}, {136, 18, 2, 68, 0, 0}, {108, 16, 4, 27, 0, 0}, {76, 24, 4, 19, 0, 0}, {60, 28, 4, 15, 0, 0}, {156, 20, 2, 78, 0, 0}, {124, 18, 4, 31, 0, 0}, {88, 18, 2, 14, 4, 15}, {66, 26, 4, 13, 1, 14}, {194, 24, 2, 97, 0, 0}, {154, 22, 2, 38, 2, 39}, {110, 22, 4, 18, 2, 19}, {86, 26, 4, 14, 2, 15}, {232, 30, 2, 116, 0, 0}, {182, 22, 3, 36, 2, 37}, {132, 20, 4, 16, 4, 17}, {100, 24, 4, 12, 4, 13}, {274, 18, 2, 68, 2, 69}, {216, 26, 4, 43, 1, 44}, {154, 24, 6, 19, 2, 20}, {122, 28, 6, 15, 2, 16}, {324, 20, 4, 81, 0, 0}, {254, 18, 1, 50, 4, 51}, {180, 24, 4, 22, 4, 23}, {140, 22, 3, 12, 8, 13}, {370, 24, 2, 92, 2, 93}, {290, 22, 6, 36, 2, 37}, {206, 20, 4, 20, 6, 21}, {158, 26, 7, 14, 4, 15}, {428, 26, 4, 107, 0, 0}, {334, 22, 8, 37, 1, 38}, {244, 24, 8, 20, 4, 21}, {180, 22, 12, 11, 4, 12}, {461, 30, 3, 115, 1, 116}, {365, 24, 4, 40, 5, 41}, {261, 20, 11, 16, 5, 17}, {197, 24, 11, 12, 5, 13}, {523, 22, 5, 87, 1, 88}, {415, 24, 5, 41, 5, 42}, {295, 30, 5, 24, 7, 25}, {223, 24, 11, 12, 7, 13}, {589, 24, 5, 98, 1, 99}, {453, 28, 7, 45, 3, 46}, {325, 24, 15, 19, 2, 20}, {253, 30, 3, 15, 13, 16},{647, 28, 1, 107, 5, 108}, {507, 28, 10, 46, 1, 47}, {367, 28, 1, 22, 15, 23}, {283, 28, 2, 14, 17, 15}, {721, 30, 5, 120, 1, 121}, {563, 26, 9, 43, 4, 44}, {397, 28, 17, 22, 1, 23}, {313, 28, 2, 14, 19, 15}, {795, 28, 3, 113, 4, 114}, {627, 26, 3, 44, 11, 45}, {445, 26, 17, 21, 4, 22}, {341, 26, 9, 13, 16, 14}, {861, 28, 3, 107, 5, 108}, {669, 26, 3, 41, 13, 42}, {485, 30, 15, 24, 5, 25}, {385, 28, 15, 15, 10, 16}, {932, 28, 4, 116, 4, 117}, {714, 26, 17, 42, 0, 0}, {512, 28, 17, 22, 6, 23}, {406, 30, 19, 16, 6, 17}, {1006, 28, 2, 111, 7, 112}, {782, 28, 17, 46, 0, 0}, {568, 30, 7, 24, 16, 25}, {442, 24, 34, 13, 0, 0}, {1094, 30, 4, 121, 5, 122}, {860, 28, 4, 47, 14, 48}, {614, 30, 11, 24, 14, 25}, {464, 30, 16, 15, 14, 16}, {1174, 30, 6, 117, 4, 118}, {914, 28, 6, 45, 14, 46}, {664, 30, 11, 24, 16, 25}, {514, 30, 30, 16, 2, 17}, {1276, 26, 8, 106, 4, 107}, {1000, 28, 8, 47, 13, 48}, {718, 30, 7, 24, 22, 25}, {538, 30, 22, 15, 13, 16}, {1370, 28, 10, 114, 2, 115}, {1062, 28, 19, 46, 4, 47}, {754, 28, 28, 22, 6, 23}, {596, 30, 33, 16, 4, 17}, {1468, 30, 8, 122, 4, 123}, {1128, 28, 22, 45, 3, 46}, {808, 30, 8, 23, 26, 24}, {628, 30, 12, 15, 28, 16}, {1531, 30, 3, 117, 10, 118}, {1193, 28, 3, 45, 23, 46}, {871, 30, 4, 24, 31, 25}, {661, 30, 11, 15, 31, 16}, {1631, 30, 7, 115, 7, 116}, {1267, 28, 21, 45, 7, 46}, {911, 30, 1, 23, 37, 24}, {701, 30, 19, 15, 26, 16}, {1735, 30, 5, 115, 10, 116}, {1373, 28, 19, 47, 10, 48}, {985, 30, 15, 24, 25, 25}, {745, 30, 23, 15, 25, 16}, {1843, 30, 13, 115, 3, 116}, {1455, 28, 2, 46, 29, 47}, {1033, 30, 42, 24, 1, 25}, {793, 30, 23, 15, 28, 16}, {1955, 30, 17, 115, 0, 0},
    {1541, 28, 10, 46, 23, 47}, {1115, 30, 10, 24, 35, 25}, {845, 30, 19, 15, 35, 16},{2071, 30, 17, 115, 1, 116}, {1631, 28, 14, 46, 24, 47}, {1171, 30, 29, 24, 19, 25}, {901, 30, 11, 15, 46, 16}, {2191, 30, 13, 115, 6, 116}, {1725, 28, 14, 46, 28, 47}, {1231, 30, 44, 24, 7, 25}, {961, 30, 59, 16, 1, 17}, {2306, 30, 12, 121, 7, 122}, {1812, 28, 12, 47, 26, 48}, {1286, 30, 39, 24, 14, 25}, {986, 30, 22, 15, 41, 16}, {2434, 30, 6, 121, 14, 122}, {1914, 28, 6, 47, 34, 48}, {1354, 30, 46, 24, 10, 25}, {1054, 30, 2, 15, 64, 16}, {2566, 30, 17, 122, 4, 123}, {1992, 28, 29, 46, 14, 47}, {1426, 30, 49, 24, 10, 25}, {1096, 30, 24, 15, 46, 16}, {2702, 30, 4, 122, 18, 123}, {2102, 28, 13, 46, 32, 47}, {1502, 30, 48, 24, 14, 25}, {1142, 30, 42, 15, 32, 16}, {2812, 30, 20, 117, 4, 118}, {2216, 28, 40, 47, 7, 48}, {1582, 30, 43, 24, 22, 25}, {1222, 30, 10, 15, 67, 16}, {2956, 30, 19, 118, 6, 119}, {2334, 28, 18, 47, 31, 48}, {1666, 30, 34, 24, 34, 25}, {1276, 30, 20, 15, 61, 16}
};
   
    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter input:");
    String input = scanner.nextLine();
    System.out.println("Enter error correction level (0 = L, 1 = M, 2 = Q, 3 = H):");
    ecLevel = scanner.nextInt();
    scanner.close();
   
    //set the version based on the input length
    setVersion(input);
    if (version != -1) {
        int index = (version - 1) * 4 + ecLevel;
        totalDataCodewords = ecTable[index][0];
        ecCodewordsPerBlock = ecTable[index][1];
        numBlocksGroup1 = ecTable[index][2];
        dataCodewordsGroup1 = ecTable[index][3];
        numBlocksGroup2 = ecTable[index][4];
        dataCodewordsGroup2 = ecTable[index][5];

        System.out.println("Version: " + version);
    } else {
        System.out.println("too long");
        return;
    }

    // encode the input data into binary
    encodedData = encodeToBinary(input);

    String[] dataCodewords = splitIntoCodewords(encodedData);

    // split codewords into blocks
    List<String[]> dataBlocks = splitIntoBlocks(dataCodewords);

    // generate error correction codewords for each block
    List<String[]> eccBlocks = new ArrayList<>();
    for (String[] block : dataBlocks) {
        int[] data = new int[block.length];
        for (int i = 0; i < block.length; i++) {
            data[i] = Integer.parseInt(block[i], 2);
        }
        int[] eccCodewords = reedSolomonEncode(data, ecCodewordsPerBlock);
        String[] eccBinary = new String[eccCodewords.length];
        for (int i = 0; i < eccCodewords.length; i++) {
            eccBinary[i] = toBinary(eccCodewords[i], 8);
        }
        eccBlocks.add(eccBinary);
    }

    // interleave data and error correction codewords into the final message
    String finalMessage = interleaveBlocks(dataBlocks, eccBlocks);
    finalMessage = finalMessage + getRemainderBits(version);
    new MatrixBuilder(finalMessage, version, ecLevel);
    }

   private static void setVersion(String input) {
        int length = input.getBytes().length;
        for (int i = 0; i < byteCapacities.length; i++) {
            if (length <= byteCapacities[i][ecLevel]) {
                version = i + 1;
                break;
            }
        }
        if (version == 0) {
            version = -1;
        }
       
    }
   
    private static String encodeToBinary(String input) {
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
            result.append(toBinary(c, 8));
        }

        // add terminator Bits
        while ((totalDataCodewords * 8) - result.length() > 0) {
            if ((totalDataCodewords * 8) - result.length() >= 4) {
                result.append("0000");
                break;
            } else {
                result.append('0');
            }
           
        }
       
        int terminatorBits = Math.min(4, (totalDataCodewords * 8) - result.length());
        for (int i = 0; i < terminatorBits; i++) {
            result.append('0');
        }

        // pad to full byte (multiples of 8)
        while (result.length() % 8 != 0) {
            result.append('0');
        }

        // add more padding Bytes (0xEC and 0x11 alternately)
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

     // convert int to bit string

    private static String toBinary(int value, int bitLength) {
        String binaryString = Integer.toBinaryString(value);
        while (binaryString.length() < bitLength) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }


// begin ec

private static final int[] logTable = new int[256];
private static final int[] expTable = new int[256];

// initialize GF(256) tables
static {
    int x = 1;
    for (int i = 0; i < 255; i++) {
        expTable[i] = x;
        logTable[x] = i;
        x <<= 1;
        if ((x & 0x100) != 0) {
            x ^= 0x11D;
        }
    }
    expTable[255] = 1;
}


private static int gfAdd(int a, int b) {
    return a ^ b;
}


private static int gfMultiply(int a, int b) {
    if (a == 0 || b == 0) {
        return 0;
    }
    return expTable[(logTable[a] + logTable[b]) % 255];
}


private static int[] generateGeneratorPolynomial(int errorCorrectionLength) {
    int[] generator = {1};

    for (int i = 0; i < errorCorrectionLength; i++) {
        int[] temp = {1, expTable[i]};
        generator = multiplyPolynomials(generator, temp);
    }

    return generator;
}


private static int[] multiplyPolynomials(int[] a, int[] b) {
    int[] result = new int[a.length + b.length - 1];
    for (int i = 0; i < a.length; i++) {
        for (int j = 0; j < b.length; j++) {
            result[i + j] = gfAdd(result[i + j], gfMultiply(a[i], b[j]));
        }
    }
    return result;
}

// Reed-Solomon encode data
private static int[] reedSolomonEncode(int[] data, int errorCorrectionLength) {
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

// split encoded data into codewords 
private static String[] splitIntoCodewords(String encodedData) {
    int codewordLength = 8; // Each codeword is 8 bits
    int numCodewords = encodedData.length() / codewordLength;
    String[] codewords = new String[numCodewords];
    for (int i = 0; i < numCodewords; i++) {
        codewords[i] = encodedData.substring(i * codewordLength, (i + 1) * codewordLength);
    }
    return codewords;
}

// split codewords into blocks
private static List<String[]> splitIntoBlocks(String[] dataCodewords) {
    List<String[]> blocks = new ArrayList<>();

    // split into group 1 blocks
    for (int i = 0; i < numBlocksGroup1; i++) {
        String[] block = new String[dataCodewordsGroup1];
        System.arraycopy(dataCodewords, i * dataCodewordsGroup1, block, 0, dataCodewordsGroup1);
        blocks.add(block);
    }

    // split into group 2 blocks (if there are any)
    if (numBlocksGroup2 > 0) {
        int startIndex = numBlocksGroup1 * dataCodewordsGroup1;
        for (int i = 0; i < numBlocksGroup2; i++) {
            String[] block = new String[dataCodewordsGroup2];
            System.arraycopy(dataCodewords, startIndex + i * dataCodewordsGroup2, block, 0, dataCodewordsGroup2);
            blocks.add(block);
        }
    }

    return blocks;
}

// interleave data and error correction codewords into final message
private static String interleaveBlocks(List<String[]> dataBlocks, List<String[]> eccBlocks) {
    StringBuilder finalMessage = new StringBuilder();

    // interleave data codewords
    int maxDataCodewords = Math.max(dataCodewordsGroup1, dataCodewordsGroup2);
    for (int i = 0; i < maxDataCodewords; i++) {
        for (String[] block : dataBlocks) {
            if (i < block.length) {
                finalMessage.append(block[i]);
            }
        }
    }

    // interleave error correction codewords
    int maxEccCodewords = ecCodewordsPerBlock;
    for (int i = 0; i < maxEccCodewords; i++) {
        for (String[] block : eccBlocks) {
            if (i < block.length) {
                finalMessage.append(block[i]);
            }
        }
    }

    return finalMessage.toString();
}

private static String getRemainderBits(int version) {

        // number of required remainder bits for each QR code version
        int[] remainderBits = {
            0, 7, 7, 7, 7, 7, 7, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0
        };

        int bits = remainderBits[version - 1];
        return "0".repeat(bits);
    }

}

 class MatrixBuilder {
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
            System.out.println("Final message: " + message);
           
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
            for (int i = size - 11; i <= size - 9; i++) {
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