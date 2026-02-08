package com.portfolio.backend.projects.repository;

import com.portfolio.backend.projects.entity.Project;
import com.portfolio.backend.projects.entity.enums.ProjectType;
import com.portfolio.backend.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio para la entidad Project
 * 
 * Extiende JpaRepository para heredar operaciones CRUD básicas:
 * - save(), findById(), findAll(), deleteById(), etc.
 * 
 * Además define queries personalizadas para búsquedas específicas.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Buscar proyectos por usuario (entidad User)
     * 
     * Query derivado: Spring genera automáticamente el SQL
     * SELECT * FROM projects WHERE owner_id = ?
     * 
     * @param owner Entidad User
     * @return Lista de proyectos del usuario
     */
    List<Project> findByOwner(User owner);

    /**
     * Buscar proyectos por ID de usuario
     * 
     * Query derivado: WHERE owner_id = ?
     * 
     * @param ownerId ID del usuario
     * @return Lista de proyectos
     */
    List<Project> findByOwnerId(Long ownerId);

    /**
     * Buscar proyectos por tipo
     * 
     * Query derivado: WHERE type = ?
     * 
     * @param type Tipo de proyecto (WEB, MOBILE, DESKTOP, etc.)
     * @return Lista de proyectos del tipo especificado
     */
    List<Project> findByType(ProjectType type);

    /**
     * Buscar proyectos por usuario Y tipo
     * 
     * Query derivado: WHERE owner_id = ? AND type = ?
     * 
     * @param ownerId ID del usuario
     * @param type    Tipo de proyecto
     * @return Lista de proyectos filtrados
     */
    List<Project> findByOwnerIdAndType(Long ownerId, ProjectType type);

    /**
     * Buscar proyectos que contengan una tecnología específica
     * 
     * @Query personalizada: Hace JOIN con la tabla project_techs
     *        para buscar en la lista de tecnologías (ElementCollection)
     * 
     * @param tech Nombre de la tecnología (ej: "React", "Spring Boot")
     * @return Lista de proyectos que usan esa tecnología
     */
    @Query("SELECT p FROM Project p JOIN p.techs t WHERE t = :tech")
    List<Project> findByTech(@Param("tech") String tech);
}
