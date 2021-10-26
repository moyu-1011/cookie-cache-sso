package com.sso.login.controller;

import com.sso.login.pojo.User;
import com.sso.login.utils.LoginCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

@RequestMapping(value = "/view")
@Controller
public class ViewController {

    // 登录页
    @GetMapping(value = "/login")
    public String toLogin(@RequestParam(value = "target", required = false)String target,
                          @CookieValue(value = "COOKIE_ID", required = false)Cookie cookie,
                          HttpSession session) {
        // 判断登录后返回地址, 为空时返回首页
        if (StringUtils.isEmpty(target)) {
            target = "localhost:8080";
        }

        // 存在cookie
        if (cookie!=null) {
            // 获取已经缓存中登录的用户, 存在时跳转主页面
            User user = LoginCacheUtil.loginUser.get(cookie.getValue());
            if (user!=null) {
                return "redirect:" + target;
            }
        }

        session.setAttribute("target", target);

        return "/login";
    }


}
