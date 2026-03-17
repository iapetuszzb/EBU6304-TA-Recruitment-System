package app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SkillMatch {
    private SkillMatch() {}

    public static Set<String> normalizeToSet(String csv) {
        Set<String> out = new HashSet<>();
        if (csv == null) return out;
        for (String s : csv.split(",")) {
            String t = s.trim().toLowerCase();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    public static List<String> missingSkills(String requiredCsv, String candidateCsv) {
        Set<String> required = normalizeToSet(requiredCsv);
        Set<String> cand = normalizeToSet(candidateCsv);
        List<String> missing = new ArrayList<>();
        for (String r : required) {
            if (!cand.contains(r)) missing.add(r);
        }
        missing.sort(String::compareTo);
        return missing;
    }

    public static int matchScore(String requiredCsv, String candidateCsv) {
        Set<String> required = normalizeToSet(requiredCsv);
        if (required.isEmpty()) return 0;
        Set<String> cand = normalizeToSet(candidateCsv);
        int hit = 0;
        for (String r : required) if (cand.contains(r)) hit++;
        return (int) Math.round(100.0 * hit / required.size());
    }
}

