package webant.maingate.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_case_id")
    private TestCase testCase;
    @ManyToOne(fetch = FetchType.EAGER)
    private Project project;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "auto_test_id")
    private AutoTest autoTest;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}