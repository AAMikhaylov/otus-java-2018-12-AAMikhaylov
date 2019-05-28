package ru.otus.frontend.servlets;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.frontend.FrontendService;
import ru.otus.frontend.messages.MsgAuthUser;
import ru.otus.frontend.messages.MsgAuthUserAnswer;
import ru.otus.messageSystem.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private FrontendService frontService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        frontService = webApplicationContext.getBean("frontendService", FrontendService.class);
    }

    public LoginServlet() {
    }

    public void setFrontService(FrontendService frontService) {
        this.frontService = frontService;
    }

    private boolean authenticate(String login, String password, HttpServletRequest req) {
        Message msg = new MsgAuthUser(frontService.getAddress(), frontService.getDbAddress(), login, password, req);
        frontService.sendMessage(msg);
        MsgAuthUserAnswer answer = (MsgAuthUserAnswer) frontService.getAnswer(msg);
        if (answer != null && answer.isAuth())
            return true;
        return false;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (password != null && login != null && authenticate(login, password, req))
            resp.sendRedirect("/main");
        else
            resp.sendRedirect("/?");
    }
}
