package ru.otus.l14.frontend.webserver.servlets;

import com.google.gson.Gson;
import ru.otus.l14.app.FrontendService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsersServlet extends HttpServlet {
    private final FrontendService frontService;
    private static final String APPLICATION_JSON = "application/json;charset=UTF-8";

    public UsersServlet(FrontendService frontService, Gson gson) {
        this.frontService = frontService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String property = req.getParameter("property").trim();
        String jsonResult = null;
        if (property.equals("list")) {
            jsonResult = frontService.getUsers(req.getParameter("id"));
        }
        if (property.equals("count")) {
            jsonResult = frontService.getUsersCount();
        }
        if (jsonResult != null) {
            resp.setContentType(APPLICATION_JSON);
            ServletOutputStream out = resp.getOutputStream();
            out.print(jsonResult);
        }
    }
}

