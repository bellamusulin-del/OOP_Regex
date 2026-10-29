package validator.szabalyok;

import java.util.regex.Pattern;

public class TelefonszamValidator extends RegexValidator {

    private static final Pattern ROMAN_MOBIL = Pattern.compile(
            "(\\+40 ?|0)7\\d{2}[ -]?\\d{3}[ -]?\\d{3}");

    public TelefonszamValidator() {
        super(ROMAN_MOBIL,
                "Invalid Romanian mobile number (example: 0745 123 456 or +40 745 123 456).");
    }
}