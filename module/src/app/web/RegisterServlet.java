package app.web;

import app.model.Role;
import app.model.TaProfile;
import app.model.User;
import app.service.PasswordHasher;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class RegisterServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        render(req, resp, "/WEB-INF/jsp/register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = safe(req.getParameter("username"));
        String password = req.getParameter("password");
        String password2 = req.getParameter("password2");
        String roleStr = safe(req.getParameter("role"));

        Role role = null;
        try {
            role = Role.from(roleStr);
        } catch (Exception ignored) {}

        if (username.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "用户名和密码不能为空");
            render(req, resp, "/WEB-INF/jsp/register.jsp");
            return;
        }
        if (!username.matches("[A-Za-z0-9_]{3,20}")) {
            req.setAttribute("error", "用户名需为 3-20 位字母/数字/下划线");
            render(req, resp, "/WEB-INF/jsp/register.jsp");
            return;
        }
        if (!password.equals(password2)) {
            req.setAttribute("error", "两次输入的密码不一致");
            render(req, resp, "/WEB-INF/jsp/register.jsp");
            return;
        }
        if (role != Role.TA && role != Role.MO) {
            req.setAttribute("error", "注册仅支持 TA 或 MO");
            render(req, resp, "/WEB-INF/jsp/register.jsp");
            return;
        }
        if ("admin".equalsIgnoreCase(username)) {
            req.setAttribute("error", "该用户名不可用");
            render(req, resp, "/WEB-INF/jsp/register.jsp");
            return;
        }

        Optional<User> existing = users(req).findByUsername(username);
        if (existing.isPresent()) {
            req.setAttribute("error", "用户名已存在");
            render(req, resp, "/WEB-INF/jsp/register.jsp");
            return;
        }

        users(req).save(new User(username, PasswordHasher.hash(password), role));

        if (role == Role.TA) {
            // 初始化空档案，后续由 TA 完善并上传简历
            profiles(req).save(new TaProfile(username, "", "", "", ""));
        }

        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}

