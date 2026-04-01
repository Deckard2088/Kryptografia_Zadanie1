package pl.comp;

import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DES {
    private static final Logger logger = LoggerFactory.getLogger(DES.class);

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

    private static final byte[] numberOfShiftsForROL = {
            1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1
    };

    private static final byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            67, 49, 41, 33, 25, 17, 9, 1,
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

    //bajty zakres od -128 do 127
    //int to 4 bajty; short to 2 bajty, long 8 bajtów (64 bity)
    //Przygotowanie klucza (permutacja + pominięcie bitów parzystości)
    public byte[] keyConfiguration(String keyFromUser){
        //konwertowanie tekstu na bajty
        byte[] textBytes = keyFromUser.getBytes(StandardCharsets.UTF_8);
        byte[] finalKey = new byte[8];

        //klucz jest 64 bitowy, więc jeśli tekst ma mniej niż 8 bajtów to zostaje dodełniony zerami, a jeśli ma więcej to tylko pobieramy piersze 8 bajtów.
        for (int i = 0; i < 8; i++){
            if (i < textBytes.length){
                finalKey[i] = textBytes[i];
            } else {
                finalKey[i] = 0;
            }
        }
        byte[] finalfinal = new byte[8];
        finalfinal = bitPermutation(finalKey, PC1);
        return finalfinal;
    }

    public byte[] bitPermutation(byte[] byteTable, byte[] positions){
        byte[] permutatedBytetable = new byte[7];
        for (int i = 0; i < positions.length; i++){
            //pobieramy z którego bajtu chcemy wyciągnąc bit (np. dla 57. bitu to 6 bajt - przez to że ignorujemy bity parzystości)
            byte byteNumber = (byte) ((positions[i] - 1) / 8);
            //pobieramy który bit chcemy wyjąć w ramach tego bajtu (np. dla 57. bitu to 1 bit 7 bajtu)
            byte bitNumber = (byte) (7 - ((positions[i] - 1) % 8));
            //wartość bitu (0 lub 1)
            byte valueOfBit = (byte) ((byteTable[byteNumber] >> bitNumber) & 1);

            int outBit = 7 - (i % 8);
            permutatedBytetable[i/8] = (byte) (permutatedBytetable[i/8] | (valueOfBit << outBit));
        }
        return permutatedBytetable;
    }

    public byte[] generateSubKey(int numberOfRepeats, byte[] keyBitsIndexes){
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
            RightPart[j] = keyBitsIndexes[j];
        }

        for (int i = 0; i < numberOfRepeats; i++) {
            Algorithms.ROL(LeftPart);
            Algorithms.ROL(RightPart);
        }

        byte[] subKey = new byte[56];
        for (int i = 0; i < 28; i++){
            int j = i + 28;
            subKey[i] = LeftPart[i];
            subKey[j] = RightPart[j];
        }

        return subKey;
    }
}
