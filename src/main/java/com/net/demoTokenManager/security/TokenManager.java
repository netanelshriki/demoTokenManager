package com.net.demoTokenManager.security;

import com.net.demoTokenManager.beans.Project;
import com.net.demoTokenManager.beans.User;
import com.net.demoTokenManager.repos.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TokenManager {
    private Map<String, Information> tokens = new HashMap<>();

    @Autowired
    private ProjectRepository projectRepository;

    public String generateToken(User user) {

        String token = UUID.randomUUID().toString();

        Information info = Information.builder()
                .id(user.getId())
                .name(user.getFirstName())
                .lastName(user.getLastName())
                .timeStamp(LocalDateTime.now())
                .build();

        tokens.put(token, info);
        return token;
    }

    public Information getInfoFromToken(String token) {
        return tokens.get(token);
    }

    public boolean isAllowed(Project project, String token) {
        for (User user : project.getAdmins()) {
            if (user.getId() == tokens.get(token).getId()) {
                return true;
            }
        }
        return false;
    }

}
