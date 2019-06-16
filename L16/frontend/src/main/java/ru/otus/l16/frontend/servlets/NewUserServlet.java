package ru.otus.l16.frontend.servlets;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.l16.frontend.FrontendService;
import ru.otus.l16.messageSystem.message.Message;
import ru.otus.l16.messageSystem.message.MsgAddUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewUserServlet extends HttpServlet {
    private FrontendService frontService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        frontService = webApplicationContext.getBean("frontendService", FrontendService.class);
    }

    public NewUserServlet() {
    }

    private void addNewUser(HttpServletRequest req) {
        String userAddress = req.getParameter("address").trim();
        String userPhones = req.getParameter("phones").trim();
        String password = req.getParameter("password").trim();
        String userName = req.getParameter("userName").trim();
        String login = req.getParameter("login").trim();
        int age = Integer.parseInt(req.getParameter("age"));

        Message msg = new MsgAddUser(frontService.getAddress(), frontService.getDbAddress(), login, password, userName, age, userAddress, userPhones);
        frontService.sendMessage(msg);
        MsgAddUser answer = (MsgAddUser) frontService.getAnswer(msg);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addNewUser(req);
    }
}
