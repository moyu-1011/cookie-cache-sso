package com.moyu.sc.utils;

import com.moyu.sc.domain.Role;
import com.moyu.sc.domain.User;
import io.jsonwebtoken.Jwts;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class JwtUtilTest {

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void giveUserDetails_thenCreateTokenSuccess() {
        var username = "user";
        var authorities = Sets.newSet(Role.builder()
                        .authority("ROLE_USER")
                        .build(),
                Role.builder()
                        .authority("ROLE_ADMIN")
                        .build());
        var user = User.builder()
                .username(username)
                .authorities(authorities)
                .build();
        var token = jwtUtil.createAccessToken(user);
        System.out.println(token);
        var body = Jwts.parserBuilder()
                .setSigningKey(jwtUtil.getAccessKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        assertEquals(username, body.getSubject(), "jwt解析错误: unexpected username");
    }


}
