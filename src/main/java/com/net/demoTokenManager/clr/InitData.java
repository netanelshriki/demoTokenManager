package com.net.demoTokenManager.clr;

import com.net.demoTokenManager.beans.Project;
import com.net.demoTokenManager.beans.User;
import com.net.demoTokenManager.repos.ProjectRepository;
import com.net.demoTokenManager.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Order
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    @Override
    public void run(String... args) throws Exception {

        Project project1 = Project.builder()
                .projectName("project 1")
                .build();

        User user1 = User.builder()
                .firstName("user 1")
                .email("1@gmail.com")
                .lastName("1")
                .build();


        userRepository.save(user1);

        projectRepository.save(project1);

        Project projectToAdd = projectRepository.findByProjectName("project 1");

        User userToAdd= userRepository.findByEmail("1@gmail.com");

        projectToAdd.setAdmins(Arrays.asList(userToAdd));

        projectRepository.saveAndFlush(projectToAdd);



    }
}
