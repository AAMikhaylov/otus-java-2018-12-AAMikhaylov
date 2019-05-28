package ru.otus.frontend.servlets;

import ru.otus.frontend.SessionParameters;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends HttpServlet {
    private final String USERNAME_VARIABLE_NAME = "userName";

    public MainServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put(USERNAME_VARIABLE_NAME, req.getSession(false).getAttribute(SessionParameters.USERNAME_SESSION_ATTRIBUTE));
        resp.getWriter().println(new TemplateProcessor().getPage("main.html", data));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

}
