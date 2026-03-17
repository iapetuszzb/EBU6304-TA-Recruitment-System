package app.web.admin;

import app.model.Application;
import app.model.ApplicationStatus;
import app.model.Role;
import app.model.User;
import app.web.Auth;
import app.web.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class AdminWorkloadServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Auth.hasRole(req, Role.ADMIN)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Map<String, int[]> stats = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (User u : users(req).listAll()) {
            if (u.getRole() == Role.TA) stats.put(u.getUsername(), new int[]{0, 0});
        }

        for (Application a : apps(req).listAll()) {
            int[] s = stats.computeIfAbsent(a.getTaUsername(), k -> new int[]{0, 0});
            s[0] += 1; // total apps
            if (a.getStatus() == ApplicationStatus.SHORTLISTED) s[1] += 1; // shortlisted
        }

        req.setAttribute("stats", stats);
        render(req, resp, "/WEB-INF/jsp/admin_workload.jsp");
    }
}

