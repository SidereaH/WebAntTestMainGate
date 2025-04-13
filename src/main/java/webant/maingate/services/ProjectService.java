package webant.maingate.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webant.maingate.models.Project;
import webant.maingate.models.security.User;
import webant.maingate.repositories.ProjectRepository;
import webant.maingate.repositories.UserRepository;

import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Transactional
    public Project save(Project project) {
        User owner = userRepository.findByUsername(project.getOwner().getUsername()).orElse(null);
        project.setOwner(owner);
        project.addMember(project.getOwner());
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public Optional<Project> update(Long id, Project project) {
        return projectRepository.findById(id)
                .map(existing -> {
                    project.setId(id);
                    return projectRepository.save(project);
                });
    }

    @Transactional
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public void addMember(Long projectId, Long userId) {
        projectRepository.findById(projectId).ifPresent(project -> {
            userRepository.findById(userId).ifPresent(user -> {
                project.addMember(user);
                projectRepository.save(project);
            });
        });
    }

    @Transactional
    public void removeMember(Long projectId, Long userId) {
        projectRepository.findById(projectId).ifPresent(project -> {
            userRepository.findById(userId).ifPresent(user -> {
                project.removeMember(user);
                projectRepository.save(project);
            });
        });
    }
}