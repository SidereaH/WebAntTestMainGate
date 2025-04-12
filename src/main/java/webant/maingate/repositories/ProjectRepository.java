package webant.maingate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webant.maingate.models.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
