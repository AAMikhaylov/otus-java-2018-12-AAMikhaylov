package ru.otus.l14.app;

import ru.otus.l14.db.base.UserDataSet;
import ru.otus.l14.messageSystem.Addressee;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FrontendService extends Addressee {

    void init();

    boolean authenticate(String login, String password, HttpServletRequest req, HttpServletResponse resp);

    boolean authenticate(HttpServletRequest req);

    void authenticateResp(boolean authResult, String msgSourceId);

    String getUserNameAttribute(HttpServletRequest req);

    void addNewUser(HttpServletRequest req);

    long getUsersCount();

    List<UserDataSet> getUsers(String idStr);
}
