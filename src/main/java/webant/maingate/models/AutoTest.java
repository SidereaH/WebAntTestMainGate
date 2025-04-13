// AutoTest.java
package webant.maingate.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "auto_tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    private String description;

    @Column(columnDefinition = "TEXT")
    @Lob
    private String testCode;

    @Enumerated(EnumType.STRING)
    private TestFramework framework;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public AutoTest(String name, String description, String testCode, TestFramework framework) {
        this.name = name;
        this.description = description;
        this.testCode = testCode;
        this.framework = framework;
    }

    public AutoTest(String name) {
        this.name = name;
    }
}

