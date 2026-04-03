package pl.comp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Algorithms {
    private static final Logger logger = LoggerFactory.getLogger(Algorithms.class);

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

}
