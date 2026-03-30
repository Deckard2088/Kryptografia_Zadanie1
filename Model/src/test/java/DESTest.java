import org.junit.jupiter.api.Test;
import pl.comp.DES;

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
        byte[] test = des.keyConfiguration(napisTestowy);
        for (byte b : test) {
            String binary = String.format("%8s", Integer.toBinaryString(b & 0xFF))
                    .replace(' ', '0');
            System.out.println(binary);
        }

    }
}
