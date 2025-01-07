package com.movie.tdmb.security.jwt;
import com.movie.tdmb.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generate a JWT token based on the provided authentication.
     *
     * @param authentication The authentication object containing user details.
     * @return The generated JWT token as a string.
     */
    public String generateJwtToken(Authentication authentication) {
        // Get the user details from the authentication object
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        // Build and return the JWT token
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .claim("passwordChangedAt", userPrincipal.getPasswordChangedAt())
                .claim("userID", userPrincipal.getId())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Create a signing key from the JWT secret.
     *
     * @return The signing key as a Key object.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extract the username from the given JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
    public String getIdFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().get("userID", String.class);
    }
    public Date getPasswordChangedAtFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().get("passwordChangedAt", Date.class);
    }
    /**
     * Validate the given JWT token.
     *
     * @param authToken The JWT token to validate.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            // Parse the token and verify its signature
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true; // Token is valid
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage()); // Log invalid token error
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage()); // Log expired token error
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage()); // Log unsupported token error
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage()); // Log empty claims error
        }
        return false; // Token is invalid
    }
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
}
