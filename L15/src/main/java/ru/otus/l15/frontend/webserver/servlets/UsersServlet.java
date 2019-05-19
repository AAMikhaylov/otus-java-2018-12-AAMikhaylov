package ru.otus.l15.frontend.webserver.servlets;

import com.google.gson.Gson;
import ru.otus.l15.app.FrontendService;
import ru.otus.l15.app.messages.MsgGetUsers;
import ru.otus.l15.app.messages.MsgGetUsersAnswer;
import ru.otus.l15.app.messages.MsgGetUsersCount;
import ru.otus.l15.app.messages.MsgGetUsersCountAnswer;
import ru.otus.l15.messageSystem.Message;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsersServlet extends HttpServlet {
    private final FrontendService frontService;
    private static final String APPLICATION_JSON = "application/json;charset=UTF-8";

    public UsersServlet(FrontendService frontService, Gson gson) {
        this.frontService = frontService;
    }

    public String getUsersCount() {
        Message msg = new MsgGetUsersCount(frontService.getAddress(), frontService.getDbAddress());
        frontService.sendMessage(msg);
        MsgGetUsersCountAnswer answer = (MsgGetUsersCountAnswer) frontService.getAnswer(msg);
        if (answer != null)
            return answer.getUsersCount();
        return null;
    }

    public String getUsers(String idStr) {
        Message msg = new MsgGetUsers(frontService.getAddress(), frontService.getDbAddress(), idStr);
        frontService.sendMessage(msg);
        MsgGetUsersAnswer answer = (MsgGetUsersAnswer) frontService.getAnswer(msg);
        if (answer != null)
            return answer.getUsersJsonList();
        return null;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String property = req.getParameter("property").trim();
        String jsonResult = null;
        if (property.equals("list")) {
            jsonResult = getUsers(req.getParameter("id"));
        }
        if (property.equals("count")) {
            jsonResult = getUsersCount();
        }
        if (jsonResult != null) {
            resp.setContentType(APPLICATION_JSON);
            ServletOutputStream out = resp.getOutputStream();
            out.print(jsonResult);
        }
    }
}

