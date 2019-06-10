package ru.otus.frontend.servlets;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.frontend.FrontendService;
import ru.otus.frontend.messages.MsgGetUsers;
import ru.otus.frontend.messages.MsgGetUsersAnswer;
import ru.otus.frontend.messages.MsgGetUsersCount;
import ru.otus.frontend.messages.MsgGetUsersCountAnswer;
import ru.otus.l16.messageSystem.Message;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsersServlet extends HttpServlet {
    private FrontendService frontService;
    private static final String APPLICATION_JSON = "application/json;charset=UTF-8";

    @Override
    public void init() throws ServletException {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        frontService = webApplicationContext.getBean("frontendService", FrontendService.class);
    }

    public UsersServlet() {
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

