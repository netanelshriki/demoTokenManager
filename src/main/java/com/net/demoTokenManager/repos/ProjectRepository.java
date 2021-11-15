package com.net.demoTokenManager.repos;

import com.net.demoTokenManager.beans.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Integer> {
    Project findByProjectName(String name);
}
