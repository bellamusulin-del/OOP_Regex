package validator.szabalyok;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class IranyitoszamValidatorTest {

    private final IranyitoszamValidator validator = new IranyitoszamValidator();

    @ParameterizedTest(name = "érvényes: {0}")
    @CsvSource({
            "540139",
            "400001",
            "010011"
    })
    void ervenyesIranyitoszamok(String iranyitoszam) {
        assertTrue(validator.ellenoriz(iranyitoszam).ervenyes());
    }

    @ParameterizedTest(name = "érvénytelen: {0} ({1})")
    @CsvSource({
            "54013,   csak 5 számjegy",
            "5401390, 7 számjegy",
            "54O139,  O betű van benne nulla helyett",
            "540 139, szóköz van benne",
            "abcdef,  csak betűk"
    })
    void ervenytelenIranyitoszamok(String iranyitoszam, String indok) {
        assertFalse(validator.ellenoriz(iranyitoszam).ervenyes(), "Hibásnak kellene lennie, mert: " + indok);
    }
}
