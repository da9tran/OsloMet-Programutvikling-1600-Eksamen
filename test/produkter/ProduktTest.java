package produkter;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProduktTest {

    @Test
    void setPris() {


        Korn k = new Korn("",0, "", "");
        assertEquals(0, k.getPris());
    }


    @Test
    void setPris_2() {


        Korn k = new Korn("",10, "", "");
        assertEquals(10, k.getPris());
    }
}