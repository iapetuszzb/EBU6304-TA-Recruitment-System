package app.web;

import app.model.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("username", Auth.currentUsername(req).orElse(null));
        req.setAttribute("role", Auth.currentRole(req).orElse(null));
        render(req, resp, "/WEB-INF/jsp/home.jsp");
    }
}

