package com.example.ecommerce.security;


import com.example.ecommerce.filters.JwtRequestFilter;
import com.example.ecommerce.userDetails.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity(debug = true) @Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable()
                .authorizeRequests().antMatchers("/getAllProducts","/login","/logout","/product/{productName}","/logout_success").permitAll()
                .antMatchers("/categories/{category}").permitAll().anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().headers()
                .and().formLogin().loginPage("http://localhost:3000/").successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        log.info("****************************** reqUri "+request.getRequestURI()+"******************************");
                        log.info("****************************** reqUri "+request.getServletContext()+"******************************");

                    }
                })
                .and().logout().permitAll().logoutUrl("http://localhost:3000/").logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/logout_success")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        log.info("Logged out successfully");
                        log.info("******************************jwt value is "+ jwtRequestFilter.getJwt()+"******************************");
                        log.info("******************************one last time checking if user is logged in "+ jwtRequestFilter.isLoggedIn()+"******************************");
                       jwtRequestFilter.setJwt(null);
                        jwtRequestFilter.setLoggedIn(false);
                        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
                        response.setHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS");
                        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
                        response.setHeader("Access-Control-Allow-Credentials", "true");

                        //  response.sendRedirect("/logout_success");
                    }
                }).invalidateHttpSession(true).clearAuthentication(true)
                .and().addFilterBefore(this.jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);
    }
   /* @Override
    public void configure(WebSecurity web) throws Exception {
        // Ignore spring security in these paths
       // web.ignoring().antMatchers(HttpMethod.GET, "/getAllProducts");
        web.ignoring().antMatchers(HttpMethod.GET, "/{category}");
              //  ,"/login", "/{category}", "/product/{productName}", "/logout").permitAll()
    }*/
   @Bean
   CorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration configuration = new CorsConfiguration();
       configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000/", "http://localhost:8080"));
       configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST","OPTIONS", "DELETE"));
       configuration.setAllowedHeaders(Arrays.asList("authorization","content-type","Access-Control-Allow-Origin"));
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", configuration);
       return source;
   }

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
