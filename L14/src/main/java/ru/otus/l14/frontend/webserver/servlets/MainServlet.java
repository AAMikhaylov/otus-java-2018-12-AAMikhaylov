package ru.otus.l14.frontend.webserver.servlets;

import ru.otus.l14.app.SessionParameters;
import ru.otus.l14.frontend.webserver.TemplateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends HttpServlet {

    private final TemplateProcessor templateProcessor;
    private final String USERNAME_VARIABLE_NAME = "userName";


    public MainServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put(USERNAME_VARIABLE_NAME, req.getSession(false).getAttribute(SessionParameters.USERNAME_SESSION_ATTRIBUTE));
        resp.getWriter().println(templateProcessor.getPage("main.html", data));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

}
