package webant.maingate.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import webant.maingate.models.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
    Page<Test>  findByProjectId(Long id,Pageable pageable);
}
