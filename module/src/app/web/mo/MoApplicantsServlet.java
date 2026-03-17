package app.web.mo;

import app.model.*;
import app.service.SkillMatch;
import app.store.ApplicationStore;
import app.web.Auth;
import app.web.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class MoApplicantsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Auth.hasRole(req, Role.MO)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String mo = Auth.currentUsername(req).orElseThrow(IllegalStateException::new);
        List<Job> myJobs = new ArrayList<>();
        for (Job j : jobs(req).listAll()) {
            if (j.getMoUsername().equalsIgnoreCase(mo)) myJobs.add(j);
        }
        req.setAttribute("myJobs", myJobs);

        String jobId = req.getParameter("jobId");
        if (jobId != null && !jobId.trim().isEmpty()) {
            Optional<Job> job = jobs(req).findById(jobId);
            if (job.isPresent() && job.get().getMoUsername().equalsIgnoreCase(mo)) {
                req.setAttribute("job", job.get());
                List<Application> apps = apps(req).listByJob(jobId);
                req.setAttribute("applications", apps);

                Map<String, TaProfile> profileMap = new HashMap<>();
                for (TaProfile p : profiles(req).listAll()) profileMap.put(p.getUsername().toLowerCase(), p);
                req.setAttribute("profileMap", profileMap);
            } else {
                req.setAttribute("error", "你没有权限查看该岗位，或岗位不存在");
            }
        }
        render(req, resp, "/WEB-INF/jsp/mo_applicants.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Auth.hasRole(req, Role.MO)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setCharacterEncoding("UTF-8");
        String mo = Auth.currentUsername(req).orElseThrow(IllegalStateException::new);
        String applicationId = req.getParameter("applicationId");
        String action = req.getParameter("action");
        String jobId = req.getParameter("jobId");

        if (applicationId == null || action == null || jobId == null) {
            resp.sendRedirect(req.getContextPath() + "/mo/applicants");
            return;
        }

        Optional<Job> job = jobs(req).findById(jobId);
        if (!job.isPresent() || !job.get().getMoUsername().equalsIgnoreCase(mo)) {
            resp.sendRedirect(req.getContextPath() + "/mo/applicants");
            return;
        }

        ApplicationStore as = apps(req);
        Optional<Application> appOpt = as.findById(applicationId);
        if (!appOpt.isPresent() || !appOpt.get().getJobId().equals(jobId)) {
            resp.sendRedirect(req.getContextPath() + "/mo/applicants?jobId=" + jobId);
            return;
        }

        Application a = appOpt.get();
        if ("shortlist".equalsIgnoreCase(action)) {
            a.setStatus(ApplicationStatus.SHORTLISTED);
        } else if ("reject".equalsIgnoreCase(action)) {
            a.setStatus(ApplicationStatus.REJECTED);
        }
        as.save(a);
        resp.sendRedirect(req.getContextPath() + "/mo/applicants?jobId=" + jobId);
    }
}

