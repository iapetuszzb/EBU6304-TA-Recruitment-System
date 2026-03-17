package app.store;

import app.model.TaProfile;
import app.util.Csv;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfileStore {
    private final File profilesCsv;

    public ProfileStore(File dataDir) {
        this.profilesCsv = new File(dataDir, "profiles.csv");
    }

    public Optional<TaProfile> findByUsername(String username) throws IOException {
        for (TaProfile p : listAll()) {
            if (p.getUsername().equalsIgnoreCase(username)) return Optional.of(p);
        }
        return Optional.empty();
    }

    public List<TaProfile> listAll() throws IOException {
        synchronized (FileLock.PROFILES) {
            List<TaProfile> out = new ArrayList<>();
            if (!profilesCsv.exists()) return out;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(profilesCsv), StandardCharsets.UTF_8))) {
                String line = br.readLine(); // header
                if (line == null) return out;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = Csv.parseLine(line);
                    if (p.length < 5) continue;
                    out.add(new TaProfile(p[0], p[1], p[2], p[3], p[4]));
                }
            }
            return out;
        }
    }

    public void save(TaProfile profile) throws IOException {
        synchronized (FileLock.PROFILES) {
            List<TaProfile> all = listAll();
            boolean replaced = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getUsername().equalsIgnoreCase(profile.getUsername())) {
                    all.set(i, profile);
                    replaced = true;
                    break;
                }
            }
            if (!replaced) all.add(profile);

            profilesCsv.getParentFile().mkdirs();
            try (Writer w = new OutputStreamWriter(new FileOutputStream(profilesCsv), StandardCharsets.UTF_8)) {
                w.write("username,fullName,email,skills,resumePath\n");
                for (TaProfile p : all) {
                    w.write(Csv.escape(p.getUsername()) + "," +
                            Csv.escape(p.getFullName()) + "," +
                            Csv.escape(p.getEmail()) + "," +
                            Csv.escape(p.getSkills()) + "," +
                            Csv.escape(p.getResumePath()) + "\n");
                }
            }
        }
    }
}

