package ru.otus.l14.front;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.l14.app.FrontendService;
import ru.otus.l14.front.servlets.LoginServlet;
import ru.otus.l14.front.servlets.MainServlet;
import ru.otus.l14.front.servlets.NewUserServlet;
import ru.otus.l14.front.servlets.UsersServlet;

public class JettyServer {
    private final int PORT;
    private final FrontendService frontService;


    public JettyServer(int PORT, FrontendService frontService) {
        this.PORT = PORT;
        this.frontService = frontService;
    }

    public void start() throws Exception {

        Resource resource = Resource.newClassPathResource("/html");
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(resource);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();
        contextHandler.addServlet(new ServletHolder(new MainServlet(frontService, templateProcessor)), "/main");
        contextHandler.addServlet(new ServletHolder(new LoginServlet(frontService)), "/login");
        contextHandler.addServlet(new ServletHolder(new UsersServlet(frontService, new Gson())), "/users");
        contextHandler.addServlet(new ServletHolder(new NewUserServlet(frontService)), "/newUser");
        contextHandler.addFilter(new FilterHolder(new AuthorisationFilter(frontService)), "/main", null);
        contextHandler.addFilter(new FilterHolder(new AuthorisationFilter(frontService)), "/users", null);
        contextHandler.addFilter(new FilterHolder(new AuthorisationFilter(frontService)), "/newUser", null);
        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, contextHandler));
        server.start();
        server.join();
    }
}
