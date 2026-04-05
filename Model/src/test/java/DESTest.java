import org.junit.jupiter.api.Test;
import pl.comp.Algorithms;
import pl.comp.DES;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DESTest {
    @Test
    public void testKeyConfiguration(){
        DES des = new DES();
        String napisTestowy = "Tego typu benc";
        byte[] bytes = napisTestowy.getBytes();

        for (byte b : bytes) {
            String binary = String.format("%8s", Integer.toBinaryString(b & 0xFF))
                    .replace(' ', '0');
            System.out.println(binary);
        }
        System.out.println("=======================");
        byte[] test = des.keyConfiguration(bytes);
        for (byte b : test) {
            String binary = String.format("%8s", Integer.toBinaryString(b & 0xFF))
                    .replace(' ', '0');
            System.out.println(binary);
        }

    }

    @Test
    public void testMainFunction(){
        DES des = new DES();
        byte[] klucz = Algorithms.generateRandomKey();

        des.createSubKeysArray(klucz);
        String testowyBlok = "XD12mu67";
        byte[] textBytes = testowyBlok.getBytes(StandardCharsets.UTF_8);
        //byte[] finalKey = new byte[8];
        byte[] encrypted = des.processBlock(textBytes, false);
        String str = new String(encrypted, StandardCharsets.UTF_8);
        System.out.println(str);
    }

    @Test
    public void testMain2Function() {
        DES des = new DES();
        byte[] klucz = Algorithms.generateRandomKey();
        des.createSubKeysArray(klucz);

        String testowyBlok = "XD12mu67";
        byte[] textBytes = testowyBlok.getBytes(StandardCharsets.UTF_8);

        // szyfrowanie
        byte[] encrypted = des.processBlock(textBytes, false);
        String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);
        System.out.println("Zaszyfrowany: " + encryptedBase64);
        System.out.println("======");

        byte[] decrypted = des.processBlock(encrypted, true);
        String str = new String(decrypted, StandardCharsets.UTF_8);
        System.out.println(str);
    }
}
