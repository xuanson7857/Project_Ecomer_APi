package com.ra.security.jwt;

import com.ra.security.principal.M5UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${expired}")
    private Long EXPIRED;
    @Value("${secret_key}")
    private String SECRET_KEY;
    private final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
    public String generateAccessToken(M5UserPrincipal userPrincipal){
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRED))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }
    public Boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException expired){
            logger.error("Expired Token {}",expired.getMessage());
        }catch (MalformedJwtException malformedJwtException){
            logger.error("Invalid Format {}",malformedJwtException.getMessage());
        }catch (UnsupportedJwtException unsupportedJwtException){
            logger.error("Unsupported token {}" ,unsupportedJwtException.getMessage());
        }catch (SignatureException signatureException) {
            logger.error("Invalid Signature {}" , signatureException.getMessage());
        }
        return false;
    }
    public String getUsernameToken(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
