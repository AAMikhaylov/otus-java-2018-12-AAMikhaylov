package ru.otus.l14.front.servlets;

import ru.otus.l14.front.UserHttpService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewUserServlet extends HttpServlet {
    private final UserHttpService userService;

    public NewUserServlet(UserHttpService userService) {
        this.userService = userService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userService.addNewUser(req);
         }
}
