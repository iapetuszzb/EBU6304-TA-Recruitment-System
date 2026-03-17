package app.web.ta;

import app.model.Application;
import app.model.Job;
import app.model.Role;
import app.web.Auth;
import app.web.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplicationsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Auth.hasRole(req, Role.TA)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String username = Auth.currentUsername(req).orElseThrow(IllegalStateException::new);
        List<Application> apps = apps(req).listByTa(username);
        Map<String, Job> jobMap = new HashMap<>();
        for (Job j : jobs(req).listAll()) {
            jobMap.put(j.getJobId(), j);
        }
        req.setAttribute("applications", apps);
        req.setAttribute("jobMap", jobMap);
        render(req, resp, "/WEB-INF/jsp/ta_applications.jsp");
    }
}

