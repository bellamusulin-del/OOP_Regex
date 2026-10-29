package validator.urlap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import validator.Eredmeny;
import validator.Validator;

public class UrlapValidator {

    private final Map<String, Validator<String>> mezok =
            new LinkedHashMap<>();

    // Adds a validator to a field. Multiple validators are combined with AND.
    public UrlapValidator mezo(
            String mezonev,
            Validator<String> validator) {

        Objects.requireNonNull(mezonev, "The field name cannot be null.");
        Objects.requireNonNull(validator, "The validator cannot be null.");

        mezok.merge(mezonev, validator, Validator::es);

        return this;
    }

    // Checks every configured field and collects all errors.
    public UrlapEredmeny ellenoriz(Map<String, String> urlap) {
        Objects.requireNonNull(urlap, "The form cannot be null.");

        Map<String, List<String>> hibak = new LinkedHashMap<>();

        for (Map.Entry<String, Validator<String>> bejegyzes
                : mezok.entrySet()) {

            String mezonev = bejegyzes.getKey();
            Eredmeny eredmeny =
                    bejegyzes.getValue().ellenoriz(urlap.get(mezonev));

            if (!eredmeny.ervenyes()) {
                hibak.put(mezonev, eredmeny.hibak());
            }
        }

        return new UrlapEredmeny(hibak);
    }
}