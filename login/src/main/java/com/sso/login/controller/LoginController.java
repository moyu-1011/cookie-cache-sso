package com.sso.login.controller;

import com.example.cache.utils.RedisUtil;
import com.sso.login.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final HashSet<User> dbUser ;

    static {
        dbUser = new HashSet<>();
        dbUser.add(new User(1L,"猪", "123456"));
        dbUser.add(new User(2L,"牛", "123456"));
        dbUser.add(new User(3L,"羊", "123456"));
    }

    /**
     * 登录
     * @param user
     * @param response
     * @param session
     * @return
     */
    @PostMapping
    public String doLogin(User user, HttpServletResponse response, HttpSession session) {
        // 模拟从数据库查找用户
        Optional<User> loginAccount = dbUser.stream().filter(dbUser -> dbUser.getUserName().equals(user.getUserName()) &&
                dbUser.getPassword().equals(user.getPassword())).findFirst();

        if (loginAccount.isPresent()) {
            // 登录成功, 添加cookie并缓存用户
            String token = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("COOKIE_ID", token); // 定义cookie
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            RedisUtil redisUtil = new RedisUtil();  // 添加缓存
            redisUtil.setRedisTemplate(redisTemplate);
            redisUtil.set(token, user, 30, TimeUnit.MINUTES);   // 过期时间30分钟

        }else {
            session.setAttribute("msg", "账号密码错误");
            return "login";
        }

        // 登录后重定向地址
        String target = (String) session.getAttribute("target");
        return "redirect:" + target;
    }

    /**
     * 查询用户信息
     * @param token
     * @return
     */
    @GetMapping(value = "/info")
    public ResponseEntity<User> getUserInfo(String token) {
        if (token!=null) {
            RedisUtil redisUtil = new RedisUtil();
            redisUtil.setRedisTemplate(redisTemplate);
            User user = (User) redisUtil.get(token);
            return ResponseEntity.ok(user);

        }else {
            return ResponseEntity.ok((User) null);
        }
    }

    @GetMapping(value = "/logout")
    public String logout(String target, @CookieValue(value = "COOKIE_ID")Cookie cookie, HttpSession session) {
        // 清除缓存中的用户
        if (cookie!=null) {
            RedisUtil redisUtil = new RedisUtil();
            redisUtil.setRedisTemplate(redisTemplate);
            redisUtil.remove(cookie.getValue());
        }
        // 销毁session
        session.invalidate();
        return "redirect:" + target;
    }

}
