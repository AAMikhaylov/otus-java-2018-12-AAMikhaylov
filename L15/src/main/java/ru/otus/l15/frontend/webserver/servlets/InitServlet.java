package ru.otus.l15.frontend.webserver.servlets;
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
import java.sql.SQLException;

public class InitServlet extends HttpServlet {
    private MessageSystem messageSystem;
    private DBService dbService;

    public InitServlet() {
    }

    @Override
    public void init() throws ServletException {
        messageSystem = new MessageSystem();
        MessageSystemContext messageSystemContext = new MessageSystemContext(messageSystem);
        Address frontAddress = new Address("Frontend");
        messageSystemContext.setFrontAddress(frontAddress);
        Address dbAddress = new Address("DB");
        messageSystemContext.setDbAddress(dbAddress);
        FrontendService frontendService = new FrontendServiceImpl(messageSystemContext, frontAddress);
        frontendService.init();
//        DBService dbService = new DBServiceHibernateImpl(messageSystemContext, dbAddress, "db/hibernate.cfg.xml");
        dbService = new DBServiceHibernateImpl(messageSystemContext,dbAddress,"db/hibernate_oracle.cfg.xml");
        dbService.init();
        messageSystem.start();
        ServletContext servletContext=getServletContext();
        TemplateProcessor templateProcessor = new TemplateProcessor();
        servletContext.addServlet("mainServlet",new MainServlet(templateProcessor)).addMapping("/main");
//        servletContext.addServlet("loginServlet",new LoginServlet(frontendService)).addMapping("/login");
        servletContext.addServlet("usersServlet",new UsersServlet(frontendService)).addMapping("/users");
        servletContext.addServlet("newUserServlet",new NewUserServlet(frontendService)).addMapping("/newUser");
        AuthorisationFilter authFilter = new AuthorisationFilter(frontendService);
        servletContext.addFilter("filter",authFilter).addMappingForUrlPatterns(null,true,"/main","/users","/newUser");
    }

    @Override
    public void destroy() {
        messageSystem.dispose();
        try {
            dbService.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.destroy();


    }
}
