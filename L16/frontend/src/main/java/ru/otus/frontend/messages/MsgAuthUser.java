package ru.otus.frontend.messages;
import ru.otus.dbService.DBService;
import ru.otus.frontend.SessionParameters;
import ru.otus.dbService.base.UserDataSet;
import ru.otus.messageSystem.Address;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class MsgAuthUser extends MsgToDB {

    private final String login;
    private final String password;
    private final HttpServletRequest httpRequest;

    public MsgAuthUser(Address from, Address to, String login, String password, HttpServletRequest httpRequest) {
        super(from, to);
        this.login = login;
        this.password = password;
        this.httpRequest = httpRequest;
    }

    public MsgAuthUser(Address from, Address to, HttpServletRequest httpRequest) {
        super(from, to);
        password = null;
        HttpSession session = httpRequest.getSession(false);
        if (session == null)
            login = null;
        else
            login = (String) session.getAttribute(SessionParameters.LOGIN_SESSION_ATTRIBUTE);
        this.httpRequest = httpRequest;
    }

    @Override
    public void exec(DBService dbService) {
        Boolean authResult = false;
        if (login != null)
            try {
                UserDataSet user = dbService.load(login, UserDataSet.class);
                if (user != null) {
                    if (password != null) {
                        authResult = password.equals(user.getPassword());
                        if (authResult) {
                            HttpSession session = httpRequest.getSession(true);
                            if (session != null) {
                                session.setMaxInactiveInterval(SessionParameters.SESSION_EXPIRE_INTERVAL);
                                session.setAttribute(SessionParameters.LOGIN_SESSION_ATTRIBUTE, login);
                                session.setAttribute(SessionParameters.USERNAME_SESSION_ATTRIBUTE, user.getName());
                                user.setSessionID(session.getId());
                                dbService.update(user);
                            }
                        }
                    } else {
                        HttpSession session = httpRequest.getSession(false);
                        if (session != null)
                            authResult = session.getId().equals(user.getSessionID());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        dbService.getMS().sendMessage(new MsgAuthUserAnswer(getTo(), getFrom(), authResult, this));
    }
}