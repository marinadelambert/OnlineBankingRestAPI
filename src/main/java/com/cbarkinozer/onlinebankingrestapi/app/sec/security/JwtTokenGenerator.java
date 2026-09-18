package com.cbarkinozer.onlinebankingrestapi.app.sec.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenGenerator {

    private static final int MIN_KEY_BYTES = 64;

    private final SecretKey signingKey;
    private final JwtParser jwtParser;
    private final Long expireTime;

    public JwtTokenGenerator(@Value("${onlinebankingrestapi.jwt.security.app.key}") String appKey,
                             @Value("${onlinebankingrestapi.jwt.security.expire.time}") Long expireTime) {
        this.signingKey = buildSigningKey(appKey);
        this.jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build();
        this.expireTime = expireTime;
    }

    private static SecretKey buildSigningKey(String appKey) {
        if (appKey == null || appKey.isBlank()) {
            throw new IllegalStateException("JWT signing key is not configured (set JWT_APP_KEY)");
        }
        byte[] keyBytes = Decoders.BASE64.decode(appKey);
        if (keyBytes.length < MIN_KEY_BYTES) {
            throw new IllegalStateException("JWT signing key must be at least " + MIN_KEY_BYTES
                    + " bytes (512 bits) for HS512, got " + keyBytes.length);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(Authentication authentication){

        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + expireTime);

        String token = Jwts.builder()
                .setSubject(Long.toString(jwtUserDetails.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        return token;
    }

    public Long findUserIdByToken(String token){

        Jws<Claims> claimsJws = parseToken(token);

        String userIdStr = claimsJws
                .getBody()
                .getSubject();

        return Long.parseLong(userIdStr);
    }

    private Jws<Claims> parseToken(String token) {
        return jwtParser.parseClaimsJws(token);
    }

    public boolean validateToken(String token){

        boolean isValid;

        try {
            Jws<Claims> claimsJws = parseToken(token);

            isValid = !isTokenExpired(claimsJws);
        } catch (Exception e){
            isValid = false;
        }

        return isValid;
    }

    private boolean isTokenExpired(Jws<Claims> claimsJws) {

        Date expirationDate = claimsJws.getBody().getExpiration();

        return expirationDate.before(new Date());
    }
}
