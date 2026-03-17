package app.util;

import java.util.ArrayList;
import java.util.List;

public final class Csv {
    private Csv() {}

    public static String[] parseLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else if (c == '"') {
                    inQuotes = true;
                } else {
                    cur.append(c);
                }
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }

    public static String escape(String value) {
        if (value == null) return "";
        boolean needQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String v = value.replace("\"", "\"\"");
        return needQuotes ? ("\"" + v + "\"") : v;
    }
}

