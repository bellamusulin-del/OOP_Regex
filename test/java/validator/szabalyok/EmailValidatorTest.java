package validator.szabalyok;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import validator.Eredmeny;

class EmailValidatorTest {

    private final EmailValidator validator = new EmailValidator();

    @ParameterizedTest(name = "érvényes: {0}")
    @CsvSource({
            "lili@example.com",
            "lili.virag@gmail.com",
            "kovacs_reka+hirlevel@egyetem.ro",
            "anna.maria.szabo@diak.pelda-egyetem.ro",
            "'  bogi@example.hu  '"  // a szélén lévő szóközt a validátor levágja
    })
    void ervenyesEmailCimek(String email) {
        assertTrue(validator.ellenoriz(email).ervenyes());
    }

    @ParameterizedTest(name = "érvénytelen: {0} ({1})")
    @CsvSource({
            "lilivirag.example.com,  nincs benne @",
            "lili@,                  nincs domain",
            "@example.com,           nincs felhasználónév",
            "lili@example,           nincs végződés (.com / .ro)",
            "lili..virag@example.com, két pont egymás után",
            "lili@example.c,         túl rövid végződés",
            "lili virag@example.com, szóköz van benne",
            "lili@-example.com,      kötőjellel kezdődő domain"
    })
    void ervenytelenEmailCimek(String email, String indok) {
        Eredmeny eredmeny = validator.ellenoriz(email);
        assertFalse(eredmeny.ervenyes(), "Hibásnak kellene lennie, mert: " + indok);
        assertEquals(List.of("Hibás e-mail cím (helyes példa: lili.virag@example.com)."), eredmeny.hibak());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void uresMezoKotelezo(String email) {
        assertEquals(List.of("A mező kitöltése kötelező."), validator.ellenoriz(email).hibak());
    }
}
