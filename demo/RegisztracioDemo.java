package validator.demo;

import java.util.HashMap;
import java.util.Map;

import validator.Validator;
import validator.szabalyok.EmailValidator;
import validator.szabalyok.IranyitoszamValidator;
import validator.szabalyok.JelszoValidator;
import validator.szabalyok.RendszamValidator;
import validator.szabalyok.TelefonszamValidator;
import validator.urlap.UrlapEredmeny;
import validator.urlap.UrlapValidator;

public class RegisztracioDemo {

    public static void main(String[] args) {

        // Custom rules created with lambdas.
        Validator<String> nevKotelezo = Validator.szabaly(
                s -> s != null && !s.isBlank(),
                "Please enter your name.");

        Validator<String> nevMaxHossz = Validator.szabaly(
                s -> s == null || s.length() <= 50,
                "The name must not exceed 50 characters.");

        Validator<String> uresHagyhato = Validator.szabaly(
                s -> s == null || s.isBlank(),
                "The field must be empty.");

        UrlapValidator regisztracio = new UrlapValidator()
                .mezo("name", nevKotelezo.es(nevMaxHossz))
                .mezo("email", new EmailValidator())
                .mezo("phone", new TelefonszamValidator())
                .mezo("password", new JelszoValidator())
                .mezo("plate", uresHagyhato.vagy(new RendszamValidator())
                        .uzenettel(
                                "Invalid plate (example: MS 12 ABC), "
                                + "or leave it empty if you do not have a car."))
                .mezo("postalCode", new IranyitoszamValidator());

        Map<String, String> hibasUrlap = new HashMap<>();
        hibasUrlap.put("name", "Lili");
        hibasUrlap.put("email", "lili.virag@example");
        hibasUrlap.put("phone", "12345");
        hibasUrlap.put("password", "tulipan");
        hibasUrlap.put("plate", "MS 1 LIL");
        hibasUrlap.put("postalCode", "5401");

        Map<String, String> joUrlap = new HashMap<>();
        joUrlap.put("name", "Lili");
        joUrlap.put("email", "lili.virag@example.com");
        joUrlap.put("phone", "+40 745 123 456");
        joUrlap.put("password", "Tulipan#2024");
        joUrlap.put("plate", "");
        joUrlap.put("postalCode", "540139");

        kiir("First attempt", regisztracio.ellenoriz(hibasUrlap));
        kiir("Second attempt", regisztracio.ellenoriz(joUrlap));
    }

    private static void kiir(String cim, UrlapEredmeny eredmeny) {
        System.out.println("=== " + cim + " ===");

        if (eredmeny.ervenyes()) {
            System.out.println("All fields are valid. Registration successful!");
        } else {
            System.out.println(
                    "Errors found in " + eredmeny.mezoHibak().size() + " field(s):");

            eredmeny.osszesHiba().forEach(
                    hiba -> System.out.println("  - " + hiba));
        }

        System.out.println();
    }
}