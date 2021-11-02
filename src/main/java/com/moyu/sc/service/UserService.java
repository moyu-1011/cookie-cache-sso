package com.moyu.sc.service;

import com.moyu.sc.domain.Auth;
import com.moyu.sc.domain.Role;
import com.moyu.sc.domain.User;
import com.moyu.sc.domain.dto.LoginDto;
import com.moyu.sc.domain.dto.UserDto;
import com.moyu.sc.repository.RoleRepo;
import com.moyu.sc.repository.UserRepo;
import com.moyu.sc.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 登录。返回Auth对象, 包含token及refresh_token
     *
     * @param loginDto loginDto
     * @return auth
     * @throws AuthenticationException 认证异常
     */
    public Auth login(LoginDto loginDto) throws AuthenticationException {
        return userRepo.findByUsername(loginDto.getUsername())
                .filter(user -> passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
                .map(user -> new Auth(
                        jwtUtil.createAccessToken(user),
                        jwtUtil.createRefreshToken(user)
                ))
                .orElseThrow(() -> new BadCredentialsException("用户名密码错误"));
    }


    /**
     * 用户注册
     *
     * @param userDto userDto
     * @return user
     */
    public User register(UserDto userDto) {
        Role role = Role.builder()
                .authority("ROLE_USER")
                .username(userDto.getUsername())
                .build();
        Set<Role> roleSet = Collections.singleton(roleRepo.save(role));
        return userRepo.save(User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .authorities(roleSet)
                .build());
    }


    public Auth refreshToken(String authorization, String refreshToken) {

        return null;
    }
}
