package ru.otus.l15.frontend.webserver;

import ru.otus.l15.app.FrontendService;
import ru.otus.l15.app.messages.MsgAuthUser;
import ru.otus.l15.app.messages.MsgAuthUserAnswer;
import ru.otus.l15.messageSystem.Message;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorisationFilter implements Filter {
    private FrontendService frontService;

    public AuthorisationFilter(FrontendService frontService) {
        this.frontService = frontService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    private boolean authenticate(HttpServletRequest req) {
        Message msg = new MsgAuthUser(frontService.getAddress(), frontService.getDbAddress(), req);
        frontService.sendMessage(msg);
        MsgAuthUserAnswer answer = (MsgAuthUserAnswer) frontService.getAnswer(msg);
        if (answer != null && answer.isAuth())
            return true;
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if (authenticate(req))
            filterChain.doFilter(servletRequest, servletResponse);
        else
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    public void destroy() {

    }
}
