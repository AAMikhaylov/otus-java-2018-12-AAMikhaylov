package ru.otus.l16.dbService;

import picocli.CommandLine;
import ru.otus.l16.messageSystem.channel.MsgChannel;
import ru.otus.l16.messageSystem.channel.MsgChannelClient;
import ru.otus.l16.messageSystem.Address;

import ru.otus.l16.messageSystem.MsClient;

public class DBServiceMain {

    public static void main(String[] args) {
        ComamndArgs comamndArgs = CommandLine.populateCommand(new DBServiceMain().new ComamndArgs(), args);
        Address address = new Address(comamndArgs.address);
        MsgChannel msgChannel = new MsgChannelClient(address, comamndArgs.serverHost, comamndArgs.serverPort);
        DBService db = new DBServiceHibernateImpl("db/hibernate.cfg.xml",comamndArgs.createSchema);
        db.init(comamndArgs.createSchema);
        MsClient dbMsClient = new DbMsClient(db, msgChannel, address);
        msgChannel.setAcceptHandler(dbMsClient::messageHandler);
        msgChannel.start();
    }

    private class ComamndArgs {
        @CommandLine.Option(names = {"address"}, description = "Service address")
        String address;
        @CommandLine.Option(names = {"serverPort"}, description = "Message server port")
        int serverPort;
        @CommandLine.Option(names = {"serverHost"}, description = "Message server host")
        String serverHost;
        @CommandLine.Option(names = {"createSchema"}, description = "Creation database schema")
        boolean createSchema;
    }
}
