package validator.szabalyok;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import validator.Eredmeny;
import validator.Validator;

/**
 * Erős jelszó: legalább 8 karakter, van benne kisbetű, nagybetű, szám és speciális karakter.
 * Ha valami nem stimmel, felsorolja, hogy pontosan MI hiányzik (nem csak annyit, hogy "rossz jelszó").
 */
public class JelszoValidator implements Validator<String> {

    public static final int MIN_HOSSZ = 8;

    // Az egész szabály egyben: 4 előretekintés (lookahead) + legalább 8 karakter.
    // DOTALL: a "." a sortörésre is illeszkedjen, így tényleg minden karaktert megszámol.
    private static final Pattern EROS_JELSZO = Pattern.compile(
            "(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\d)(?=.*[^\\p{L}\\d\\s]).{" + MIN_HOSSZ + ",}", Pattern.DOTALL);

    // A részszabályok külön is, hogy meg tudjuk mondani, mi hiányzik
    private static final Pattern KISBETU = Pattern.compile("\\p{Ll}");
    private static final Pattern NAGYBETU = Pattern.compile("\\p{Lu}");
    private static final Pattern SZAMJEGY = Pattern.compile("\\d");
    private static final Pattern SPECIALIS = Pattern.compile("[^\\p{L}\\d\\s]");

    @Override
    public Eredmeny ellenoriz(String jelszo) {
        if (jelszo == null || jelszo.isEmpty()) {
            return Eredmeny.hiba("Add meg a jelszót.");
        }
        if (EROS_JELSZO.matcher(jelszo).matches()) {
            return Eredmeny.ok();
        }

        List<String> hianyzik = new ArrayList<>();
        // karakterek száma úgy, ahogy a regex "." is számol (egy emoji is 1 karakter)
        int hossz = jelszo.codePointCount(0, jelszo.length());
        if (hossz < MIN_HOSSZ) {
            hianyzik.add("Túl rövid: legalább " + MIN_HOSSZ + " karakter kell (most " + hossz + ").");
        }
        // Itt find() kell, nem matches(): azt nézzük, hogy VALAHOL van-e benne ilyen karakter
        if (!KISBETU.matcher(jelszo).find()) {
            hianyzik.add("Hiányzik belőle kisbetű.");
        }
        if (!NAGYBETU.matcher(jelszo).find()) {
            hianyzik.add("Hiányzik belőle nagybetű.");
        }
        if (!SZAMJEGY.matcher(jelszo).find()) {
            hianyzik.add("Hiányzik belőle szám.");
        }
        if (!SPECIALIS.matcher(jelszo).find()) {
            hianyzik.add("Hiányzik belőle speciális karakter (pl. ! ? # @ _).");
        }
        return new Eredmeny(false, hianyzik);
    }
}
