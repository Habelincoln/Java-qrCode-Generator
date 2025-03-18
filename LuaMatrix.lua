local QR = {
    -- Reed-Solomon GF(256) Tables
    logTable = {},
    expTable = {},

    -- QR Code Specification Tables
    byteCapacities = {
        {17,14,11,7}, {32,26,20,14}, {53,42,32,24}, {78,62,46,34}, {106,84,60,44},
        {134,106,74,58}, {154,122,86,64}, {192,152,108,84}, {230,180,130,98}, {271,213,151,119},
        {321,251,177,137}, {367,287,203,155}, {425,331,241,177}, {458,362,258,194}, {520,412,292,220},
        {586,450,322,250}, {644,504,364,280}, {718,560,394,310}, {792,624,442,338}, {858,666,482,382},
        {929,711,509,403}, {1003,779,565,439}, {1091,857,611,461}, {1171,911,661,511}, {1273,997,715,535},
        {1367,1059,751,593}, {1465,1125,805,625}, {1528,1190,868,658}, {1628,1264,908,698}, {1732,1370,982,742},
        {1840,1452,1030,790}, {1952,1538,1112,842}, {2068,1628,1168,898}, {2188,1722,1228,958}, {2303,1809,1283,983},
        {2431,1911,1351,1051}, {2563,1989,1423,1093}, {2699,2099,1499,1139}, {2809,2213,1579,1219}, {2953,2331,1663,1273}
    },

    ecTable = {
        {19,7,1,19,0,0}, {16,10,1,16,0,0}, {13,13,1,13,0,0}, {9,17,1,9,0,0},
        {34,10,1,34,0,0}, {28,16,1,28,0,0}, {22,22,1,22,0,0}, {16,28,1,16,0,0},
        {55,15,1,55,0,0}, {44,26,1,44,0,0}, {34,18,2,17,0,0}, {26,22,2,13,0,0},
        {80,20,1,80,0,0}, {64,18,2,32,0,0}, {48,26,2,24,0,0}, {36,16,4,9,0,0},
        {108,26,1,108,0,0}, {86,24,2,43,0,0}, {62,18,2,15,2,16}, {46,22,2,11,2,12},
        {136,18,2,68,0,0}, {108,16,4,27,0,0}, {76,24,4,19,0,0}, {60,28,4,15,0,0},
        {156,20,2,78,0,0}, {124,18,4,31,0,0}, {88,18,2,14,4,15}, {66,26,4,13,1,14},
        {194,24,2,97,0,0}, {154,22,2,38,2,39}, {110,22,4,18,2,19}, {86,26,4,14,2,15},
        {232,30,2,116,0,0}, {182,22,3,36,2,37}, {132,20,4,16,4,17}, {100,24,4,12,4,13},
        {274,18,2,68,2,69}, {216,26,4,43,1,44}, {154,24,6,19,2,20}, {122,28,6,15,2,16},
        {324,20,4,81,0,0}, {254,18,1,50,4,51}, {180,24,4,22,4,23}, {140,22,3,12,8,13},
        {370,24,2,92,2,93}, {290,22,6,36,2,37}, {206,20,4,20,6,21}, {158,26,7,14,4,15},
        {428,26,4,107,0,0}, {334,22,8,37,1,38}, {244,24,8,20,4,21}, {180,22,12,11,4,12},
        {461,30,3,115,1,116}, {365,24,4,40,5,41}, {261,20,11,16,5,17}, {197,24,11,12,5,13},
        {523,22,5,87,1,88}, {415,24,5,41,5,42}, {295,30,5,24,7,25}, {223,24,11,12,7,13},
        {589,24,5,98,1,99}, {453,28,7,45,3,46}, {325,24,15,19,2,20}, {253,30,3,15,13,16},
        {647,28,1,107,5,108}, {507,28,10,46,1,47}, {367,28,1,22,15,23}, {283,28,2,14,17,15},
        {721,30,5,120,1,121}, {563,26,9,43,4,44}, {397,28,17,22,1,23}, {313,28,2,14,19,15},
        {795,28,3,113,4,114}, {627,26,3,44,11,45}, {445,26,17,21,4,22}, {341,26,9,13,16,14},
        {861,28,3,107,5,108}, {669,26,3,41,13,42}, {485,30,15,24,5,25}, {385,28,15,15,10,16},
        {932,28,4,116,4,117}, {714,26,17,42,0,0}, {512,28,17,22,6,23}, {406,30,19,16,6,17},
        {1006,28,2,111,7,112}, {782,28,17,46,0,0}, {568,30,7,24,16,25}, {442,24,34,13,0,0},
        {1094,30,4,121,5,122}, {860,28,4,47,14,48}, {614,30,11,24,14,25}, {464,30,16,15,14,16},
        {1174,30,6,117,4,118}, {914,28,6,45,14,46}, {664,30,11,24,16,25}, {514,30,30,16,2,17},
        {1276,26,8,106,4,107}, {1000,28,8,47,13,48}, {718,30,7,24,22,25}, {538,30,22,15,13,16},
        {1370,28,10,114,2,115}, {1062,28,19,46,4,47}, {754,28,28,22,6,23}, {596,30,33,16,4,17},
        {1468,30,8,122,4,123}, {1128,28,22,45,3,46}, {808,30,8,23,26,24}, {628,30,12,15,28,16},
        {1531,30,3,117,10,118}, {1193,28,3,45,23,46}, {871,30,4,24,31,25}, {661,30,11,15,31,16},
        {1631,30,7,115,7,116}, {1267,28,21,45,7,46}, {911,30,1,23,37,24}, {701,30,19,15,26,16},
        {1735,30,5,115,10,116}, {1373,28,19,47,10,48}, {985,30,15,24,25,25}, {745,30,23,15,25,16},
        {1843,30,13,115,3,116}, {1455,28,2,46,29,47}, {1033,30,42,24,1,25}, {793,30,23,15,28,16},
        {1955,30,17,115,0,0}, {1541,28,10,46,23,47}, {1115,30,10,24,35,25}, {845,30,19,15,35,16},
        {2071,30,17,115,1,116}, {1631,28,14,46,24,47}, {1171,30,29,24,19,25}, {901,30,11,15,46,16},
        {2191,30,13,115,6,116}, {1725,28,14,46,28,47}, {1231,30,44,24,7,25}, {961,30,59,16,1,17},
        {2306,30,12,121,7,122}, {1812,28,12,47,26,48}, {1286,30,39,24,14,25}, {986,30,22,15,41,16},
        {2434,30,6,121,14,122}, {1914,28,6,47,34,48}, {1354,30,46,24,10,25}, {1054,30,2,15,64,16},
        {2566,30,17,122,4,123}, {1992,28,29,46,14,47}, {1426,30,49,24,10,25}, {1096,30,24,15,46,16},
        {2702,30,4,122,18,123}, {2102,28,13,46,32,47}, {1502,30,48,24,14,25}, {1142,30,42,15,32,16},
        {2812,30,20,117,4,118}, {2216,28,40,47,7,48}, {1582,30,43,24,22,25}, {1222,30,10,15,67,16},
        {2956,30,19,118,6,119}, {2334,28,18,47,31,48}, {1666,30,34,24,34,25}, {1276,30,20,15,61,16}
    },

    alignmentPositions = {
        [2] = {6, 18}, [3] = {6, 22}, [4] = {6, 26}, [5] = {6, 30},
        [6] = {6, 34}, [7] = {6, 22, 38}, [8] = {6, 24, 42},
        [9] = {6, 26, 46}, [10] = {6, 28, 50}, [11] = {6, 30, 54},
        [12] = {6, 32, 58}, [13] = {6, 34, 62}, [14] = {6, 26, 46, 66},
        [15] = {6, 26, 48, 70}, [16] = {6, 26, 50, 74}, [17] = {6, 30, 54, 78},
        [18] = {6, 30, 56, 82}, [19] = {6, 30, 58, 86}, [20] = {6, 34, 62, 90},
        [21] = {6, 28, 50, 72, 94}, [22] = {6, 26, 50, 74, 98},
        [23] = {6, 30, 54, 78, 102}, [24] = {6, 28, 54, 80, 106},
        [25] = {6, 32, 58, 84, 110}, [26] = {6, 30, 58, 86, 114},
        [27] = {6, 34, 62, 90, 118}, [28] = {6, 26, 50, 74, 98, 122},
        [29] = {6, 30, 54, 78, 102, 126}, [30] = {6, 26, 52, 78, 104, 130},
        [31] = {6, 30, 56, 82, 108, 134}, [32] = {6, 34, 60, 86, 112, 138},
        [33] = {6, 30, 58, 86, 114, 142}, [34] = {6, 34, 62, 90, 118, 146},
        [35] = {6, 30, 54, 78, 102, 126, 150}, [36] = {6, 24, 50, 76, 102, 128, 154},
        [37] = {6, 28, 54, 80, 106, 132, 158}, [38] = {6, 32, 58, 84, 110, 136, 162},
        [39] = {6, 26, 54, 82, 110, 138, 166}, [40] = {6, 30, 58, 86, 114, 142, 170}
    },

    formatInfo = {
        [0] = { -- L
            "111011111000100", "111001011110011", "111110110101010", "111100010011101",
            "110011000101111", "110001100011000", "110110001000001", "110100101110110"
        },
        [1] = { -- M
            "101010000010010", "101000100100101", "101111001111100", "101101101001011",
            "100010111111001", "100000011001110", "100111110010111", "100101010100000"
        },
        [2] = { -- Q
            "011010101011111", "011000001101000", "011111100110001", "011101000000110",
            "010010010110100", "010000110000011", "010111011011010", "010101111101101"
        },
        [3] = { -- H
            "001011010001001", "001001110111110", "001110011100111", "001100111010000",
            "000011101100010", "000001001010101", "000110100001100", "000100000111011"
        }
    },

    versionFormats = {
        [7] = "000111110010010100",
        [8] = "001000010110111100",
        [9] = "001001101010011001",
        [10] = "001010010011010011",
        [11] = "001011101111110110",
        [12] = "001100011101100010",
        [13] = "001101100001000111",
        [14] = "001110011000001101",
        [15] = "001111100100101000",
        [16] = "010000101101111000",
        [17] = "010001010001011101",
        [18] = "010010101000010111",
        [19] = "010011010100110010",
        [20] = "010100100110100110",
        [21] = "010101011010000011",
        [22] = "010110100011001001",
        [23] = "010111011111101100",
        [24] = "011000111011000100",
        [25] = "011001000111100001",
        [26] = "011010111110101011",
        [27] = "011011000010001110",
        [28] = "011100110000011010",
        [29] = "011101001100111111",
        [30] = "011110110101110101",
        [31] = "011111001001010000",
        [32] = "100000100111010101",
        [33] = "100001011011110000",
        [34] = "100010100010111010",
        [35] = "100011011110011111",
        [36] = "100100101100001011",
        [37] = "100101010000101110",
        [38] = "100110101001100100",
        [39] = "100111010101000001",
        [40] = "101000110001101001"
    }
}
function QR.encodeData(input, version, totalDataCodewords)
    local result = {}
    
    -- Add mode indicator (0100 for byte mode)
    table.insert(result, "0100")
    
    -- Add character count indicator
    local charCount = #input
    local bitLength = (version >= 1 and version <= 9) and 8 or 16
    table.insert(result, QR.toBinary(charCount, bitLength))
    
    -- Add data bytes
    for i = 1, #input do
        local c = input:sub(i, i)
        table.insert(result, QR.toBinary(string.byte(c), 8))
    end
    
    -- Combine all parts
    local encoded = table.concat(result)
    
    -- Calculate available bits
    local totalBits = totalDataCodewords * 8
    local currentLength = #encoded
    
    -- Add terminator bits
    local terminatorBits = math.min(4, totalBits - currentLength)
    encoded = encoded .. string.rep('0', terminatorBits)
    
    -- Pad to multiple of 8
    while #encoded % 8 ~= 0 do
        encoded = encoded .. '0'
    end
    
    -- Add padding bytes
    local paddingBytes = {
        "11101100",  -- 0xEC
        "00010001"   -- 0x11
    }
    local padIndex = 1
    
    while #encoded < totalBits do
        encoded = encoded .. paddingBytes[padIndex]
        padIndex = padIndex == 1 and 2 or 1
    end

    return encoded
