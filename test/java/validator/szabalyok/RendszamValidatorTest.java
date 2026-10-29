package validator.szabalyok;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RendszamValidatorTest {

    private final RendszamValidator validator = new RendszamValidator();

    @ParameterizedTest(name = "érvényes: {0}")
    @CsvSource({
            "MS 12 ABC",
            "B 123 ABC",
            "B 12 XYZ",
            "CJ 99 ZZZ",
            "HR 05 LIL",
            "MS12ABC"
    })
    void ervenyesRendszamok(String rendszam) {
        assertTrue(validator.ellenoriz(rendszam).ervenyes());
    }

    @ParameterizedTest(name = "érvénytelen: {0} ({1})")
    @CsvSource({
            "XY 12 ABC,  nem létező megyekód",
            "MS 123 ABC, megyében csak 2 számjegy lehet",
            "B 1234 ABC, Bukarestben is max. 3 számjegy",
            "ms 12 abc,  kisbetűs",
            "MS 12 AB,   csak 2 betű a végén",
            "MS 1 ABC,   csak 1 számjegy",
            "MS-12-ABC,  kötőjel nem lehet benne"
    })
    void ervenytelenRendszamok(String rendszam, String indok) {
        assertFalse(validator.ellenoriz(rendszam).ervenyes(), "Hibásnak kellene lennie, mert: " + indok);
    }
}
