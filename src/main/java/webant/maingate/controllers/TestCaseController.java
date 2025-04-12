package webant.maingate.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webant.maingate.models.TestCase;
import webant.maingate.services.TestCaseService;

@RestController
@RequestMapping("/api/test-cases")
public class TestCaseController {
    private final TestCaseService testCaseService;

    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @GetMapping
    public Page<TestCase> getAllTestCases(Pageable pageable) {
        return testCaseService.findAll(pageable);
    }

    @GetMapping("/project/{projectId}")
    public Page<TestCase> getTestCasesByProject(@PathVariable Long projectId, Pageable pageable) {
        return testCaseService.findByProjectId(projectId, pageable);
    }

    @PostMapping
    public TestCase createTestCase(@Valid @RequestBody TestCase testCase) {
        return testCaseService.save(testCase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCase> getTestCase(@PathVariable Long id) {
        return ResponseEntity.of(testCaseService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestCase> updateTestCase(@PathVariable Long id, @Valid @RequestBody TestCase testCase) {
        return ResponseEntity.of(testCaseService.update(id, testCase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long id) {
        testCaseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}