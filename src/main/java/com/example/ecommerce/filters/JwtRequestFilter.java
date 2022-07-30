package com.example.ecommerce.filters;


import com.example.ecommerce.userDetails.MyUserDetailsService;
import com.example.ecommerce.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpConnectTimeoutException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;

@Component @Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;


    String username = null;
    String jwt = null;

    boolean isLoggedIn = false;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("cookie");
        Iterator<String> iterator = request.getHeaderNames().asIterator();
       /* while (iterator.hasNext()){
            log.info("******************************request headers "+ iterator.next()+"******************************");
           // log.info("******************************request headers "+ request.getRequestURI()+"******************************");

        }*/
        String reqUri = request.getRequestURI();

        if (reqUri.equalsIgnoreCase("/login")) {
            setLoggedIn(true);
            log.info("****************************** reqUri " + reqUri + "******************************");
            log.info("****************************** user is logging in now " + isLoggedIn() + "******************************");

        }
        log.info("****************************** reqUri " + reqUri + "******************************");

        if (reqUri.equalsIgnoreCase("/logout")) {
            log.info("****************************** reqUri " + reqUri + "******************************");
            log.info("****************************** user is logging out TRUE******************************");

        }
        if (reqUri.contains("&&Bearer")) {
            String BearerToken = reqUri.substring(reqUri.indexOf("&&Bearer") + 11);
            log.info("Bearer token is=>" + reqUri.substring(reqUri.indexOf("&&Bearer") + 11));

            log.info("****************************** security context" + SecurityContextHolder.getContext().getAuthentication() + "******************************");

            // if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            //  if (isLoggedIn()==true) {
            if (BearerToken != null) {
                log.info("******************************in the jwt filter******************************");
                this.setJwt(BearerToken);
                log.info("******************************jwt is " + getJwt() + "******************************");

                this.setUsername(jwtUtil.extractUsernmae(getJwt()));
                log.info("******************************username is " + username + "******************************");

            } else {
                SecurityContextHolder.getContext().setAuthentication(null);
            }

            if (this.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("******************************in the jwt authentication validation process******************************");

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(this.getJwt(), userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            /*}else {
                response.setStatus(401,"user needs to log in");
                response.sendError(401,"user needs to log in");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"User Needs to Log In First");
              //  throw new HttpConnectTimeoutException("User needs to log in to access the resource");
*/
        }

        log.info("******************************jwt request " + request.getRemoteUser() + "******************************");
        log.info("******************************jwt response " + response + "******************************");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(request, response);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
