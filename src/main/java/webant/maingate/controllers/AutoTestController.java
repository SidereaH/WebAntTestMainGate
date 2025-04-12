package webant.maingate.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webant.maingate.models.AutoTest;
import webant.maingate.services.AutoTestService;

@RestController
@RequestMapping("/api/auto-tests")
public class AutoTestController {
    private final AutoTestService autoTestService;

    public AutoTestController(AutoTestService autoTestService) {
        this.autoTestService = autoTestService;
    }

    @GetMapping
    public Page<AutoTest> getAllAutoTests(Pageable pageable) {
        return autoTestService.findAll(pageable);
    }

    @PostMapping
    public AutoTest createAutoTest(@Valid @RequestBody AutoTest autoTest) {
        return autoTestService.save(autoTest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoTest> getAutoTest(@PathVariable Long id) {
        return ResponseEntity.of(autoTestService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutoTest> updateAutoTest(@PathVariable Long id, @Valid @RequestBody AutoTest autoTest) {
        return ResponseEntity.of(autoTestService.update(id, autoTest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutoTest(@PathVariable Long id) {
        autoTestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}