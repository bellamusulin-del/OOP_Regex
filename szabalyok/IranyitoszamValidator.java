package validator.szabalyok;

import java.util.regex.Pattern;

/** Román irányítószám: pontosan 6 számjegy, pl. {@code 540139} (Marosvásárhely). */
public class IranyitoszamValidator extends RegexValidator {

    private static final Pattern IRANYITOSZAM = Pattern.compile("\\d{6}");

    public IranyitoszamValidator() {
        super(IRANYITOSZAM, "Az irányítószám pontosan 6 számjegyből áll (példa: 540139).");
    }
}
