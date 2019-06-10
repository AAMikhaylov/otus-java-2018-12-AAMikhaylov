package ru.otus.l16;

import ru.otus.l16.channel.MsgChannel;
import ru.otus.l16.channel.MsgClientChannel;
import ru.otus.l16.dbService.DBService;
import ru.otus.l16.dbService.DBServiceHibernateImpl;

public class DBServiceMain {
    public static void main(String[] args) {
        try {
            if (args.length < 2)
                throw new Exception("Argument count exception.");
            DBService db = new DBServiceHibernateImpl("db/hibernate_oracle.cfg.xml");
            MsgChannel msgChannel = new MsgClientChannel(Integer.parseInt(args[0]), "localhost", args[1],(message) -> System.out.println(message));
            msgChannel.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
