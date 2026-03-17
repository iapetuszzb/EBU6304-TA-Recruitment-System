package app.web;

import app.model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        render(req, resp, "/WEB-INF/jsp/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        boolean ok = Auth.login(req, users(req), username == null ? "" : username.trim(), password == null ? "" : password);
        if (!ok) {
            req.setAttribute("error", "用户名或密码错误");
            render(req, resp, "/WEB-INF/jsp/login.jsp");
            return;
        }
        // 登录后按角色跳转到各自入口
        Role role = Auth.currentRole(req).orElse(null);
        if (role == Role.TA) {
            resp.sendRedirect(req.getContextPath() + "/ta/profile");
        } else if (role == Role.MO) {
            resp.sendRedirect(req.getContextPath() + "/mo/post-job");
        } else if (role == Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/admin/workload");
        } else {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}

