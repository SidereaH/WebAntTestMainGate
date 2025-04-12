package webant.maingate.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import webant.maingate.models.TestRun;

public interface TestRunRepository extends JpaRepository<TestRun, Long> {
    Page<TestRun> findByTestId(Long testId, Pageable pageable);
}
