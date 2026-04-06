package pl.comp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DES {
    private static final Logger logger = LoggerFactory.getLogger(DES.class);

    //DANE TABELARYCZNE SĄ Z WIKIPEDII POZDRO BYQ
    //możnaby nazwy zmienić bo nwm czy później z czymś kolidować nie będą
    private static final byte[] PC1 = {
            57, 49, 41, 33, 25, 17, 9, 1,
            58, 50, 42, 34, 26, 18, 10, 2,
            59, 51, 43, 35, 27, 19, 11, 3,
            60, 52, 44, 36, 63, 55, 47, 39,
            31, 23, 15, 7, 62, 54, 46, 38,
            30, 22, 14, 6, 61, 53, 45, 37,
            29, 21, 13, 5, 28, 20, 12, 4
    };

    //PC-2 ?
    private static final byte[] compPBOX = {
            14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
    };

    //to jest to do feistela ostatni
    private static final byte[] PBox = {
            16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25
    };

    private static final byte[] numberOfShiftsForROL = {
            1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1
    };

    private static final byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final byte[] IPminus1 = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    //permutaacja rozszerzona
    private static final byte[] E = {
            32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1
    };

    private static final byte[][] SBox = {
            //S1
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13},
            //S2
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9},
            //S3
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12},
            //S4
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14},
            //S5
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3},
            //S6
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13},
            //S7
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12},
            //S8
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
    };

    //tablica z podkluczami
    private byte[][] subKeys = new byte[16][];

    //bajty zakres od -128 do 127
    //int to 4 bajty; short to 2 bajty, long 8 bajtów (64 bity)
    //Przygotowanie klucza (permutacja + pominięcie bitów parzystości)
    public byte[] keyConfiguration(byte[] keyFromUser){
        //konwertowanie tekstu na bajty
        //byte[] textBytes = keyFromUser.getBytes(StandardCharsets.UTF_8);
        byte[] finalKey = new byte[8];

        //klucz jest 64 bitowy, więc jeśli tekst ma mniej niż 8 bajtów to zostaje dodełniony zerami, a jeśli ma więcej to tylko pobieramy piersze 8 bajtów.
        for (int i = 0; i < 8; i++){
            if (i < keyFromUser.length){
                finalKey[i] = keyFromUser[i];
            } else {
                finalKey[i] = 0;
            }
        }
        return bitPermutation(finalKey, PC1);
    }

    public byte[] bitPermutation(byte[] byteTable, byte[] positions){
        //funckja ma działa uniwersalnie i stąd to oblicznie w długości tablicy permutatedBytetable
        //dla 64 bitowego wejścia dostajemy 56 bitowe wyjście
        byte[] permutatedBytetable = new byte[(positions.length + 7) / 8];
        for (int i = 0; i < positions.length; i++){
            //pobieramy z którego bajtu chcemy wyciągnąc bit (np. dla 57. bitu to 6 bajt - przez to że ignorujemy bity parzystości)
            //& 0xFF
            int byteNumber = ((positions[i] - 1) / 8);
            //pobieramy który bit chcemy wyjąć w ramach tego bajtu (np. dla 57. bitu to 1 bit 7 bajtu)
            int bitNumber = (7 - ((positions[i] - 1) % 8));
            //wartość bitu (0 lub 1): bit który nas interesuje zostaje przeniesiony skrajnie na prawo a następnie operacja OR z 1
            int valueOfBit = ((byteTable[byteNumber] >> bitNumber) & 1);

            int outBit = 7 - (i % 8);
            permutatedBytetable[i/8] = (byte) (permutatedBytetable[i/8] | (valueOfBit << outBit));
        }
        return permutatedBytetable;
    }

    //same indeksy czyli de facto PC1 będzie drugim argumentem
    public byte[] generateSubKey(byte numberOfRepeats, byte[] keyBitsIndexes, byte[] key){
        if (keyBitsIndexes.length != 56){
            logger.info("Podany klucz nie ma 56 bitów");
            return null;
        }

        //dzielimy tablice na pół
        byte[] LeftPart = new byte[28];
        byte[] RightPart = new byte[28];
        for (int i = 0; i < 28; i++){
            int j = i + 28;
            LeftPart[i] = keyBitsIndexes[i];
            RightPart[i] = keyBitsIndexes[j];
        }

        for (int i = 0; i < numberOfRepeats; i++) {
            Algorithms.ROL(LeftPart);
            Algorithms.ROL(RightPart);
        }

        byte[] subKey = new byte[56];
        for (int i = 0; i < 28; i++){
            int j = i + 28;
            subKey[i] = LeftPart[i];
            subKey[j] = RightPart[i];
        }

        return bitPermutation(key, subKey);
    }
    //w takiej formie to najlepiej chyba będzie zrobić że wynik tej funkcji później będzie jej argumentem przy generowaniu kolejnych kluczy
    //poprawić tę funckję od permutacji
    //dodać tablicę z comp. p box
    // sprawdzić czy dobrze tworzy ten 48 bitowy podklucz

    public void createSubKeysArray(byte[] key){
        byte[] configuratedKey = keyConfiguration(key);
        //byte[] tablicaPomocnicza = generateSubKey((byte) 0, PC1, configuratedKey);

        /*
        for (int i = 0; i < subKeys.length; i++){
            byte[] subKey = generateSubKey(numberOfShiftsForROL[i], PC1, tablicaPomocnicza);
            subKeys[i] = bitPermutation(subKey, compPBOX);
        }
        */
        int leftSide = Algorithms.separateByte(configuratedKey, 0, 28);
        int rightSide = Algorithms.separateByte(configuratedKey, 28, 28);

        //16 rund
        for (int i = 0; i < 16; i++){
            leftSide = Algorithms.ROLbits(leftSide, numberOfShiftsForROL[i]);
            rightSide = Algorithms.ROLbits(rightSide, numberOfShiftsForROL[i]);
            byte[] finalSubKey = Algorithms.mergeSides(leftSide, rightSide);
            subKeys[i] = bitPermutation(finalSubKey, compPBOX);
        }
    }

    public byte[] feistelFunctions(byte[] subKey, byte[] rightSide){
        //permutacja roszerzona (48 bitów)
        byte[] permutatedRightSide = bitPermutation(rightSide, E);
        //XOR z bitami podklucza
        byte[] xored = Algorithms.xor(subKey, permutatedRightSide);
        long xoredLong = Algorithms.toLong(xored, 0, xored.length);
        //dzielimy to na 8 grup po 6 bitów
        byte[] groups = new byte[8];
        for (int i = 0; i < groups.length; i++){
            groups[i] =  (byte) (xoredLong >> (42 - i * 6) & 0b00111111);
        }
        //odczytujemy wartości z SBOXów
        byte[] valuesSBox = new byte[4];
        for (int i = 0; i < groups.length; i++){
            /*
            int firstBit = ((groups[i] >> 5) << 1) & 0b00000010;
            int lastBit = groups[i] & 1;
            int row = firstBit | lastBit;
            int column = groups[i] >> 1 & 0b00011110;
            int sBox = SBox[i][16 * row + column] & 0x0F;
            //bajty w tej tablicy są w połowie puste, a do dalszej operacji potrzebne są 32 bity więc 'kompresujemy'
            if (i % 2 == 1){
                valuesSBox[i] = (byte) (valuesSBox[i] | sBox);
            } else {
                valuesSBox[i] = (byte) (sBox << 4);
            }*/
            int firstBit = (groups[i] >> 5) & 1;       // bit 5 (MSB grupy)
            int lastBit  =  groups[i] & 1;              // bit 0 (LSB grupy)
            int row      = (firstBit << 1) | lastBit;   // 0–3
            int column   = (groups[i] >> 1) & 0x0F;    // bity 1–4, wartość 0–15

            int sBoxVal  = SBox[i][16 * row + column] & 0x0F; // 4 bity

            int idx = i / 2;
            if (i % 2 == 0) {
                valuesSBox[idx] = (byte) (sBoxVal << 4);              // starsze 4 bity
            } else {
                valuesSBox[idx] = (byte) (valuesSBox[idx] | sBoxVal); // młodsze 4 bity
            }
        }

        return bitPermutation(valuesSBox, PBox);
    }

    //funkcja szyfrująca/deszyfrująca blok; jeśli deszyfruje to drugi argument true;
    public byte[] processBlock(byte[] block, boolean decrypt){
        block = bitPermutation(block, IP);
        byte[] LeftPart = new byte[4];
        byte[] RightPart = new byte[4];

        for (int i = 0; i < 4; i++){
            LeftPart[i] = block[i];
            RightPart[i] = block[i+4];
        }

        for (int i = 0; i < 16; i++){
            int keyIndex = decrypt ? (15 - i) : i;
            byte[] bytesAfterFeistel = feistelFunctions(subKeys[keyIndex], RightPart);
            byte[] newRightSide = Algorithms.xor(bytesAfterFeistel, LeftPart);
            LeftPart = RightPart;
            RightPart = newRightSide;
        }

        byte[] encryptedBlock = new byte[8];
        for (int i = 0; i < 4; i++){
            encryptedBlock[i]     = RightPart[i];  // R16 idzie pierwszy
            encryptedBlock[i + 4] = LeftPart[i];   // L16 idzie drugi
        }

        return bitPermutation(encryptedBlock, IPminus1);
    }

    public List<byte[]> encryptBlocks(byte[] input){
        List<byte[]> blocks = createBlocks(input, 8);
        List<byte[]> encryptedBlocks = new ArrayList<>();

        for (byte[] block: blocks){
            byte[] encryptedBlock = processBlock(block, false);
            encryptedBlocks.add(encryptedBlock);
        }
        return encryptedBlocks;
    }

    public List<byte[]> decryptBlocks(byte[] input){
        List<byte[]> blocks = createBlocks(input, 8);
        List<byte[]> decryptedBlocks = new ArrayList<>();

        for (byte[] block: blocks){
            byte[] decryptedBlock = processBlock(block, true);
            decryptedBlocks.add(decryptedBlock);
        }

        return decryptedBlocks;
    }

    public List<byte[]> createBlocks(byte[] input, int blockSize){
        int numberOfBytes = input.length;
        List<byte[]> blocks = new ArrayList<>();
        //liczba nieużytych bajtów, użyte do uzupełnienia bloku
        int padding = blockSize - (input.length % blockSize);
        if (padding == 0) {
            padding = blockSize;
        }
        //tablica z paddingiem
        byte[] tableWithPadding = new byte[input.length + padding];
        //przekopiowanie wartości z input do nowej tablicy
        System.arraycopy(input, 0, tableWithPadding, 0, blockSize);
        //wstawienie paddingu
        for (int i = numberOfBytes; i < tableWithPadding.length; i++){
            tableWithPadding[i] = (byte) padding;
        }

        //dzielenie na bloki tablicy z paddingiem
        for (int i = 0; i < tableWithPadding.length; i += blockSize){
            byte[] block = new byte[blockSize];
            //arrayCopy to to samo co kopiowanie przez pętlę for
            System.arraycopy(input, i, block, 0, blockSize);
            blocks.add(block);
        }
        return blocks;
    }
}
