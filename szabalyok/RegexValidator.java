package validator.szabalyok;

import java.util.Objects;
import java.util.regex.Pattern;

import validator.Eredmeny;
import validator.Validator;

public abstract class RegexValidator implements Validator<String> {

    private final Pattern minta;
    private final String hibauzenet;

    protected RegexValidator(Pattern minta, String hibauzenet) {
        this.minta = Objects.requireNonNull(minta);
        this.hibauzenet = Objects.requireNonNull(hibauzenet);
    }

    @Override
    public Eredmeny ellenoriz(String ertek) {
        if (ertek == null || ertek.isBlank()) {
            return Eredmeny.hiba("This field is required.");
        }

        // matches() checks the entire input, not just a substring.
        if (minta.matcher(ertek.strip()).matches()) {
            return Eredmeny.ok();
        }

        return Eredmeny.hiba(hibauzenet);
    }
}
