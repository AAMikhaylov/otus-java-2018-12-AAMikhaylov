package ru.otus.l14.front.servlets;

import com.google.gson.Gson;
import ru.otus.l14.app.FrontendService;
import ru.otus.l14.db.base.UserDataSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
//            List<UserDataSet> usersResult = frontService.getUsers(req.getParameter("id"));
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

