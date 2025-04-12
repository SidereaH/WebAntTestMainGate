package webant.maingate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webant.maingate.models.AutoTest;

public interface AutoTestRepository extends JpaRepository<AutoTest, Long> {
}
