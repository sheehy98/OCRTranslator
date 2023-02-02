/* This is just a placeholder. */
package ocr;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OCRTest {

    @Test
    void testNull() { 
        OCRTranslator tester = new OCRTranslator();

        assertThrows(OCRException.class, () -> {
            tester.translate(null, null, null);
        });

        assertThrows(OCRException.class, () -> {
            tester.translate("___", null, null);
        });

        assertThrows(OCRException.class, () -> {
            tester.translate(null, "___", "___");
        });
    }

    @Test
    void testEmpty() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate("", "", ""), "");
        
        assertEquals(tester.translate("  ", "  ", "  "), "");
    }

    @Test
    void testInvalidLengths() { 
        OCRTranslator tester = new OCRTranslator();

        assertThrows(OCRException.class, () -> {
            tester.translate("_", "__", "___");
        });

        assertThrows(OCRException.class, () -> {
            tester.translate("_", "__", "_");
        });

        assertThrows(OCRException.class, () -> {
            tester.translate("", "___", "___");
        });
    }

    @Test
    void testInvalidCharacters() { 
        OCRTranslator tester = new OCRTranslator();

        assertThrows(OCRException.class, () -> {
            tester.translate("b", "b", "b");
        });

        assertThrows(OCRException.class, () -> {
            tester.translate(" ", " ", "b");
        });

        assertThrows(OCRException.class, () -> {
            tester.translate("___", "_b_", "___");
        });
    }

    @Test
    void testOne() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate(" ", "|", "|"), "1");

        assertEquals(tester.translate("    ", "   |", "   |"), "1");
        
        assertEquals(tester.translate("  ", "| ", "| "), "1");
    }

    @Test
    void testThree() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate("_ ", "_|", "_|"), "3");

        assertEquals(tester.translate("   _ ", "   _|", "   _|"), "3");
        
        assertEquals(tester.translate("_  ", "_| ", "_| "), "3");
    }

    @Test
    void testInvalidDigit() { 
        OCRTranslator tester = new OCRTranslator();

        assertThrows(OCRException.class, () -> {
            tester.translate("|||", "|||", "|||");
        });

        assertThrows(OCRException.class, () -> {
            tester.translate("  ", " ||", " ||");
        });
    }

    @Test
    void testOneThree() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate("  _ ", "| _|", "| _|"), "13");

        assertEquals(tester.translate("    _ ", "|   _|", "|   _|"), "13");
        
        assertEquals(tester.translate("   _    ", " | _|   ", " | _|   "), "13");
    }

    @Test
    void testThreeOne() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate("_   ", "_| |", "_| |"), "31");
    }

    @Test
    void testThreeOneCrowded() { 
        OCRTranslator tester = new OCRTranslator();

        assertThrows(OCRException.class, () -> {
            tester.translate("_  ", "_||", "_||");
        });

        assertThrows(OCRException.class, () -> {
            tester.translate("  _  ", "  _||", "  _||");
        });

        assertThrows(OCRException.class, () -> {
            tester.translate("_    ", "_||  ", "_||  ");
        });
    }

    @Test
    void testZero() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate(" _ ", "| |", "|_|"), "0");
     }

    @Test
    void testTwo() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate(" _ ", " _|", "|_ "), "2");
    }

    @Test
    void testFour() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate("   ", "|_|", "  |"), "4");
    }

    @Test
    void testFive() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate(" _ ", "|_ ", " _|"), "5");
    }

    @Test
    void testSix() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate(" _ ", "|_ ", "|_|"), "6");
    }

    @Test
    void testSeven() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate("_ ", " |", " |"), "7");
    }

    @Test
    void testEight() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate(" _ ", "|_|", "|_|"), "8");
    }

    @Test
    void testNine() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate(" _ ", "|_|", "  |"), "9");
    }

    @Test
    void testAll() { 
        OCRTranslator tester = new OCRTranslator();

        assertEquals(tester.translate(
            " _     _  _       _   _  _   _   _ ",
            "| | |  _| _| |_| |_  |_   | |_| |_|",
            "|_| | |_  _|   |  _| |_|  | |_|   |"
        ), "0123456789");

        assertEquals(tester.translate(
            "    _      _    _              _      _    _    _   _  ",
            "   | | |   _|   _|    |_|     |_     |_     |  |_| |_| ",
            "   |_| |  |_    _|      |      _|    |_|    |  |_|   | "
        ), "0123456789");
    }

    @Test
    void testValidAndInvalid() { 
        OCRTranslator tester = new OCRTranslator();

        assertThrows(OCRException.class, () -> {
            tester.translate("_  |  ", "_| | |", "_| | |");
        });

        assertThrows(OCRException.class, () -> {
            tester.translate(
                "    _      _    _          _      _    _    _   _  ",
                "   | | |   _|   _||_|     |_     |_     |  |_| |_| ",
                "   |_| |  |_    _|  |      _|    |_|    |  |_|   | "
            );
        });

        assertThrows(OCRException.class, () -> {
            tester.translate(
                "    _      _    _           _      _    _    _   _  |",
                "   | | |   _|   _| |_|     |_     |_     |  |_| |_| |",
                "   |_| |  |_    _|   |      _|    |_|    |  |_|   | |"
            );
        });
    }
}
