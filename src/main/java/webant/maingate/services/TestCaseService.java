package webant.maingate.services;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webant.maingate.models.Project;
import webant.maingate.models.TestCase;
import webant.maingate.repositories.ProjectRepository;
import webant.maingate.repositories.TestCaseRepository;

import java.util.Optional;

@Service
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;
    private final ProjectRepository projectRepository;

    public TestCaseService(TestCaseRepository testCaseRepository, ProjectRepository projectRepository) {
        this.testCaseRepository = testCaseRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public Page<TestCase> findAll(Pageable pageable) {
        return testCaseRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<TestCase> findByProjectId(Long projectId, Pageable pageable) {
        return testCaseRepository.findByProjectId(projectId, pageable);
    }

    @Transactional
    public TestCase save(TestCase testCase) {
        Project project = projectRepository.findById(testCase.getProject().getId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        testCase.setProject(project);
        return testCaseRepository.save(testCase);
    }

    @Transactional(readOnly = true)
    public Optional<TestCase> findById(Long id) {
        return testCaseRepository.findById(id);
    }

    @Transactional
    public Optional<TestCase> update(Long id, TestCase testCase) {
        return testCaseRepository.findById(id)
                .map(existing -> {
                    testCase.setId(id);
                    return testCaseRepository.save(testCase);
                });
    }

    @Transactional
    public void deleteById(Long id) {
        testCaseRepository.deleteById(id);
    }
}