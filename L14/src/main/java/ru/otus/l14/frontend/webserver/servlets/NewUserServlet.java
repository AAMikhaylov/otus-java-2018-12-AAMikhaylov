package ru.otus.l14.frontend.webserver.servlets;

import ru.otus.l14.app.FrontendService;
import ru.otus.l14.app.messages.MsgAddUser;
import ru.otus.l14.app.messages.MsgAddUserAnswer;
import ru.otus.l14.db.base.AddressDataSet;
import ru.otus.l14.db.base.PhoneDataSet;
import ru.otus.l14.db.base.UserDataSet;
import ru.otus.l14.messageSystem.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewUserServlet extends HttpServlet {
    private final FrontendService frontService;

    public NewUserServlet(FrontendService frontService) {
        this.frontService = frontService;
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
