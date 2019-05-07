package ru.otus.l14.front.servlets;
import ru.otus.l14.app.FrontendService;
import ru.otus.l14.front.TemplateProcessor;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends HttpServlet {
    private final FrontendService frontService;
    private final TemplateProcessor templateProcessor;
    private final String USERNAME_VARIABLE_NAME = "userName";


    public MainServlet(FrontendService frontService, TemplateProcessor templateProcessor) {
        this.frontService = frontService;
        this.templateProcessor = templateProcessor;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put(USERNAME_VARIABLE_NAME, frontService.getUserNameAttribute(req));
        resp.getWriter().println(templateProcessor.getPage("main.html", data));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

}
