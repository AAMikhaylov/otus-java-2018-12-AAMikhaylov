package ru.otus.l14.frontend.webserver.servlets;

import ru.otus.l14.app.FrontendService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final FrontendService frontService;

    public LoginServlet(FrontendService frontService) {
        this.frontService = frontService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (password != null && login != null && frontService.authenticate(login, password, req))

            resp.sendRedirect("/main");
        else
            resp.sendRedirect("/?");
    }
}
