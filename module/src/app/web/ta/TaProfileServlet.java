package app.web.ta;

import app.model.Role;
import app.model.TaProfile;
import app.store.ProfileStore;
import app.util.PathsConfig;
import app.web.Auth;
import app.web.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@MultipartConfig
public class TaProfileServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Auth.hasRole(req, Role.TA)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String username = Auth.currentUsername(req).orElseThrow(IllegalStateException::new);
        Optional<TaProfile> existing = profiles(req).findByUsername(username);
        req.setAttribute("profile", existing.orElse(null));
        render(req, resp, "/WEB-INF/jsp/ta_profile.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Auth.hasRole(req, Role.TA)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setCharacterEncoding("UTF-8");
        String username = Auth.currentUsername(req).orElseThrow(IllegalStateException::new);

        String fullName = safe(req.getParameter("fullName"));
        String email = safe(req.getParameter("email"));
        String skills = safe(req.getParameter("skills"));

        ProfileStore ps = profiles(req);
        TaProfile p = ps.findByUsername(username).orElse(new TaProfile(username, "", "", "", ""));
        p.setFullName(fullName);
        p.setEmail(email);
        p.setSkills(skills);

        Part resume = null;
        try {
            resume = req.getPart("resume");
        } catch (Exception ignored) {
            // if not multipart, ignore
        }

        if (resume != null && resume.getSize() > 0) {
            File resumesDir = PathsConfig.resumesDir(dataDir());
            String submitted = resume.getSubmittedFileName();
            String ext = "";
            if (submitted != null) {
                int idx = submitted.lastIndexOf('.');
                if (idx >= 0 && idx < submitted.length() - 1) ext = submitted.substring(idx);
            }
            File out = new File(resumesDir, username + ext);
            try (InputStream in = resume.getInputStream(); FileOutputStream fos = new FileOutputStream(out)) {
                byte[] buf = new byte[8192];
                int n;
                while ((n = in.read(buf)) >= 0) {
                    fos.write(buf, 0, n);
                }
            }
            p.setResumePath("resumes/" + out.getName());
        }

        ps.save(p);
        resp.sendRedirect(req.getContextPath() + "/ta/profile");
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}

