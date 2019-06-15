package ru.otus.l16.dbService;


import ru.otus.l16.channel.MsgChannel;
import ru.otus.l16.channel.MsgChannelClient;
import ru.otus.l16.messageSystem.Address;

public class DBServiceClient {
    public static void main(String[] args) {
        try {
            if (args.length < 2)
                throw new IllegalArgumentException("Argument count exception.");
            DBService db = new DBServiceHibernateImpl("db/hibernate_oracle.cfg.xml");
            Address address=new Address("DB1");
            MsgChannel msgChannel = new MsgChannelClient(address,Integer.parseInt(args[0]), args[1]);
            msgChannel.setAcceptHandler((message) -> System.out.println(message));
            msgChannel.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
