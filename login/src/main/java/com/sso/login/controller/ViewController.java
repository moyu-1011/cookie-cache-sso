package com.sso.login.controller;

import com.example.cache.utils.RedisUtil;
import com.sso.login.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@RequestMapping(value = "/view")
@Controller
public class ViewController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 登录页
    @GetMapping(value = "/login")
    public String toLogin(@RequestParam(value = "target", required = false) String target,
                          @CookieValue(value = "COOKIE_ID", required = false) Cookie cookie,
                          HttpSession session) {
        // 判断登录后返回地址, 为空时返回首页
        if (StringUtils.isEmpty(target)) {
            target = "localhost:8080";
        }

        // 存在cookie
        if (cookie != null) {
            // 如果缓存命中,直接登录
            RedisUtil redisUtil = new RedisUtil();
            redisUtil.setRedisTemplate(redisTemplate);
            if (redisUtil.keyExists(cookie.getValue())) {
                // 更新缓存过期时间
                redisUtil.resetExpires(cookie.getValue(), 30, TimeUnit.MINUTES);
                return "redirect:" + target;
            }
        }

        session.setAttribute("target", target);

        return "/login";
    }


}
