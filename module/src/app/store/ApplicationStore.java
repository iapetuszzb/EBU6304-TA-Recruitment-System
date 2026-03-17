package app.store;

import app.model.Application;
import app.model.ApplicationStatus;
import app.util.Csv;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApplicationStore {
    private final File applicationsCsv;

    public ApplicationStore(File dataDir) {
        this.applicationsCsv = new File(dataDir, "applications.csv");
    }

    public List<Application> listAll() throws IOException {
        synchronized (FileLock.APPLICATIONS) {
            List<Application> out = new ArrayList<>();
            if (!applicationsCsv.exists()) return out;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(applicationsCsv), StandardCharsets.UTF_8))) {
                String line = br.readLine(); // header
                if (line == null) return out;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = Csv.parseLine(line);
                    if (p.length < 5) continue;
                    out.add(new Application(p[0], p[1], p[2], ApplicationStatus.from(p[3]), p[4]));
                }
            }
            return out;
        }
    }

    public Optional<Application> findById(String applicationId) throws IOException {
        for (Application a : listAll()) {
            if (a.getApplicationId().equals(applicationId)) return Optional.of(a);
        }
        return Optional.empty();
    }

    public Optional<Application> findByJobAndTa(String jobId, String taUsername) throws IOException {
        for (Application a : listAll()) {
            if (a.getJobId().equals(jobId) && a.getTaUsername().equalsIgnoreCase(taUsername)) return Optional.of(a);
        }
        return Optional.empty();
    }

    public List<Application> listByTa(String taUsername) throws IOException {
        List<Application> out = new ArrayList<>();
        for (Application a : listAll()) {
            if (a.getTaUsername().equalsIgnoreCase(taUsername)) out.add(a);
        }
        return out;
    }

    public List<Application> listByJob(String jobId) throws IOException {
        List<Application> out = new ArrayList<>();
        for (Application a : listAll()) {
            if (a.getJobId().equals(jobId)) out.add(a);
        }
        return out;
    }

    public void save(Application app) throws IOException {
        synchronized (FileLock.APPLICATIONS) {
            List<Application> all = listAll();
            boolean replaced = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getApplicationId().equals(app.getApplicationId())) {
                    all.set(i, app);
                    replaced = true;
                    break;
                }
            }
            if (!replaced) all.add(app);

            applicationsCsv.getParentFile().mkdirs();
            try (Writer w = new OutputStreamWriter(new FileOutputStream(applicationsCsv), StandardCharsets.UTF_8)) {
                w.write("applicationId,jobId,taUsername,status,createdAtIso\n");
                for (Application a : all) {
                    w.write(Csv.escape(a.getApplicationId()) + "," +
                            Csv.escape(a.getJobId()) + "," +
                            Csv.escape(a.getTaUsername()) + "," +
                            Csv.escape(a.getStatus().name()) + "," +
                            Csv.escape(a.getCreatedAtIso()) + "\n");
                }
            }
        }
    }
}

