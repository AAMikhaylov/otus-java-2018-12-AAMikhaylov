package ru.otus.l14.front;

import ru.otus.l14.app.FrontendService;

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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if (frontService.authenticate(req))
            filterChain.doFilter(servletRequest, servletResponse);
        else
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    public void destroy() {

    }
}
