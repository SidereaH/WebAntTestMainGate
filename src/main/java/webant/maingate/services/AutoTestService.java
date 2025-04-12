package webant.maingate.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webant.maingate.models.AutoTest;
import webant.maingate.repositories.AutoTestRepository;

import java.util.Optional;

@Service
public class AutoTestService {
    private final AutoTestRepository autoTestRepository;

    public AutoTestService(AutoTestRepository autoTestRepository) {
        this.autoTestRepository = autoTestRepository;
    }

    @Transactional(readOnly = true)
    public Page<AutoTest> findAll(Pageable pageable) {
        return autoTestRepository.findAll(pageable);
    }

    @Transactional
    public AutoTest save(AutoTest autoTest) {
        return autoTestRepository.save(autoTest);
    }

    @Transactional(readOnly = true)
    public Optional<AutoTest> findById(Long id) {
        return autoTestRepository.findById(id);
    }

    @Transactional
    public Optional<AutoTest> update(Long id, AutoTest autoTest) {
        return autoTestRepository.findById(id)
                .map(existing -> {
                    autoTest.setId(id);
                    return autoTestRepository.save(autoTest);
                });
    }

    @Transactional
    public void deleteById(Long id) {
        autoTestRepository.deleteById(id);
    }
}