package validator.szabalyok;

import java.util.regex.Pattern;

public class RendszamValidator extends RegexValidator {

    private static final String MEGYEK =
            "AB|AG|AR|BC|BH|BN|BR|BT|BV|BZ|CJ|CL|CS|CT|CV|DB|DJ|GJ|GL|GR|HD"
            + "|HR|IF|IL|IS|MH|MM|MS|NT|OT|PH|SB|SJ|SM|SV|TL|TM|TR|VL|VN|VS";

    private static final Pattern RENDSZAM = Pattern.compile(
            "(B ?\\d{2,3}|(" + MEGYEK + ") ?\\d{2}) ?[A-Z]{3}");

    public RendszamValidator() {
        super(RENDSZAM,
                "Invalid registration plate (example: MS 12 ABC or B 123 ABC).");
    }
}
