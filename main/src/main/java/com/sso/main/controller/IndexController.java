package com.sso.main.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RequestMapping(value = "/view")
@Controller
public class IndexController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String LOGIN_INFO_ADDRESS = "http://localhost:9090/login/info?token=";

    // 主页面
    @GetMapping(value = "/index")
    public String toIndex(@CookieValue(required = false, value = "COOKIE_ID")Cookie cookie, HttpSession session) {
        if (cookie!=null) {
            String token = cookie.getValue();
            if (StringUtils.isNotEmpty(token)) {
                Map loginUser = restTemplate.getForObject(LOGIN_INFO_ADDRESS + token, Map.class);
                session.setAttribute("loginUser", loginUser);
            }
        }

        return "index";
    }


}
