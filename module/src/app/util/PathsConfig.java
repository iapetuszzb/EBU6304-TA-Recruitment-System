package app.util;

import java.io.File;

public final class PathsConfig {
    private PathsConfig() {}

    public static File dataDir(File webAppRoot) {
        // Use a stable location inside the deployed webapp.
        File data = new File(webAppRoot, "WEB-INF" + File.separator + "data");
        if (!data.exists()) data.mkdirs();
        return data;
    }

    public static File resumesDir(File dataDir) {
        File d = new File(dataDir, "resumes");
        if (!d.exists()) d.mkdirs();
        return d;
    }
}

