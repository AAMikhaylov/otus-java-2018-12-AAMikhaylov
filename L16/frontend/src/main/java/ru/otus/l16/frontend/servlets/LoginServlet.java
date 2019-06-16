package ru.otus.l16.frontend.servlets;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.l16.frontend.FrontendService;
import ru.otus.l16.frontend.SessionParameters;
import ru.otus.l16.messageSystem.message.Message;
import ru.otus.l16.messageSystem.message.MsgAuthUser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private FrontendService frontService;

    @Override
    public void init() {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        frontService = webApplicationContext.getBean("frontendService", FrontendService.class);
    }

    public LoginServlet() {
    }


    private boolean authenticate(String login, String password, HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(true);
        if (session != null) {
            session.setMaxInactiveInterval(SessionParameters.SESSION_EXPIRE_INTERVAL);
            session.setAttribute(SessionParameters.LOGIN_SESSION_ATTRIBUTE, login);
            Message msg = new MsgAuthUser(frontService.getAddress(), frontService.getDbAddress(), login, password, session.getId());
            frontService.sendMessage(msg);
            MsgAuthUser answer = (MsgAuthUser) frontService.getAnswer(msg);
            if (answer != null && answer.isAuth()) {
                session.setAttribute(SessionParameters.USERNAME_SESSION_ATTRIBUTE, answer.getUserName());
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (password != null && login != null && authenticate(login, password, req))
            resp.sendRedirect("/main");
        else
            resp.sendRedirect("/?");
    }
}
