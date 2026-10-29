package validator.urlap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record UrlapEredmeny(Map<String, List<String>> mezoHibak) {

    public UrlapEredmeny {
        Map<String, List<String>> masolat = new LinkedHashMap<>();

        mezoHibak.forEach((mezo, hibak) ->
                masolat.put(mezo, List.copyOf(hibak)));

        mezoHibak = Collections.unmodifiableMap(masolat);
    }

    public boolean ervenyes() {
        return mezoHibak.isEmpty();
    }

    public List<String> hibak(String mezonev) {
        return mezoHibak.getOrDefault(mezonev, List.of());
    }

    public List<String> osszesHiba() {
        List<String> lista = new ArrayList<>();

        mezoHibak.forEach((mezo, hibak) ->
                hibak.forEach(hiba ->
                        lista.add(mezo + ": " + hiba)));

        return lista;
    }
}