import org.junit.jupiter.api.Test;
import pl.comp.Algorithms;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmsTest {
    @Test
    public void testROL(){
        byte[] tablicatestowa = {1,2,3,4,5,6,7,8,9};
        for (int i = 0; i<tablicatestowa.length; i++) {
            System.out.print(tablicatestowa[i] + " ");
        }

        System.out.println(tablicatestowa.toString());

        Algorithms.ROL(1, tablicatestowa);
        for (int i = 0; i<tablicatestowa.length; i++) {
            System.out.print(tablicatestowa[i] + " ");
        }
    }
}
