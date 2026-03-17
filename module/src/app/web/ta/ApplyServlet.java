package app.web.ta;

import app.model.*;
import app.service.Ids;
import app.service.SkillMatch;
import app.store.ApplicationStore;
import app.store.JobStore;
import app.store.ProfileStore;
import app.web.Auth;
import app.web.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

public class ApplyServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Auth.hasRole(req, Role.TA)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String jobId = req.getParameter("jobId");
        if (jobId == null || jobId.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/jobs");
            return;
        }
        JobStore js = jobs(req);
        Optional<Job> job = js.findById(jobId);
        if (!job.isPresent() || !job.get().isOpen()) {
            req.setAttribute("error", "岗位不存在或已关闭");
            render(req, resp, "/WEB-INF/jsp/ta_apply.jsp");
            return;
        }

        String username = Auth.currentUsername(req).orElseThrow(IllegalStateException::new);
        ProfileStore ps = profiles(req);
        Optional<TaProfile> profile = ps.findByUsername(username);
        req.setAttribute("job", job.get());
        req.setAttribute("profile", profile.orElse(null));

        if (profile.isPresent()) {
            req.setAttribute("matchScore", SkillMatch.matchScore(job.get().getRequiredSkills(), profile.get().getSkills()));
            req.setAttribute("missingSkills", SkillMatch.missingSkills(job.get().getRequiredSkills(), profile.get().getSkills()));
        }

        Optional<Application> existing = apps(req).findByJobAndTa(jobId, username);
        req.setAttribute("existing", existing.orElse(null));
        render(req, resp, "/WEB-INF/jsp/ta_apply.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Auth.hasRole(req, Role.TA)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setCharacterEncoding("UTF-8");
        String jobId = req.getParameter("jobId");
        if (jobId == null || jobId.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/jobs");
            return;
        }

        String username = Auth.currentUsername(req).orElseThrow(IllegalStateException::new);
        Optional<TaProfile> profile = profiles(req).findByUsername(username);
        if (!profile.isPresent() || profile.get().getResumePath() == null || profile.get().getResumePath().trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/ta/profile");
            return;
        }

        Optional<Job> job = jobs(req).findById(jobId);
        if (!job.isPresent() || !job.get().isOpen()) {
            resp.sendRedirect(req.getContextPath() + "/jobs");
            return;
        }

        ApplicationStore as = apps(req);
        Optional<Application> existing = as.findByJobAndTa(jobId, username);
        if (existing.isPresent()) {
            resp.sendRedirect(req.getContextPath() + "/ta/applications");
            return;
        }

        Application app = new Application(
                Ids.newId("app"),
                jobId,
                username,
                ApplicationStatus.SUBMITTED,
                Instant.now().toString()
        );
        as.save(app);
        resp.sendRedirect(req.getContextPath() + "/ta/applications");
    }
}

