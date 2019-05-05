package ru.otus.l14.front.servlets;

import ru.otus.l14.front.UserHttpService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final UserHttpService userService;

    public LoginServlet(UserHttpService userService) {
        this.userService = userService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        boolean authResult = false;
        if (password != null && login != null && userService.authenticate(login, password, req, resp))
            resp.sendRedirect("/main");
        else
            resp.sendRedirect("/?");
    }
}
