package webant.maingate.controllers;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import webant.maingate.models.*;
import webant.maingate.services.TestService;

@RestController
@RequestMapping("/api/git-analysis")
public class GitAnalysisController {

    private final RestTemplate restTemplate;

    private final String gitAnalysisServiceUrl;
    private final TestService testService;

    public GitAnalysisController(RestTemplateBuilder restTemplateBuilder, @Value("${git-analysis.service.url}") String gitAnalysisServiceUrl, TestService testService) {
        this.restTemplate = restTemplateBuilder.build();
        this.gitAnalysisServiceUrl = gitAnalysisServiceUrl;
        this.testService = testService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<GitAnalysisResponse> analyzeGitRepo(@RequestBody GitReview request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GitAnalysisRequest> entity = new HttpEntity<>(new GitAnalysisRequest(request.getRepoUrl()), headers);

        ResponseEntity<GitAnalysisResponse> response = restTemplate.exchange(
                gitAnalysisServiceUrl,
                HttpMethod.POST,
                entity,
                GitAnalysisResponse.class
        );
        var newTest = new Test();
        var autoTest = new AutoTest(
                request.getName(),
                response.getBody().getReview(),
                response.getBody().getGenerated_tests(),
                TestFramework.SELENIUM
        );
        newTest.setAutoTest(autoTest);
        newTest.setTestCase(
                new TestCase(
                        "null"
                )
        );
        newTest.setName(request.getName());
        newTest.setDescription(request.getDescription());


        testService.save( newTest, request.getProjectId());


        return ResponseEntity.ok(response.getBody());
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GitReview{
        private String name;
        private String description;
        private Long projectId;
        private String priority;
        private String repoUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GitAnalysisRequest {
        private String repo_url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GitAnalysisResponse {
        private String summary;
        private String generated_tests;
        private String review;
    }
}