package ru.otus.l14.app;

import ru.otus.l14.messageSystem.Addressee;
import ru.otus.l14.messageSystem.Message;

import javax.servlet.http.HttpServletRequest;


public interface FrontendService extends Addressee {

    void init();

    boolean authenticate(String login, String password, HttpServletRequest req);

    boolean authenticate(HttpServletRequest req);

    void saveAnswer(Message msgAnswer, Message msgSource);

    void addNewUser(HttpServletRequest req);

    String getUsersCount();

    String getUsers(String idStr);

}
