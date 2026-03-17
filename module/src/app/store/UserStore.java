package app.store;

import app.model.Role;
import app.model.User;
import app.service.PasswordHasher;
import app.util.Csv;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserStore {
    private final File usersCsv;

    public UserStore(File dataDir) {
        this.usersCsv = new File(dataDir, "users.csv");
    }

    public void ensureSeedUsers() throws IOException {
        synchronized (FileLock.USERS) {
            if (usersCsv.exists() && usersCsv.length() > 0) return;
            usersCsv.getParentFile().mkdirs();
            try (Writer w = new OutputStreamWriter(new FileOutputStream(usersCsv), StandardCharsets.UTF_8)) {
                w.write("username,password,role\n");
                // 仅预置管理员账号（TA/MO 走注册）
                w.write("admin," + Csv.escape(PasswordHasher.hash("admin")) + ",ADMIN\n");
            }
        }
    }

    public Optional<User> findByUsername(String username) throws IOException {
        for (User u : listAll()) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public List<User> listAll() throws IOException {
        synchronized (FileLock.USERS) {
            List<User> out = new ArrayList<>();
            if (!usersCsv.exists()) return out;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(usersCsv), StandardCharsets.UTF_8))) {
                String line = br.readLine(); // header
                if (line == null) return out;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = Csv.parseLine(line);
                    if (p.length < 3) continue;
                    out.add(new User(p[0], p[1], Role.from(p[2])));
                }
            }
            return out;
        }
    }

    public void save(User user) throws IOException {
        synchronized (FileLock.USERS) {
            List<User> all = listAll();
            boolean replaced = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getUsername().equalsIgnoreCase(user.getUsername())) {
                    all.set(i, user);
                    replaced = true;
                    break;
                }
            }
            if (!replaced) all.add(user);

            usersCsv.getParentFile().mkdirs();
            try (Writer w = new OutputStreamWriter(new FileOutputStream(usersCsv), StandardCharsets.UTF_8)) {
                w.write("username,password,role\n");
                for (User u : all) {
                    w.write(Csv.escape(u.getUsername()) + "," + Csv.escape(u.getPassword()) + "," + Csv.escape(u.getRole().name()) + "\n");
                }
            }
        }
    }
}

