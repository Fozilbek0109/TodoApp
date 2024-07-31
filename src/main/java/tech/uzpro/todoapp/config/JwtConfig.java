package tech.uzpro.todoapp.config;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
public class JwtConfig {

    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public static String generateToken(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .setIssuer(new Date(System.currentTimeMillis()).toString())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key)
                .compact();
    }
}
