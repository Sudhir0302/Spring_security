package com.sudhir003.spring_security.config;

import com.sudhir003.spring_security.service.JwtService;
import com.sudhir003.spring_security.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter
{

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader=request.getHeader("Authorization");
        String token=null;
        String username=null;

        if(authHeader!=null&&authHeader.startsWith("Bearer "))
        {
            token=authHeader.substring(7);
            username=jwtService.extractUserName(token);
        }

        if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null)
        {

            UserDetails userDetails=myUserDetailsService.loadUserByUsername(username);
             if(jwtService.validateToken(token,userDetails)){
                 UsernamePasswordAuthenticationToken authToken=
                         new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //set if you want request-specific metadata (like IP or session ID) to be available in the security context.
                 SecurityContextHolder.getContext().setAuthentication(authToken);
             }
        }
        filterChain.doFilter(request,response);
    }
}
