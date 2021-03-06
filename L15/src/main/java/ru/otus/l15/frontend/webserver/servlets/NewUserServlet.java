package ru.otus.l15.frontend.webserver.servlets;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.l15.app.FrontendService;
import ru.otus.l15.app.messages.MsgAddUser;
import ru.otus.l15.app.messages.MsgAddUserAnswer;
import ru.otus.l15.db.base.AddressDataSet;
import ru.otus.l15.db.base.PhoneDataSet;
import ru.otus.l15.db.base.UserDataSet;
import ru.otus.l15.messageSystem.Message;

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

        Message msg = new MsgAddUser(frontService.getAddress(), frontService.getDbAddress(), user);
        frontService.sendMessage(msg);
        MsgAddUserAnswer answer = (MsgAddUserAnswer) frontService.getAnswer(msg);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addNewUser(req);
    }
}
