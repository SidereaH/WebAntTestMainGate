package webant.maingate.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webant.maingate.models.Test;
import webant.maingate.models.TestRun;
import webant.maingate.models.TestStatus;
import webant.maingate.models.security.User;
import webant.maingate.repositories.TestRepository;
import webant.maingate.repositories.TestRunRepository;
import webant.maingate.repositories.UserRepository;

import java.util.Optional;

@Service
public class TestRunService {
    private final TestRunRepository testRunRepository;
    private final TestRepository testRepository;
    private final UserRepository userRepository;

    public TestRunService(TestRunRepository testRunRepository,
                          TestRepository testRepository,
                          UserRepository userRepository) {
        this.testRunRepository = testRunRepository;
        this.testRepository = testRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<TestRun> findAll(Pageable pageable) {
        return testRunRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<TestRun> findByTestId(Long testId, Pageable pageable) {
        return testRunRepository.findByTestId(testId, pageable);
    }

    @Transactional
    public TestRun save(TestRun testRun) {
        Test test = testRepository.findById(testRun.getTest().getId())
                .orElseThrow(() -> new IllegalArgumentException("Test not found"));
        testRun.setTest(test);

        if (testRun.getExecutedBy() != null) {
            User user = userRepository.findById(testRun.getExecutedBy().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            testRun.setExecutedBy(user);
        }

        return testRunRepository.save(testRun);
    }

    @Transactional(readOnly = true)
    public Optional<TestRun> findById(Long id) {
        return testRunRepository.findById(id);
    }

    @Transactional
    public Optional<TestRun> updateStatus(Long id, String status) {
        return testRunRepository.findById(id)
                .map(testRun -> {
                    testRun.setStatus(TestStatus.valueOf(status));
                    return testRunRepository.save(testRun);
                });
    }
}