package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.UserAccount; 
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter{
    // Path matcher, supports wildcards
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            String requestURI = request.getRequestURI();
            log.info("Intercepting request: {}",requestURI);

            String[] urls = new String[]{
                    "/auth/login",      
                    "/auth/logout",     
                    "/auth/register",   
                    "/backend/**",
                    "/frontend/**",
                    "/common/**",       
                    "/doc.html",        
                    "/webjars/**",
                    "/swagger-resources",
                    "/v2/api-docs",
                    "/error"            
            };

            boolean check = check(urls, requestURI);

            if(check){
                log.info("Request {} does not need processing",requestURI);
                filterChain.doFilter(request,response);
                return;
            }

            if(request.getSession().getAttribute("user") != null){
                UserAccount user = (UserAccount)request.getSession().getAttribute("user");
                log.info("User is logged in, user id: {}", user.getId());
                BaseContext.setCurrentId(user.getId());
                filterChain.doFilter(request,response);
                return;
            }

            log.info("User is not logged in");
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        } finally {
            BaseContext.removeCurrentId(); 
        }
    }

    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
