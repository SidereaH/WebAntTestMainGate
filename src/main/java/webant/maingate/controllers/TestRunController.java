package webant.maingate.controllers;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webant.maingate.models.TestRun;
import webant.maingate.services.TestRunService;

@RestController
@RequestMapping("/api/test-runs")
public class TestRunController {
    private final TestRunService testRunService;

    public TestRunController(TestRunService testRunService) {
        this.testRunService = testRunService;
    }

    @GetMapping
    public Page<TestRun> getAllTestRuns(Pageable pageable) {
        return testRunService.findAll(pageable);
    }

    @GetMapping("/test/{testId}")
    public Page<TestRun> getTestRunsByTest(@PathVariable Long testId, Pageable pageable) {
        return testRunService.findByTestId(testId, pageable);
    }

    @PostMapping
    public TestRun createTestRun(@Valid @RequestBody TestRun testRun) {
        return testRunService.save(testRun);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestRun> getTestRun(@PathVariable Long id) {
        return ResponseEntity.of(testRunService.findById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TestRun> updateTestRunStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.of(testRunService.updateStatus(id, status));
    }
}
