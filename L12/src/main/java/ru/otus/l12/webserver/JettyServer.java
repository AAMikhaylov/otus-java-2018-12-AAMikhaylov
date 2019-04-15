package ru.otus.l12.webserver;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.l12.webserver.servlets.LoginServlet;
import ru.otus.l12.webserver.servlets.MainServlet;
import ru.otus.l12.webserver.servlets.NewUserServlet;
import ru.otus.l12.webserver.servlets.UsersServlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JettyServer {
    private final int PORT;
    private final UserHttpService userService;


    public JettyServer(int PORT, UserHttpService userService) {
        this.PORT = PORT;
        this.userService = userService;
    }

    public void start() throws Exception {

        Resource resource = Resource.newClassPathResource("/html");
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(resource);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();

        contextHandler.addServlet(new ServletHolder(new MainServlet(userService, templateProcessor)), "/main");
        contextHandler.addServlet(new ServletHolder(new LoginServlet(userService)), "/login");
        contextHandler.addServlet(new ServletHolder(new UsersServlet(userService, new Gson())), "/users");
        contextHandler.addServlet(new ServletHolder(new NewUserServlet(userService)), "/newUser");

        contextHandler.addFilter(new FilterHolder(new AuthorisationFilter(userService)), "/main", null);
        contextHandler.addFilter(new FilterHolder(new AuthorisationFilter(userService)), "/users", null);
        contextHandler.addFilter(new FilterHolder(new AuthorisationFilter(userService)), "/newUser", null);
        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, contextHandler));
        server.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String st = br.readLine();
            if ("quit".equals(st)) break;
        }
        server.stop();

    }
}
