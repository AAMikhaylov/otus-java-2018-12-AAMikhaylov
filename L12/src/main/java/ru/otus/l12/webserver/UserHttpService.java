package ru.otus.l12.webserver;

import ru.otus.l12.base.UserDataSet;
import ru.otus.l12.dbService.DBService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

public class UserHttpService {
    private final int SESSION_EXPIRE_INTERVAL = 600;
    private final String USERNAME_COOKIE_NAME = "userName";
    private final String LOGIN_COOKIE_NAME = "login";

    private final DBService dbService;

    public UserHttpService(DBService dbService) {
        this.dbService = dbService;
    }

    public String getUserNameCookie(HttpServletRequest req) {
        return getCookieByName(req, USERNAME_COOKIE_NAME);

    }

    private String getCookieByName(HttpServletRequest req, String cookieName) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null)
            return null;
        for (int i = 0; i < cookies.length; i++)
            if (cookies[i].getName().equals(cookieName))
                return cookies[i].getValue();
        return null;
    }

    public boolean authenticate(HttpServletRequest req) {
        UserDataSet user;
        try {
            String login = getCookieByName(req, LOGIN_COOKIE_NAME);
            if (login == null)
                return false;
            user = dbService.load(login, UserDataSet.class);
            if (user == null)
                return false;
            HttpSession session = req.getSession(false);
            if (user.getSessionID().equals(session.getId()))
                return true;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticate(String login, String password, HttpServletRequest req, HttpServletResponse resp) {
        UserDataSet user;
        try {
            user = dbService.load(login, UserDataSet.class);
            if (user == null || !user.getPassword().equals(password))
                return false;
            HttpSession session = req.getSession();
            session.setMaxInactiveInterval(SESSION_EXPIRE_INTERVAL);
            user.setSessionID(session.getId());
            dbService.update(user);
            resp.addCookie(new Cookie(LOGIN_COOKIE_NAME, login));
            resp.addCookie(new Cookie(USERNAME_COOKIE_NAME, user.getName()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserDataSet> getUsersJson() {
        try {
            return dbService.load(UserDataSet.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
