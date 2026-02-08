package com.portfolio.backend.projects.repository;

import com.portfolio.backend.projects.entity.Project;
import com.portfolio.backend.projects.entity.enums.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerId(Long ownerId);

    List<Project> findByType(ProjectType type);

    List<Project> findByOwnerIdAndType(Long ownerId, ProjectType type);
}