end

-- Initialize Galois Field tables
function QR.initGaloisFields()
    local x = 1
    for i = 0, 254 do
        QR.expTable[i] = x
        QR.logTable[x] = i
        x = (x << 1) ~ ((x >= 0x80) and 0x11D or 0)
    end
    QR.expTable[255] = 1
end

-- GF Arithmetic
function QR.gfAdd(a, b) return a ~ b end

function QR.gfMultiply(a, b)
    if a == 0 or b == 0 then return 0 end
    return QR.expTable[(QR.logTable[a] + QR.logTable[b]) % 255]
end

-- Polynomial Generation
function QR.generateGeneratorPolynomial(errorCorrectionLength)
    local generator = {1}
    for i = 0, errorCorrectionLength-1 do
        local temp = {1, QR.expTable[i]}
        local newGenerator = {}
        for j = 1, #generator + #temp - 1 do newGenerator[j] = 0 end
        for j = 1, #generator do
            for k = 1, #temp do
                newGenerator[j+k-1] = QR.gfAdd(newGenerator[j+k-1], 
                    QR.gfMultiply(generator[j], temp[k]))
            end
        end
        generator = newGenerator
    end
    return generator
end

-- Reed-Solomon Encoding
function QR.reedSolomonEncode(data, eccLength)
    local genPoly = QR.generateGeneratorPolynomial(eccLength)
    local encoded = {}
    for i = 1, #data + eccLength do encoded[i] = 0 end
    for i = 1, #data do encoded[i] = data[i] end

    for i = 1, #data do
        local coef = encoded[i]
        if coef == 0 then goto continue end
        for j = 1, #genPoly do
            encoded[i+j-1] = QR.gfAdd(encoded[i+j-1], QR.gfMultiply(coef, genPoly[j]))
        end
        ::continue::
    end

    return {table.unpack(encoded, #data + 1, #data + eccLength)}
end
-- Binary Conversion
function QR.toBinary(value, bitLength)
    local bits = {}
    for i = bitLength-1, 0, -1 do
        bits[bitLength - i] = ((value >> i) % 2 == 1) and '1' or '0'
    end
    return table.concat(bits)
end

-- Data Splitting
function QR.splitIntoCodewords(encodedData)
    local codewords = {}
    for i = 1, #encodedData, 8 do
        table.insert(codewords, encodedData:sub(i, i+7))
    end
    return codewords
end

-- Block Interleaving
function QR.interleaveBlocks(dataBlocks, eccBlocks, ecCodewordsPerBlock)
    local finalMessage = {}
    
    -- Find max data codewords across all blocks
    local maxData = 0
    for _, block in ipairs(dataBlocks) do
        if #block > maxData then
            maxData = #block
        end
    end

    -- Interleave data codewords
    for i = 1, maxData do
        for _, block in ipairs(dataBlocks) do
            if block[i] then
                table.insert(finalMessage, block[i])
            end
        end
    end

    -- Interleave ECC codewords
    for i = 1, ecCodewordsPerBlock do
        for _, block in ipairs(eccBlocks) do
            if block[i] then
                table.insert(finalMessage, block[i])
            end
        end
    end

    return table.concat(finalMessage)
end

function QR.copyMatrix(matrix)
    local copy = {}
    for i = 1, #matrix do
        copy[i] = {}
        for j = 1, #matrix[i] do
            copy[i][j] = matrix[i][j]
        end
    end
    return copy
end

-- Remainder Bits
function QR.getRemainderBits(version)
    local remainderBits = {
        0,7,7,7,7,7,7,0,0,0,0,0,0,0,3,3,3,3,3,3,3,4,4,4,4,4,4,4,3,3,3,3,3,3,3,0,0,0,0,0,0
    }
    return string.rep('0', remainderBits[version] or 0)
end
-- Matrix Creation
function QR.createMatrix(message, version, ecLevel)
    local size = 21 + (version-1)*4
    local matrix = QR.createBlankMatrix(size)
    local filled = QR.createBlankMatrix(size)

    QR.addFinderPatterns(matrix, filled)
    QR.addSeparators(filled)
    QR.addTimingPatterns(matrix, filled)
    QR.addAlignmentPatterns(matrix, filled, version)
    QR.addReservedAreas(filled, version, size)
    QR.fillData(matrix, filled, message, size)
    
    local bestMask = QR.selectBestMask(matrix, filled, ecLevel, size)
    QR.applyMask(matrix, bestMask, filled)
    QR.addFormatInfo(matrix, ecLevel, bestMask, size)
    QR.addVersionInfo(matrix, version, size)    
    return matrix
end

function QR.createBlankMatrix(size)
    local m = {}
    for i = 1, size do
        m[i] = {}
        for j = 1, size do m[i][j] = 0 end
    end
    return m
end

-- Pattern Additions
function QR.addFinderPatterns(matrix, filled)
    local function addPattern(row, col)
        for i = row, row+6 do
            for j = col, col+6 do
                if i == row or i == row+6 or j == col or j == col+6 or
                   (i >= row+2 and i <= row+4 and j >= col+2 and j <= col+4) then
                    matrix[i][j] = 1
                end
                filled[i][j] = true
            end
        end
    end
    addPattern(1, 1)
    addPattern(1, #matrix-6)
    addPattern(#matrix-6, 1)
end

function QR.addSeparators(filled)
    local size = #filled
    for i = 1, 8 do
        filled[i][8] = true
        filled[8][i] = true
        filled[size-i+1][8] = true
        filled[8][size-i+1] = true
    end
end

function QR.addTimingPatterns(matrix, filled)
    local size = #matrix
    for i = 9, size-8 do
        matrix[7][i] = (i % 2 == 1) and 1 or 0
        matrix[i][7] = (i % 2 == 1) and 1 or 0
        filled[7][i] = true
        filled[i][7] = true
    end
end

function QR.addAlignmentPatterns(matrix, filled, version)
    local positions = QR.alignmentPositions[version]
    if not positions then return end
    
    for _, row in ipairs(positions) do
        for _, col in ipairs(positions) do
            if not QR.isInFinderArea(row, col, #matrix) then
                QR.addSingleAlignment(matrix, filled, row, col)
            end
        end
    end
end

function QR.isInFinderArea(row, col, size)
    return (row < 9 and col < 9) or 
           (row < 9 and col > size-8) or 
           (row > size-8 and col < 9)
end

function QR.addSingleAlignment(matrix, filled, row, col)
    for i = row-2, row+2 do
        for j = col-2, col+2 do
            if i == row-2 or i == row+2 or j == col-2 or j == col+2 or
               (i == row and j == col) then
                matrix[i][j] = 1
            end
            filled[i][j] = true
        end
    end
end

function QR.addReservedAreas(filled, version, size)
    for i = 1, 9 do
        filled[i][9] = true
        filled[9][i] = true
    end
    
    if version >= 7 then
        for i = size-11, size-8 do
            for j = 1, 6 do
                filled[i][j] = true
                filled[j][i] = true
            end
        end
    end
end

function QR.fillData(matrix, filled, message, size)
    print("message: ")
    print(message)
    print("fill data called. size: ")
    print(size)
    local dir = -1 -- -1 = up, 1 = down
    local row = size
    local col = size
    local bitIndex = 1
    
    -- Loop through columns in pairs, moving from right to left
    while col > 0 and bitIndex <= #message do
        -- Process two columns per iteration
        for _ = 1, 2 do
            
            -- Traverse rows in the current column, alternating direction
            while row >= 1 and row <= size do
                
                -- Check if the current cell is not already filled
                if not filled[row][col] then
                    -- Place the current bit from the message into the matrix
                    matrix[row][col] = tonumber(message:sub(bitIndex, bitIndex))
                    -- Move to the next bit in the message
                    bitIndex = bitIndex + 1
                    -- Debug: Print the placement of the bit
                    print("placed bit at: ")
                    print("row: ")
                    print(row)
                    print("col: ")
                    print(col)
                end
                
                -- Move to the next row in the current direction
                row = row + dir
            end
            
            -- Reverse the direction for the next column
            dir = -dir
            -- Move to the next column to the left
            col = col - 1
        end
        
        -- Adjust the row position for the next pair of columns
        row = row - 2
    end
end

function QR.selectBestMask(matrix, filled, ecLevel, size)
    local bestMask = 0
    local lowestScore = math.huge
    
    for mask = 0, 7 do
        local temp = QR.copyMatrix(matrix)
        QR.applyMask(temp, mask, filled)
        QR.addFormatInfo(temp, ecLevel, mask, size)
        local score = QR.calculatePenalty(temp)
        
        if score < lowestScore then
            lowestScore = score
            bestMask = mask
        end
    end
    
    return bestMask
end

function QR.saveAsHTML(matrix)
    local html = [[
<!DOCTYPE html>
<html>
<head>
    <title>QR Code</title>
    <style>
        .qr-cell { 
            width: 10px; 
            height: 10px; 
            display: inline-block;
        }
        .black { background-color: #000; }
        .white { background-color: #fff; }
        .qr-container { 
            border: 2px solid #333;
            padding: 10px;
            display: inline-block;
        }
    </style>
</head>
<body>
    <div class="qr-container">
]]

    for _, row in ipairs(matrix) do
        html = html .. [[<div style="line-height: 10px;">]]
        for _, val in ipairs(row) do
            html = html .. string.format([[<div class="qr-cell %s"></div>]], 
                val == 1 and "black" or "white")
        end
        html = html .. "</div>\n"
    end

    html = html .. [[
    </div>
</body>
</html>
]]

    local filename = "qrcode.html"
    local file = io.open(filename, "w")
    file:write(html)
    file:close()
    return filename
end

function QR.printDebug(matrix)
    for i, row in ipairs(matrix) do
        for j, val in ipairs(row) do
            io.write(val == 1 and "1 " or "0 ")
        end
        io.write("\n")
    end
end

function QR.applyMask(matrix, maskType, filled)
    for i = 1, #matrix do
        for j = 1, #matrix do
            if not filled[i][j] and QR.maskCondition(maskType, i, j) then
                matrix[i][j] = 1 - matrix[i][j]
            end
        end
    end
end

function QR.maskCondition(maskType, row, col)
    row = row - 1
    col = col - 1
    if maskType == 0 then return (row + col) % 2 == 0
    elseif maskType == 1 then return row % 2 == 0
    elseif maskType == 2 then return col % 3 == 0
    elseif maskType == 3 then return (row + col) % 3 == 0
    elseif maskType == 4 then return math.floor(row/2 + col/3) % 2 == 0
    elseif maskType == 5 then return (row*col) % 2 + (row*col) % 3 == 0
    elseif maskType == 6 then return ((row*col) % 2 + (row*col) % 3) % 2 == 0
    elseif maskType == 7 then return ((row+col) % 2 + (row*col) % 3) % 2 == 0 end
    return false
end
function QR.addFormatInfo(matrix, ecLevel, maskType, size)
    local formatStr = QR.formatInfo[ecLevel][maskType+1]
    
    -- Horizontal format info
    for i = 1, 7 do
        matrix[9][i] = tonumber(formatStr:sub(i, i))
    end
    matrix[9][9] = tonumber(formatStr:sub(8, 8))
    matrix[8][9] = tonumber(formatStr:sub(9, 9))
    
    -- Vertical format info
    for i = 10, 15 do
        matrix[15 - i + 1][9] = tonumber(formatStr:sub(i, i))
    end
    for i = 1, 7 do
        matrix[size - i + 1][9] = tonumber(formatStr:sub(i, i))
    end
    matrix[9][size - 7] = tonumber(formatStr:sub(8, 8))
end

function QR.addVersionInfo(matrix, version, size)
    if version < 7 then return end
    local versionStr = QR.versionFormats[version]
    
    -- Bottom-left version info
    local idx = 1
    for i = size-11, size-8 do
        for j = 1, 6 do
            matrix[i][j] = tonumber(versionStr:sub(idx, idx))
            idx = idx + 1
        end
    end
    
    -- Top-right version info
    idx = 1
    for j = size-11, size-8 do
        for i = 1, 6 do
            matrix[i][j] = tonumber(versionStr:sub(idx, idx))
            idx = idx + 1
        end
    end
end
function QR.calculatePenalty(matrix)
    return QR.penaltyCondition1(matrix) +
           QR.penaltyCondition2(matrix) +
           QR.penaltyCondition3(matrix) +
           QR.penaltyCondition4(matrix)
end

function QR.penaltyCondition1(matrix)
    local penalty = 0
    local size = #matrix
    
    -- Check rows
    for i = 1, size do
        local count = 1
        local current = matrix[i][1]
        for j = 2, size do
            if matrix[i][j] == current then
                count = count + 1
                if count == 5 then penalty = penalty + 3
                elseif count > 5 then penalty = penalty + 1 end
            else
                current = matrix[i][j]
                count = 1
            end
        end
    end
    
    -- Check columns
    for j = 1, size do
        local count = 1
        local current = matrix[1][j]
        for i = 2, size do
            if matrix[i][j] == current then
                count = count + 1
                if count == 5 then penalty = penalty + 3
                elseif count > 5 then penalty = penalty + 1 end
            else
                current = matrix[i][j]
                count = 1
            end
        end
    end
    
    return penalty
end

function QR.penaltyCondition2(matrix)
    local penalty = 0
    for i = 1, #matrix-1 do
        for j = 1, #matrix-1 do
            if matrix[i][j] == matrix[i][j+1] and
               matrix[i][j] == matrix[i+1][j] and
               matrix[i][j] == matrix[i+1][j+1] then
                penalty = penalty + 3
            end
        end
    end
    return penalty
end

function QR.penaltyCondition3(matrix)
    local penalty = 0
    local patterns = {
        {1,0,1,1,1,0,1,0,0,0,0},
        {0,0,0,0,1,0,1,1,1,0,1}
    }
    
    local function checkLine(line)
        local count = 0
        for i = 1, #line - 10 do
            for _, pattern in ipairs(patterns) do
                local match = true
                for j = 1, 11 do
                    if line[i+j-1] ~= pattern[j] then
                        match = false
                        break
                    end
                end
                if match then count = count + 1 end
            end
        end
        return count * 40
    end
    
    -- Check rows
    for i = 1, #matrix do
        penalty = penalty + checkLine(matrix[i])
    end
    
    -- Check columns
    for j = 1, #matrix do
        local column = {}
        for i = 1, #matrix do table.insert(column, matrix[i][j]) end
        penalty = penalty + checkLine(column)
    end
    
    return penalty
end

function QR.penaltyCondition4(matrix)
    local total = #matrix * #matrix
    local dark = 0
    for i = 1, #matrix do
        for j = 1, #matrix do
            if matrix[i][j] == 1 then dark = dark + 1 end
        end
    end
    local percent = (dark / total) * 100
    local nearest5 = math.floor((percent + 2.5) / 5) * 5
    return math.abs(nearest5 - 50) / 5 * 10
end
function QR.generate(input, ecLevel)
    QR.initGaloisFields()
    
    -- Determine version
    local version = -1
    local byteLength = #input
    for i = 1, #QR.byteCapacities do
        if byteLength <= QR.byteCapacities[i][ecLevel+1] then
            version = i
            break
        end
    end
    if version == -1 then return nil, "Input too long" end
    
    -- Get EC parameters
    local ecParams = QR.ecTable[(version-1)*4 + ecLevel + 1]
    
    -- Encode data
    local encoded = QR.encodeData(input, version, ecParams[1])
    local dataCodewords = QR.splitIntoCodewords(encoded)
    
    -- Split into blocks
    local dataBlocks = {}
    local idx = 1
    for _ = 1, ecParams[3] do -- Group 1
        local block = {}
        for _ = 1, ecParams[4] do
            table.insert(block, dataCodewords[idx])
            idx = idx + 1
        end
        table.insert(dataBlocks, block)
    end
    for _ = 1, ecParams[5] do -- Group 2
        local block = {}
        for _ = 1, ecParams[6] do
            table.insert(block, dataCodewords[idx])
            idx = idx + 1
        end
        table.insert(dataBlocks, block)
    end
    
    -- Generate ECC
    local eccBlocks = {}
    for _, block in ipairs(dataBlocks) do
        local data = {}
        for _, codeword in ipairs(block) do
            table.insert(data, tonumber(codeword, 2))
        end
        table.insert(eccBlocks, QR.reedSolomonEncode(data, ecParams[2]))
    end
    
    -- Final message
    local finalMessage = QR.interleaveBlocks(dataBlocks, eccBlocks, ecParams[2])
    finalMessage = finalMessage .. QR.getRemainderBits(version)
    
    -- Create matrix
    return QR.createMatrix(finalMessage, version, ecLevel)
end

function QR.print(matrix)
    for _, row in ipairs(matrix) do
        local line = {}
        for _, v in ipairs(row) do
            table.insert(line, v == 1 and "██" or "  ")
        end
        print(table.concat(line))
    end
end
--[[ Main Execution ]]--
print("QR Code Generator")
print("-----------------")

-- Get user input
io.write("Enter text to encode: ")
local input = io.read()
io.write("Error correction level (0=L, 1=M, 2=Q, 3=H): ")
local ecLevel = tonumber(io.read()) or 0

-- Generate and display
local matrix, err = QR.generate(input, ecLevel)
if not matrix then
    print("\nError:", err)
    return
end
QR.printDebug(matrix)
print("\nQR Code saved as qrcode.html")
QR.saveAsHTML(matrix)
os.execute("start qrcode.html")

return QR