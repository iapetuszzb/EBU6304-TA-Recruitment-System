package app.web.mo;

import app.model.Job;
import app.model.Role;
import app.service.Ids;
import app.web.Auth;
import app.web.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoPostJobServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Auth.hasRole(req, Role.MO)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String mo = Auth.currentUsername(req).orElseThrow(IllegalStateException::new);
        List<Job> mine = new ArrayList<>();
        for (Job j : jobs(req).listAll()) {
            if (j.getMoUsername().equalsIgnoreCase(mo)) mine.add(j);
        }
        req.setAttribute("myJobs", mine);
        render(req, resp, "/WEB-INF/jsp/mo_post_job.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Auth.hasRole(req, Role.MO)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setCharacterEncoding("UTF-8");
        String mo = Auth.currentUsername(req).orElseThrow(IllegalStateException::new);

        String title = safe(req.getParameter("title"));
        String description = safe(req.getParameter("description"));
        String requiredSkills = safe(req.getParameter("requiredSkills"));

        if (title.isEmpty() || description.isEmpty()) {
            req.setAttribute("error", "标题和描述不能为空");
            doGet(req, resp);
            return;
        }

        Job job = new Job(
                Ids.newId("job"),
                title,
                description,
                requiredSkills,
                mo,
                true
        );
        jobs(req).save(job);
        resp.sendRedirect(req.getContextPath() + "/mo/post-job");
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}

