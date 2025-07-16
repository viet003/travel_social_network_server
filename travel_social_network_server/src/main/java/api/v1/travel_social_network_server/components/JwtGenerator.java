package api.v1.travel_social_network_server.components;

import api.v1.travel_social_network_server.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtGenerator {

    @Value("${jwt.secret.key}")
    private String secretKey;


    public String generateToken(User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("roles", roles)
                .withClaim("userId", String.valueOf(user.getUserId()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 864000000)) // Expires in 10 days
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String extractEmail(String token) throws SignatureVerificationException {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getSubject();
    }

    public int extractUserId(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getClaim("userId")
                .asInt();
    }

    public List<Role> extractRoles(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getClaim("roles")
                .asList(Role.class);
    }

    public boolean isValidToken(String token) {
        try {
            if (isTokenExpired(token)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private boolean isTokenExpired(String token) {
        // Check the expiration date of the token
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getExpiresAt()
                .before(new Date());
    }
}