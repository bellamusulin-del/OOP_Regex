package validator.szabalyok;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import validator.Eredmeny;

class JelszoValidatorTest {

    private final JelszoValidator validator = new JelszoValidator();

    @ParameterizedTest(name = "erős jelszó: {0}")
    @CsvSource({
            "Tulipan#2024",
            "Pitypang_77",
            "Erős!Jelszó9",
            "Ab1!efgh",
            "Nap$ugar 2024"
    })
    void erosJelszavak(String jelszo) {
        Eredmeny eredmeny = validator.ellenoriz(jelszo);
        assertTrue(eredmeny.ervenyes(), () -> "Hibák: " + eredmeny.hibak());
    }

    @ParameterizedTest(name = "{0} -> hiányzik: {1}")
    @CsvSource({
            "tulipan#2024, nagybetű",
            "TULIPAN#2024, kisbetű",
            "Tulipan#abcd, szám",
            "Tulipan2024,  speciális karakter",
            "Tu#1,         rövid"
    })
    void megmondjaMiHianyzik(String jelszo, String hianyzo) {
        Eredmeny eredmeny = validator.ellenoriz(jelszo);
        assertFalse(eredmeny.ervenyes());
        assertTrue(eredmeny.hibak().stream().anyMatch(hiba -> hiba.contains(hianyzo)),
                () -> "Ezt kellett volna jeleznie: " + hianyzo + ", de ezek jöttek: " + eredmeny.hibak());
    }

    @ParameterizedTest(name = "\"{0}\" -> {1} hiba")
    @CsvSource({
            "abc,        4",  // rövid + nincs nagybetű, szám, speciális
            "abcdefgh,   3",  // nincs nagybetű, szám, speciális
            "Abcdefgh,   2",  // nincs szám, speciális
            "Abcdefg1,   1",  // nincs speciális
            "'        ', 4"   // 8 szóköz: a hossza jó, de minden más hiányzik
    })
    void mindenHianyossagotFelsorol(String jelszo, int hibakSzama) {
        assertEquals(hibakSzama, validator.ellenoriz(jelszo).hibak().size());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void uresJelszo(String jelszo) {
        assertEquals(List.of("Add meg a jelszót."), validator.ellenoriz(jelszo).hibak());
    }
}
