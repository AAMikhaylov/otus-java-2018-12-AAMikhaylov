package ru.otus.l15.app;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.l15.messageSystem.MessageSystem;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.sql.SQLException;

public class WebLoaderListener extends ContextLoaderListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        ServletContext servletContext = event.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        MessageSystem messageSystem = webApplicationContext.getBean("messageSystem", MessageSystem.class);
        messageSystem.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        MessageSystem messageSystem = webApplicationContext.getBean("messageSystem", MessageSystem.class);
        messageSystem.dispose();
        DBService dbService = webApplicationContext.getBean("dbService", DBService.class);
        try {
            dbService.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.contextDestroyed(event);
    }
}
