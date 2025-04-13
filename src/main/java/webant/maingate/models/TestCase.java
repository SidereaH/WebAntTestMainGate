package webant.maingate.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import webant.maingate.models.TestPriority;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_cases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String gherkinCode;

    @Enumerated(EnumType.STRING)
    private TestPriority priority;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public TestCase(String name, String description, String gherkinCode, TestPriority priority) {
        this.name = name;
        this.description = description;
        this.gherkinCode = gherkinCode;
        this.priority = priority;

    }

    public TestCase(String name) {
        this.name = name;
    }
}


