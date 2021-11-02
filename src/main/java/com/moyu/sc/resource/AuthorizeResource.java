package com.moyu.sc.resource;

import com.moyu.sc.domain.Auth;
import com.moyu.sc.domain.User;
import com.moyu.sc.domain.dto.LoginDto;
import com.moyu.sc.domain.dto.UserDto;
import com.moyu.sc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authorize")
public class AuthorizeResource {

    private final UserService userService;

    @PostMapping("/token")
    public Auth token(@Valid @RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserDto userDto) {
        return userService.register(userDto);
    }

    @PostMapping("/token/refresh")
    public Auth refresh(@RequestHeader String authorization, @RequestParam String refreshToken) {


        return null;
    }

}
