package pl.comp;

public final class Algorithms {
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


}
