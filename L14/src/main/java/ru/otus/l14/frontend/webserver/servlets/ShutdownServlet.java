package ru.otus.l14.frontend.webserver.servlets;

import org.eclipse.jetty.server.Server;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShutdownServlet extends HttpServlet {
    final private Server jettyServer;

    public ShutdownServlet(Server jettyServer) {
        this.jettyServer = jettyServer;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(500);
                jettyServer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();

    }
}
