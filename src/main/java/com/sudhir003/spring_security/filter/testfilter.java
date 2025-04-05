package com.sudhir003.spring_security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
public class testfilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest)servletRequest;
        System.out.println("filter chainnn");
        System.out.println(req.getMethod()+"\n"+req.getRequestURL());
        System.out.println(req.getContentType()); //gives mime type of request i.e Application/json ,etc
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
