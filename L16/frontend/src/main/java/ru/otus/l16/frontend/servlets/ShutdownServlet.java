package ru.otus.l16.frontend.servlets;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.l16.frontend.FrontendService;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.message.MsgShutdown;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShutdownServlet extends HttpServlet {
    private FrontendService frontService;

    public ShutdownServlet() {
    }


    @Override
    public void init() throws ServletException {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        frontService = webApplicationContext.getBean("frontendService", FrontendService.class);
    }

    public void shutdown() {
        MsgShutdown msgShutdown = new MsgShutdown(frontService.getAddress(), null);
        frontService.sendMessage(msgShutdown);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        shutdown();
        resp.setStatus(HttpServletResponse.SC_OK);
        ServletOutputStream out = resp.getOutputStream();
        resp.setContentType("text/html; charset=UTF-8");
        StringBuilder respHTML = new StringBuilder("<!DOCTYPE html>");
        respHTML.append("<html>");
        respHTML.append("<head>");
        respHTML.append("<meta charset=\"UTF-8\">");
        respHTML.append("<title>До свидания </title>");
        respHTML.append("</head>");
        respHTML.append("<body>");
        respHTML.append("<h1>До новых встреч! </h1>");
        respHTML.append("<hr>");
        respHTML.append("</body>");
        respHTML.append("</html>");
        out.print(respHTML.toString());
    }
}
