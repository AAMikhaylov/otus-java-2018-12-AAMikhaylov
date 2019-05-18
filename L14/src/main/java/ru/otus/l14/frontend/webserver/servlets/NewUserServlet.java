package ru.otus.l14.frontend.webserver.servlets;

import ru.otus.l14.app.FrontendService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewUserServlet extends HttpServlet {
    private final FrontendService frontService;

    public NewUserServlet(FrontendService frontService) {
        this.frontService = frontService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        frontService.addNewUser(req);
    }
}
