package webant.maingate.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import webant.maingate.models.TestCase;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
//    Page<TestCase> findByProjectId(Long projectId, Pageable pageable);
}
