package validator.szabalyok;

import java.util.regex.Pattern;

public class EmailValidator extends RegexValidator {

    private static final Pattern EMAIL = Pattern.compile(
            "[A-Za-z0-9_%+-]+(\\.[A-Za-z0-9_%+-]+)*"
            + "@([A-Za-z0-9]+(-[A-Za-z0-9]+)*\\.)+[A-Za-z]{2,}");

    public EmailValidator() {
        super(EMAIL, "Invalid email address (example: lili.virag@example.com).");
    }
}