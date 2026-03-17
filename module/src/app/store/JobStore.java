package app.store;

import app.model.Job;
import app.util.Csv;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobStore {
    private final File jobsCsv;

    public JobStore(File dataDir) {
        this.jobsCsv = new File(dataDir, "jobs.csv");
    }

    public List<Job> listAll() throws IOException {
        synchronized (FileLock.JOBS) {
            List<Job> out = new ArrayList<>();
            if (!jobsCsv.exists()) return out;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(jobsCsv), StandardCharsets.UTF_8))) {
                String line = br.readLine(); // header
                if (line == null) return out;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = Csv.parseLine(line);
                    if (p.length < 6) continue;
                    out.add(new Job(p[0], p[1], p[2], p[3], p[4], "OPEN".equalsIgnoreCase(p[5])));
                }
            }
            return out;
        }
    }

    public Optional<Job> findById(String jobId) throws IOException {
        for (Job j : listAll()) {
            if (j.getJobId().equals(jobId)) return Optional.of(j);
        }
        return Optional.empty();
    }

    public List<Job> listOpenJobs(String keyword) throws IOException {
        String k = keyword == null ? "" : keyword.trim().toLowerCase();
        List<Job> all = listAll();
        List<Job> out = new ArrayList<>();
        for (Job j : all) {
            if (!j.isOpen()) continue;
            if (k.isEmpty()) {
                out.add(j);
            } else {
                String blob = (j.getTitle() + " " + j.getDescription() + " " + j.getRequiredSkills()).toLowerCase();
                if (blob.contains(k)) out.add(j);
            }
        }
        return out;
    }

    public void save(Job job) throws IOException {
        synchronized (FileLock.JOBS) {
            List<Job> all = listAll();
            boolean replaced = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getJobId().equals(job.getJobId())) {
                    all.set(i, job);
                    replaced = true;
                    break;
                }
            }
            if (!replaced) all.add(job);

            jobsCsv.getParentFile().mkdirs();
            try (Writer w = new OutputStreamWriter(new FileOutputStream(jobsCsv), StandardCharsets.UTF_8)) {
                w.write("jobId,title,description,requiredSkills,moUsername,status\n");
                for (Job j : all) {
                    w.write(Csv.escape(j.getJobId()) + "," +
                            Csv.escape(j.getTitle()) + "," +
                            Csv.escape(j.getDescription()) + "," +
                            Csv.escape(j.getRequiredSkills()) + "," +
                            Csv.escape(j.getMoUsername()) + "," +
                            (j.isOpen() ? "OPEN" : "CLOSED") + "\n");
                }
            }
        }
    }
}

