package ru.otus.l15.frontend.webserver.servlets;

import com.google.gson.Gson;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.l15.app.DBService;
import ru.otus.l15.app.FrontendService;
import ru.otus.l15.db.dbService.DBServiceHibernateImpl;
import ru.otus.l15.frontend.FrontendServiceImpl;
import ru.otus.l15.frontend.webserver.AuthorisationFilter;
import ru.otus.l15.frontend.webserver.TemplateProcessor;
import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.MessageSystem;
import ru.otus.l15.messageSystem.MessageSystemContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class InitServlet extends HttpServlet {

    public InitServlet() {
    }

    @Override
    public void init() throws ServletException {
        MessageSystem messageSystem = new MessageSystem();
        MessageSystemContext context = new MessageSystemContext(messageSystem);
        Address frontAddress = new Address("Frontend");
        context.setFrontAddress(frontAddress);
        Address dbAddress = new Address("DB");
        context.setDbAddress(dbAddress);
        FrontendService frontendService = new FrontendServiceImpl(context, frontAddress);
        frontendService.init();
        DBService dbService = new DBServiceHibernateImpl(context, dbAddress, "db/hibernate.cfg.xml");
//        DBService dbService = new DBServiceHibernateImpl(context,dbAddress,"db/hibernate_oracle.cfg.xml");
        dbService.init();
        messageSystem.start();
        ServletContext contextHandler=getServletContext();
//        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();
        contextHandler.addServlet("mainServlet",new MainServlet(templateProcessor)).addMapping("/main");
        contextHandler.addServlet("loginServlet",new LoginServlet(frontendService)).addMapping("/login");
        /*contextHandler.addServlet(new ServletHolder(new LoginServlet(frontendService)), "/login");
        contextHandler.addServlet(new ServletHolder(new UsersServlet(frontendService, new Gson())), "/users");
        contextHandler.addServlet(new ServletHolder(new NewUserServlet(frontendService)), "/newUser");
        contextHandler.addFilter(new FilterHolder(new AuthorisationFilter(frontendService)), "/main", null);
        contextHandler.addFilter(new FilterHolder(new AuthorisationFilter(frontendService)), "/users", null);
        contextHandler.addFilter(new FilterHolder(new AuthorisationFilter(frontendService)), "/newUser", null);

         */
    }

}
