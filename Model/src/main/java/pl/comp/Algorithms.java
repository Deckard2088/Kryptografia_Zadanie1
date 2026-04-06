package pl.comp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.List;
import java.util.Random;

public final class Algorithms {
    private static final Logger logger = LoggerFactory.getLogger(Algorithms.class);
    private final Random random = new Random();

    //prywatny konstruktor, żeby nie tworzyć instacji tej klasy
    private Algorithms() {
    }

    public static void ROL(byte[] byteTable){
        byte temp = byteTable[0];
        for (int j = 0; j < byteTable.length-1; j++){
            byteTable[j] = byteTable[j+1];
        }
        byteTable[byteTable.length - 1] = temp;
    }

    //Operacja ROL w wersji bitowej
    public static int ROLbits(int value, int shifts){
        //w int value trzymamy 28 bitów, czyli pierwsze 4 bity od lewej to 0
        int mask = 0x0FFFFFFF;
        //zabezpieczenie
        value = value & mask;
        //robimy OR dla bitów przesuniętych w lewo i dla tych 'zawiniętych', a następnie AND z maską aby uzyskać 28 bitów
        value = ((value << shifts) | (value >> (28-shifts))) & mask;
        return value;
    }

    //Funkcja odpowiedzialna za rozdzielenie tablicy bajtów (wykorzystana przy podziale 56 bitowego klucza na dwie 28 bitowe połowy)
    public static int separateByte(byte[] tab, int firstBit, int amountOfBits){
        int value = 0;
        for (int i = 0; i < amountOfBits; i++){
            int byteNumber = (firstBit+i)/8;
            int bitNumber = 7 - ((firstBit+i) % 8);
            int valueOfBit = ((tab[byteNumber] >> bitNumber) & 1);
            value = (value << 1) | valueOfBit;
        }
        return value;
    }

    //Funkcja scalająca z powrotem dwie połówki klucza (int zawierają 28 bitowe połówki z których powstaje 56 bitowy klucz)
    public static byte[] mergeSides(int left, int right){
        long combined = ((long) left << 28) | (right & 0x0FFFFFFFL);
        byte[] result = new byte[7];
        for (int i = 6; i >= 0; i--) {
            result[i] = (byte) (combined & 0xFF);
            combined >>= 8;
        }
        return result;
    }

    //xorowanie dwóch tablic bajtów
    public static byte[] xor(byte[] firstTable, byte[] secondTable){
        if (firstTable.length != secondTable.length){
            logger.info("Błąd: Tablice nie są tej samej długości, operacja XOR zakończona niepowodzeniem");
            return null;
        }

        byte[] finalTable = new byte[firstTable.length];
        for (int i = 0; i < firstTable.length; i++){
            finalTable[i] = (byte) (firstTable[i] ^ secondTable[i]);
        }
        return finalTable;
    }

    //Funkcja pomocncicza konwertująca tablicę bajtów na long (dla wygody)
    public static final long toLong (byte[] byteArray, int offset, int len)
    {
        long val = 0;
        len = Math.min(len, 8);
        for (int i = 0; i < len; i++)
        {
            val <<= 8;
            val |= (byteArray[offset + i] & 0xFF);
        }
        return val;
    }

    //Funkcja generująca klucz w sposób losowy
    public static byte[] generateRandomKey(){
        /*
        byte[] randomKey = new byte[8];
        new Random().nextBytes(randomKey);
        return HexFormat.of().formatHex(randomKey);
         */
        byte[] randomKey = new byte[8];
        new SecureRandom().nextBytes(randomKey);
        return randomKey;
    }

    //Funkcja która przetwarza listę tablic bajtów na jedną dłuuugą tablicę bajtów
    public static byte[] joinBytesFromList(List<byte[]> blocks, int blockSize){
        byte[] joinedBytes = new byte[blocks.size()*blockSize];

        int firstBytePos = 0;
        for (byte[] block : blocks) {
            System.arraycopy(block, 0, joinedBytes, firstBytePos, blockSize);
            firstBytePos += blockSize;
        }
        return joinedBytes;
    }
}
