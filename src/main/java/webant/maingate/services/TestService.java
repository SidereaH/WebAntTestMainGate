package webant.maingate.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webant.maingate.models.Project;
import webant.maingate.models.Test;
import webant.maingate.repositories.ProjectRepository;
import webant.maingate.repositories.TestRepository;

import java.util.Optional;

@Service
public class TestService {
    private final TestRepository testRepository;
    private final ProjectRepository projectRepository;
    private final AutoTestService autoTestService;
    private final TestCaseService testCaseService;


    public TestService(TestRepository testRepository, ProjectRepository projectRepository, AutoTestService autoTestService, TestCaseService testCaseService) {
        this.testRepository = testRepository;
        this.projectRepository = projectRepository;
        this.autoTestService = autoTestService;
        this.testCaseService = testCaseService;
    }

    @Transactional(readOnly = true)
    public Page<Test> findAll(Pageable pageable) {
        return testRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Test> findByProjectId(Long projectId, Pageable pageable) {
        return testRepository.findByProjectId(projectId, pageable);
    }

    @Transactional
    public Test save(Test test, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        test.setProject(project);
        autoTestService.save(test.getAutoTest());
        testCaseService.save(test.getTestCase());
        testRepository.save(test);

        return test;
    }

    @Transactional(readOnly = true)
    public Optional<Test> findById(Long id) {
        return testRepository.findById(id);
    }

    @Transactional
    public Optional<Test> update(Long id, Test test) {
        return testRepository.findById(id)
                .map(existing -> {
                    test.setId(id);
                    return testRepository.save(test);
                });
    }

    @Transactional
    public void deleteById(Long id) {
        testRepository.deleteById(id);
    }
}