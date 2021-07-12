package com.cybersecurity.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cybersecurity.config.TokenConfig;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@NoArgsConstructor
public class JWTService {
    public String generateToken(String username) throws Exception{
        return TokenConfig.TOKEN_PREFIX+ JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis()+ TokenConfig.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(TokenConfig.SECRET.getBytes()));
    }
    public String decode(String token){
        try {
            return JWT.require(Algorithm.HMAC512(TokenConfig.SECRET.getBytes()))
                    .build().verify(token.replace(TokenConfig.TOKEN_PREFIX,""))
                    .getSubject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
