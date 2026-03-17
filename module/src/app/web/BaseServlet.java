package app.web;

import app.store.ApplicationStore;
import app.store.JobStore;
import app.store.ProfileStore;
import app.store.UserStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;

public abstract class BaseServlet extends HttpServlet {

    protected UserStore users(HttpServletRequest req) {
        return (UserStore) getServletContext().getAttribute(AppBootstrapListener.ATTR_USERS);
    }

    protected ProfileStore profiles(HttpServletRequest req) {
        return (ProfileStore) getServletContext().getAttribute(AppBootstrapListener.ATTR_PROFILES);
    }

    protected JobStore jobs(HttpServletRequest req) {
        return (JobStore) getServletContext().getAttribute(AppBootstrapListener.ATTR_JOBS);
    }

    protected ApplicationStore apps(HttpServletRequest req) {
        return (ApplicationStore) getServletContext().getAttribute(AppBootstrapListener.ATTR_APPS);
    }

    protected File dataDir() {
        return (File) getServletContext().getAttribute(AppBootstrapListener.ATTR_DATA_DIR);
    }

    protected void render(HttpServletRequest req, HttpServletResponse resp, String jspPath) throws ServletException, java.io.IOException {
        resp.setContentType("text/html; charset=UTF-8");
        req.getRequestDispatcher(jspPath).forward(req, resp);
    }
}

