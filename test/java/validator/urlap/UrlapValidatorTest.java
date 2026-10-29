package validator.urlap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import validator.Validator;
import validator.szabalyok.EmailValidator;
import validator.szabalyok.IranyitoszamValidator;
import validator.szabalyok.JelszoValidator;
import validator.szabalyok.RendszamValidator;
import validator.szabalyok.TelefonszamValidator;

class UrlapValidatorTest {

    private UrlapValidator regisztracio;

    @BeforeEach
    void setUp() {
        regisztracio = new UrlapValidator()
                .mezo("email", new EmailValidator())
                .mezo("telefon", new TelefonszamValidator())
                .mezo("jelszo", new JelszoValidator())
                .mezo("rendszam", new RendszamValidator())
                .mezo("iranyitoszam", new IranyitoszamValidator());
    }

    private Map<String, String> joUrlap() {
        Map<String, String> urlap = new HashMap<>();
        urlap.put("email", "lili.virag@example.com");
        urlap.put("telefon", "0745 123 456");
        urlap.put("jelszo", "Tulipan#2024");
        urlap.put("rendszam", "MS 12 LIL");
        urlap.put("iranyitoszam", "540139");
        return urlap;
    }

    @Test
    void joUrlapErvenyes() {
        UrlapEredmeny eredmeny = regisztracio.ellenoriz(joUrlap());
        assertTrue(eredmeny.ervenyes());
        assertTrue(eredmeny.osszesHiba().isEmpty());
    }

    @ParameterizedTest(name = "csak a(z) {0} mező hibás: {1}")
    @CsvSource({
            "email,        lili-kukac-example.com",
            "telefon,      123456",
            "jelszo,       tulipan",
            "rendszam,     XX 00 AAA",
            "iranyitoszam, 5401"
    })
    void csakAHibasMezoJelenikMeg(String mezo, String rosszErtek) {
        Map<String, String> urlap = joUrlap();
        urlap.put(mezo, rosszErtek);

        UrlapEredmeny eredmeny = regisztracio.ellenoriz(urlap);

        assertFalse(eredmeny.ervenyes());
        assertEquals(Set.of(mezo), eredmeny.mezoHibak().keySet());
    }

    @Test
    void nemAllMegAzElsoHibanal() {
        Map<String, String> urlap = Map.of(
                "email", "rossz",
                "telefon", "nem szám",
                "jelszo", "abc",
                "rendszam", "MS 12 ABC",
                "iranyitoszam", "12");

        UrlapEredmeny eredmeny = regisztracio.ellenoriz(urlap);

        // mind a 4 hibás mező benne van, a felvétel sorrendjében
        assertEquals(List.of("email", "telefon", "jelszo", "iranyitoszam"), List.copyOf(eredmeny.mezoHibak().keySet()));
        // a jelszónál is az összes hiányosság benne van, nem csak az első
        assertEquals(4, eredmeny.hibak("jelszo").size());
        assertEquals(7, eredmeny.osszesHiba().size());
        assertTrue(eredmeny.osszesHiba().get(0).startsWith("email: "));
    }

    @Test
    void hianyzoMezoKotelezo() {
        Map<String, String> urlap = joUrlap();
        urlap.remove("telefon");

        assertEquals(List.of("A mező kitöltése kötelező."), regisztracio.ellenoriz(urlap).hibak("telefon"));
    }

    @Test
    void ugyanarraAMezoreKetValidatorIsMehet() {
        UrlapValidator csakRomanEmail = new UrlapValidator()
                .mezo("email", new EmailValidator())
                .mezo("email", Validator.szabaly(s -> s != null && s.endsWith(".ro"), "Csak .ro-s cím fogadható el."));

        UrlapEredmeny eredmeny = csakRomanEmail.ellenoriz(Map.of("email", "lili.virag@example.com"));

        assertEquals(List.of("Csak .ro-s cím fogadható el."), eredmeny.hibak("email"));
    }
}
