package app.web.common;

import app.model.Role;
import app.model.TaProfile;
import app.service.SkillMatch;
import app.store.JobStore;
import app.web.Auth;
import app.web.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class JobsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = req.getParameter("q");
        JobStore jobStore = jobs(req);
        req.setAttribute("q", q == null ? "" : q);
        req.setAttribute("jobs", jobStore.listOpenJobs(q));

        Optional<Role> role = Auth.currentRole(req);
        if (role.isPresent() && role.get() == Role.TA) {
            Optional<String> u = Auth.currentUsername(req);
            if (u.isPresent()) {
                Optional<TaProfile> p = profiles(req).findByUsername(u.get());
                if (p.isPresent()) {
                    req.setAttribute("taSkills", p.get().getSkills());
                }
            }
        }
        render(req, resp, "/WEB-INF/jsp/jobs.jsp");
    }
}

