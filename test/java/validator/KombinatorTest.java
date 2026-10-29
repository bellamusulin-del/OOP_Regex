package validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import validator.szabalyok.EmailValidator;
import validator.szabalyok.IranyitoszamValidator;
import validator.szabalyok.TelefonszamValidator;

class KombinatorTest {

    private static final Validator<String> RO_VEGZODES =
            Validator.szabaly(s -> s != null && s.endsWith(".ro"), "Csak .ro végű cím fogadható el.");

    @ParameterizedTest(name = "{0} -> érvényes: {1}, hibák száma: {2}")
    @CsvSource({
            "lili@egyetem.ro,  true,  0",
            "lili@example.com, false, 1",
            "lili-kukac,       false, 2"
    })
    void esMindkettotLefuttatja(String email, boolean ervenyes, int hibakSzama) {
        Eredmeny eredmeny = new EmailValidator().es(RO_VEGZODES).ellenoriz(email);
        assertEquals(ervenyes, eredmeny.ervenyes());
        assertEquals(hibakSzama, eredmeny.hibak().size());
    }

    @ParameterizedTest(name = "kapcsolat: {0} -> {1}")
    @CsvSource({
            "lili.virag@example.com, true",
            "0745 123 456,           true",
            "+40 745 123 456,        true",
            "valami,                 false",
            "0745 12,                false"
    })
    void vagyElegHaAzEgyikJo(String kapcsolat, boolean vart) {
        Validator<String> emailVagyTelefon = new EmailValidator().vagy(new TelefonszamValidator());
        assertEquals(vart, emailVagyTelefon.ellenoriz(kapcsolat).ervenyes());
    }

    @Test
    void vagyHaEgyikSemJoMindketHibatVisszaadja() {
        Eredmeny eredmeny = new EmailValidator().vagy(new TelefonszamValidator()).ellenoriz("valami");
        assertEquals(2, eredmeny.hibak().size());
    }

    @ParameterizedTest(name = "rossz irányítószám: {0}")
    @CsvSource({"12", "abcdef", "5401390"})
    void uzenettelLecsereliAHibauzenetet(String rossz) {
        Validator<String> validator = new IranyitoszamValidator().uzenettel("Kérlek, 6 jegyű irányítószámot adj meg!");
        assertEquals(List.of("Kérlek, 6 jegyű irányítószámot adj meg!"), validator.ellenoriz(rossz).hibak());
    }

    @Test
    void uzenettelJoErtekenNemValtoztat() {
        Validator<String> validator = new IranyitoszamValidator().uzenettel("Kérlek, 6 jegyű irányítószámot adj meg!");
        assertTrue(validator.ellenoriz("540139").ervenyes());
    }

    @Test
    void lambdaIsLehetValidator() {
        Validator<String> rovidNev = s -> s.length() <= 10 ? Eredmeny.ok() : Eredmeny.hiba("Túl hosszú név.");
        assertTrue(rovidNev.ellenoriz("Lili").ervenyes());
        assertEquals(List.of("Túl hosszú név."), rovidNev.ellenoriz("Kiss-Nagy Boglárka").hibak());
    }

    @Test
    void kombinatorokLancolhatok() {
        // (e-mail VAGY telefon) ÉS legfeljebb 30 karakter
        Validator<String> kapcsolat = new EmailValidator().vagy(new TelefonszamValidator())
                .es(Validator.szabaly(s -> s.length() <= 30, "Legfeljebb 30 karakter lehet."));
        assertTrue(kapcsolat.ellenoriz("0745 123 456").ervenyes());
        assertEquals(List.of("Legfeljebb 30 karakter lehet."),
                kapcsolat.ellenoriz("nagyon.hosszu.email.cim@example.com").hibak());
    }
}
