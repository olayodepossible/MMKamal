package com.possible.mmk.gatewayserver.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

/**
 *
 * @author Abayomi
 */

@Component
@Slf4j
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        log.info("REAHER HERE3 ******************");
        Claims claims = null;
        try{
            claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }
        catch (ExpiredJwtException e) {
            log.info(" Token expired ");
        } catch (SignatureException e) {
            log.info("ERR_____> {}", e.getMessage());
        } catch(Exception e){
            log.info(" Some other exception in JWT parsing ");
        }
        return claims;
    }

    private boolean isTokenExpired(String token) {
        log.info("REAHER HERE2 ******************");
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean isInvalid(String token) {
        log.info("REAHER HERE1 ******************");
        return this.isTokenExpired(token);
    }

}