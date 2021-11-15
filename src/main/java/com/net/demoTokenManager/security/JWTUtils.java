package com.net.demoTokenManager.security;

import com.net.demoTokenManager.beans.User;
import com.net.demoTokenManager.repos.ProjectRepository;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Slf4j
public class JWTUtils {

    @Value("${cula.app.jwtSecret}")
    private String jwtSecret;

    @Value("${cula.app.addMoreSeconds}")
    private long addMoreSeconds;
    @Autowired
    private ProjectRepository projectRepository;

    public String generateToken(User user) {

        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("name", user.getFirstName());
        info.put("email", user.getEmail());

        return Jwts.builder().setExpiration(new Date((new Date()).getTime() + 259200000))
                .addClaims(info)
                .setIssuedAt(new Date((new Date()).getTime()))
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
    }

    public Claims getClaimFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).setAllowedClockSkewSeconds(addMoreSeconds).parseClaimsJws(token).getBody();
        Map<String, Object> map = claims;
        return claims;
    }

    public String getEmailFromToken(String token){
       Map<String,Object> map = getClaimFromToken(token);
       return map.get("email").toString();
    }


    public boolean isAllowed(String token, int projectId) {
        Map<String, Object> info = getClaimFromToken(token);
        int id = (int) info.get("id");
        for (User user : projectRepository.getOne(projectId).getAdmins()) {
            if (user.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }


}
