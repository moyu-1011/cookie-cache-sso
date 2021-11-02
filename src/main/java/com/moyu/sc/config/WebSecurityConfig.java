package com.moyu.sc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyu.sc.security.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(req -> req.anyRequest().authenticated())
                .formLogin(login -> login.loginPage("/login").loginProcessingUrl("/dologin"))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .rememberMe(AbstractHttpConfigurer::disable);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from user where username = ?")
                .authoritiesByUsernameQuery("select username, role from role where username = ?")
                .passwordEncoder(passwordEncoder());

    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring().antMatchers("/public/**")
                .antMatchers("/login")
                .antMatchers("/error")
                .antMatchers("/authorize/**")
                .antMatchers("/h2-console/**");
    }


    private AuthenticationFailureHandler getAuthenticationFailureHandler() {
        // 登录成功处理器
        return (req, res, e) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> map = new HashMap<>();
            map.put("title", "Login Failure");
            map.put("details", e.getMessage());
            res.setCharacterEncoding("UTF-8");
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.getWriter().println(objectMapper.writeValueAsString(map));
        };
    }

    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        // 登录失败处理器
        return (req, res, auth) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> map = new HashMap<>();
            map.put("title", "Login Success");
            res.setStatus(HttpStatus.OK.value());
            res.setCharacterEncoding("UTF-8");
            res.getWriter().println(objectMapper.writeValueAsString(map));
        };
    }
}
