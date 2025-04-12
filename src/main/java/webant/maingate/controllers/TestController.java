package webant.maingate.controllers;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webant.maingate.models.Test;
import webant.maingate.services.TestService;

@RestController
@RequestMapping("/api/tests")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public Page<Test> getAllTests(Pageable pageable) {
        return testService.findAll(pageable);
    }

    @GetMapping("/project/{projectId}")
    public Page<Test> getTestsByProject(@PathVariable Long projectId, Pageable pageable) {
        return testService.findByProjectId(projectId, pageable);
    }

    @PostMapping
    public Test createTest(@Valid @RequestBody Test test) {
        return testService.save(test);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Test> getTest(@PathVariable Long id) {
        return ResponseEntity.of(testService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable Long id, @Valid @RequestBody Test test) {
        return ResponseEntity.of(testService.update(id, test));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}