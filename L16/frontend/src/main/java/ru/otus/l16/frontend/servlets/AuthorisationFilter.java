package ru.otus.l16.frontend.servlets;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.l16.frontend.FrontendService;
import ru.otus.l16.frontend.SessionParameters;
import ru.otus.l16.messages.Message;
import ru.otus.l16.messages.MsgAuthUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorisationFilter implements Filter {
    private FrontendService frontService;

    public AuthorisationFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        frontService = webApplicationContext.getBean("frontendService", FrontendService.class);
    }

    private boolean authenticate(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            String sessionID = session.getId();
            String login = (String) session.getAttribute(SessionParameters.LOGIN_SESSION_ATTRIBUTE);
            Message msg = new MsgAuthUser(frontService.getAddress(), frontService.getDbAddress(), login, "", sessionID);
            frontService.sendMessage(msg);
            MsgAuthUser answer = (MsgAuthUser) frontService.getAnswer(msg);
            if (answer != null && answer.isAuth())
                return true;
        }
            return false;
        }

        @Override
        public void doFilter (ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
        IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            if (authenticate(req))
                filterChain.doFilter(servletRequest, servletResponse);
            else
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        @Override
        public void destroy () {

        }
    }
