package app.web;

import app.model.Role;
import app.model.User;
import app.service.PasswordHasher;
import app.store.UserStore;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public final class Auth {
    private Auth() {}

    public static final String SESSION_USER = "LOGIN_USER";
    public static final String SESSION_ROLE = "LOGIN_ROLE";

    public static Optional<String> currentUsername(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return Optional.empty();
        Object u = s.getAttribute(SESSION_USER);
        return u == null ? Optional.empty() : Optional.of(u.toString());
    }

    public static Optional<Role> currentRole(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return Optional.empty();
        Object r = s.getAttribute(SESSION_ROLE);
        if (r == null) return Optional.empty();
        try {
            return Optional.of(Role.from(r.toString()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static boolean hasRole(HttpServletRequest req, Role role) {
        return currentRole(req).map(r -> r == role).orElse(false);
    }

    public static boolean login(HttpServletRequest req, UserStore userStore, String username, String password) throws IOException {
        Optional<User> u = userStore.findByUsername(username);
        if (!u.isPresent()) return false;
        String stored = u.get().getPassword();
        boolean ok;
        if (stored != null && stored.startsWith("sha256$")) {
            ok = PasswordHasher.verify(password, stored);
        } else {
            // 兼容旧数据：明文匹配（建议用户重新注册/迁移）
            ok = stored != null && stored.equals(password);
        }
        if (!ok) return false;
        HttpSession s = req.getSession(true);
        s.setAttribute(SESSION_USER, u.get().getUsername());
        s.setAttribute(SESSION_ROLE, u.get().getRole().name());
        return true;
    }

    public static void logout(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null) s.invalidate();
    }
}

