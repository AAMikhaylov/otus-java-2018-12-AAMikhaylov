package ru.otus.l12.webserver.servlets;

import ru.otus.l12.webserver.TemplateProcessor;
import ru.otus.l12.webserver.UserHttpService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends HttpServlet {
    private final UserHttpService userService;
    private final TemplateProcessor templateProcessor;
    private final String USERNAME_VARIABLE_NAME="userName";


    public MainServlet(UserHttpService userService, TemplateProcessor templateProcessor) {
        this.userService = userService;
        this.templateProcessor = templateProcessor;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put(USERNAME_VARIABLE_NAME,userService.getUserNameCookie(req));
        resp.getWriter().println(templateProcessor.getPage("main.html", data));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

}
