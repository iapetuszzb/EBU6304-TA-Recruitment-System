package app.web;

import app.store.ApplicationStore;
import app.store.JobStore;
import app.store.ProfileStore;
import app.store.UserStore;
import app.util.PathsConfig;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.io.File;

public class AppBootstrapListener implements ServletContextListener {
    public static final String ATTR_DATA_DIR = "DATA_DIR";
    public static final String ATTR_USERS = "USER_STORE";
    public static final String ATTR_PROFILES = "PROFILE_STORE";
    public static final String ATTR_JOBS = "JOB_STORE";
    public static final String ATTR_APPS = "APP_STORE";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext ctx = sce.getServletContext();
            File root = new File(ctx.getRealPath("/"));
            File dataDir = PathsConfig.dataDir(root);

            UserStore users = new UserStore(dataDir);
            users.ensureSeedUsers();

            ctx.setAttribute(ATTR_DATA_DIR, dataDir);
            ctx.setAttribute(ATTR_USERS, users);
            ctx.setAttribute(ATTR_PROFILES, new ProfileStore(dataDir));
            ctx.setAttribute(ATTR_JOBS, new JobStore(dataDir));
            ctx.setAttribute(ATTR_APPS, new ApplicationStore(dataDir));
        } catch (Exception e) {
            throw new RuntimeException("App init failed: " + e.getMessage(), e);
        }
    }
}

