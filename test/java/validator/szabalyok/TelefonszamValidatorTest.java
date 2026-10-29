package validator.szabalyok;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TelefonszamValidatorTest {

    private final TelefonszamValidator validator = new TelefonszamValidator();

    @ParameterizedTest(name = "érvényes: {0}")
    @CsvSource({
            "0745123456",
            "0745 123 456",
            "0745-123-456",
            "+40745123456",
            "+40 745 123 456",
            "0723 000 999"
    })
    void ervenyesMobilszamok(String szam) {
        assertTrue(validator.ellenoriz(szam).ervenyes());
    }

    @ParameterizedTest(name = "érvénytelen: {0} ({1})")
    @CsvSource({
            "0265123456,      vezetékes szám (nem 07-tel kezdődik)",
            "074512345,       túl rövid",
            "07451234567,     túl hosszú",
            "+36 30 123 4567, magyar szám",
            "40745123456,     hiányzik a + jel",
            "0745 12a 456,    betű van benne",
            "+40 0745 123 456, a +40 után nem kell 0"
    })
    void ervenytelenMobilszamok(String szam, String indok) {
        assertFalse(validator.ellenoriz(szam).ervenyes(), "Hibásnak kellene lennie, mert: " + indok);
    }
}
