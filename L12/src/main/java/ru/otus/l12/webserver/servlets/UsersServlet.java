package ru.otus.l12.webserver.servlets;

import com.google.gson.Gson;
import ru.otus.l12.base.UserDataSet;
import ru.otus.l12.webserver.UserHttpService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UsersServlet extends HttpServlet {
    private final UserHttpService userService;
    private final Gson gson;
    private static final String APPLICATION_JSON = "application/json;charset=UTF-8";

    public UsersServlet(UserHttpService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserDataSet> users = userService.getUsers(req.getParameter("id"));
        if (users != null) {
            resp.setContentType(APPLICATION_JSON);
            ServletOutputStream out = resp.getOutputStream();
            out.print(gson.toJson(users));
        }


    }
}
