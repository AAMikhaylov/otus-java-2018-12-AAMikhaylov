package ru.otus.l14.front;

import ru.otus.l14.db.base.AddressDataSet;
import ru.otus.l14.db.base.PhoneDataSet;
import ru.otus.l14.db.base.UserDataSet;
import ru.otus.l14.db.dbService.DBService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class UserHttpService {
    private final int SESSION_EXPIRE_INTERVAL = 600;
    private final String USERNAME_SESSION_ATTRIBUTE = "userName";
    private final String LOGIN_SESSION_ATTRIBUTE = "login";

    private final DBService dbService;

    public UserHttpService(DBService dbService) {
        this.dbService = dbService;
    }

    public String getUserNameAttribute(HttpServletRequest req) {
        return (String) req.getSession(false).getAttribute(USERNAME_SESSION_ATTRIBUTE);
    }

    public void addNewUser(HttpServletRequest req) {
        try {
            AddressDataSet addr = new AddressDataSet(req.getParameter("address").trim());
            String[] phonesStr = req.getParameter("phones").split(",");
            PhoneDataSet[] phones = new PhoneDataSet[phonesStr.length];
            for (int i = 0; i < phonesStr.length; i++)
                phones[i] = new PhoneDataSet(phonesStr[i].trim());
            UserDataSet user = new UserDataSet(req.getParameter("login").trim(),
                    req.getParameter("password").trim(),
                    req.getParameter("userName").trim(),
                    Integer.parseInt(req.getParameter("age")),
                    addr, phones
            );
            dbService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getAttributeByName(HttpServletRequest req, String attributeName) {
        HttpSession session = req.getSession(false);
        if (session == null)
            return null;
        Object o = session.getAttribute(attributeName);
        return (String) o;
    }

    public boolean authenticate(HttpServletRequest req) {
        UserDataSet user;
        try {
            String login = getAttributeByName(req, LOGIN_SESSION_ATTRIBUTE);
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
            session.setAttribute(LOGIN_SESSION_ATTRIBUTE, login);
            session.setAttribute(USERNAME_SESSION_ATTRIBUTE, user.getName());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long getUsersCount() {
        try {
            return dbService.count();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<UserDataSet> getUsers(String idStr) {
        try {
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                List<UserDataSet> result = new ArrayList<>();
                result.add(dbService.load(id, UserDataSet.class));
                return result;
            } else
                return dbService.load(UserDataSet.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
