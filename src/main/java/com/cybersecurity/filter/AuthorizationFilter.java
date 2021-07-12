package com.cybersecurity.filter;



import com.cybersecurity.repository.UserRepository;
import com.cybersecurity.service.JWTService;
import com.cybersecurity.service.SessionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Order(1)
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AuthorizationFilter implements Filter {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    SessionService sessionService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods",
                "GET,POST,OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");

        if(!request.getRequestURL().toString().contains("/data/")){
            filterChain.doFilter(request,response);
        }
            String token= request.getHeader("Authorization");
            if(token!=null){
                String decodeString=jwtService.decode(token);
                String username=decodeString.split("_")[0];
                String sessionID=decodeString.split("_")[1];
                System.out.println(sessionID);
                boolean userexist=sessionService.checkSession(sessionID,username);
                if(userexist)
                    filterChain.doFilter(request,response);
                else response.setStatus(406);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
