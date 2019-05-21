package ru.otus.l14;


import ru.otus.l14.app.FrontendService;
import ru.otus.l14.messageSystem.Address;
import ru.otus.l14.messageSystem.MessageSystemContext;
import ru.otus.l14.app.DBService;
import ru.otus.l14.db.dbService.DBServiceHibernateImpl;
import ru.otus.l14.frontend.FrontendServiceImpl;
import ru.otus.l14.frontend.webserver.JettyServer;
import ru.otus.l14.messageSystem.MessageSystem;

public class Main {


    public static void main(String[] args) throws Exception {

        MessageSystem messageSystem = new MessageSystem();
        MessageSystemContext context = new MessageSystemContext(messageSystem);
        Address frontAddress = new Address("Frontend");
        context.setFrontAddress(frontAddress);
        Address dbAddress = new Address("DB");
        context.setDbAddress(dbAddress);
        FrontendService frontendService = new FrontendServiceImpl(context, frontAddress);
        frontendService.init();
//        DBService dbService = new DBServiceHibernateImpl(context, dbAddress, "db/hibernate.cfg.xml");
        DBService dbService = new DBServiceHibernateImpl(context,dbAddress,"db/hibernate_oracle.cfg.xml");
        dbService.init();
        messageSystem.start();
        JettyServer jettyServer = new JettyServer(80, frontendService);
        jettyServer.start();
        messageSystem.dispose();
        dbService.shutdown();
    }
}
