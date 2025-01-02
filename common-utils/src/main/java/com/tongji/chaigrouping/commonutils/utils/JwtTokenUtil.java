package com.tongji.chaigrouping.commonutils.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public int tryParseToken(String token) throws JwtException {
        JwtParser parser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build();
        return parser.parseSignedClaims(token).getPayload().get("userId", Integer.class);
    }

    public String generateToken(Integer userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
