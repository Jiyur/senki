package com.abc.senki.security.jwt;

import com.abc.senki.security.dto.AppUserDetail;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private static final String ROLE_PERMISSION="rolePermissions";
    @Value("${apps.security.secret}")
    private String jwtSecret;

    @Value("${apps.security.header-string}")
    private String headerString;
    @Value("${apps.security.token-prefix}")
    private String tokenPrefix;
    @Value("${apps.security.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${apps.security.header-string}")
    private String authorizationHeader;
    @Value("${apps.security.refreshJwtExpirationMs}")
    private int refreshJwtExpirationMs;
    public String generateJwtToken(AppUserDetail userPrincipal) {
        Algorithm algorithm = getAlgorithm();

        return JWT.create()
                .withSubject(userPrincipal.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis()+ jwtExpirationMs))
                .withClaim("roleNames", new ArrayList<>(userPrincipal.getRoles()))
                .withClaim(ROLE_PERMISSION,userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);

    }
    public Collection<SimpleGrantedAuthority> getAuthoritiesFromJwtToken(String token) {
        String tempRolesString = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody().get(ROLE_PERMISSION).toString();
        String[] roles =tempRolesString.substring(1,tempRolesString.length()-1).replace(" ","").split(",");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (String role: roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }


    public boolean validateJwtToken(String authToken) throws ExpiredJwtException {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(authToken.replace(tokenPrefix, ""))
                    .getBody()
                    .getSubject();
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
    public String getUserNameFromJwtToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return  jwt.getSubject();
    }
    public String generateRefreshJwtToken(AppUserDetail userPrincipal){
        Algorithm algorithm = getAlgorithm();
        return JWT.create()
                .withSubject(userPrincipal.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis()+ refreshJwtExpirationMs))
                .withClaim("roleNames", new ArrayList<>(userPrincipal.getRoles()))
                .withClaim(ROLE_PERMISSION,userPrincipal.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
    }
    public boolean validateExpiredToken(String authToken) {
        DecodedJWT jwt = JWT.decode(authToken);
        return  jwt.getExpiresAt().before(new Date());
    }
    public String generateEmailJwtToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis()+ 600000))
                .sign(algorithm);
    }
    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtSecret.getBytes());
    }


}
