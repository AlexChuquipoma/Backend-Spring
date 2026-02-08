package com.portfolio.backend.repository;

import com.portfolio.backend.entity.Project;
import com.portfolio.backend.entity.enums.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerId(Long ownerId);

    List<Project> findByType(ProjectType type);

    List<Project> findByOwnerIdAndType(Long ownerId, ProjectType type);
}
