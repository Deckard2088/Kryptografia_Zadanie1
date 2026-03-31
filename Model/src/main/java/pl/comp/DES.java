package pl.comp;

import java.nio.charset.StandardCharsets;

public class DES {
    //możnaby nazwy zmienić bo nwm czy później z czymś kolidować nie będą
    private static final byte[] schemaForPC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1 ,58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,//tymczasowe rozszerzenie bo cos sprawdzałem
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    //bajty zakres od -128 do 127
    //int to 4 bajty; short to 2 bajty, long 8 bajtów (64 bity)
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
        finalfinal = bitPermutation(finalKey, schemaForPC1);
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
            permutatedBytetable[i/8] = (byte) (permutatedBytetable[i/8] | (valueOfBit << bitNumber));
        }
        return permutatedBytetable;
    }
}
