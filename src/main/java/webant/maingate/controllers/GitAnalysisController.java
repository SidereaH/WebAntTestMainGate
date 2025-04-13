package webant.maingate.controllers;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/git-analysis")
public class GitAnalysisController {

    private final RestTemplate restTemplate;

    private final String gitAnalysisServiceUrl;

    public GitAnalysisController(RestTemplateBuilder restTemplateBuilder,@Value("${git-analysis.service.url}") String gitAnalysisServiceUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.gitAnalysisServiceUrl = gitAnalysisServiceUrl;
    }

    @PostMapping("/analyze")
    public ResponseEntity<GitAnalysisResponse> analyzeGitRepo(@RequestBody GitAnalysisRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GitAnalysisRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<GitAnalysisResponse> response = restTemplate.exchange(
                gitAnalysisServiceUrl,
                HttpMethod.POST,
                entity,
                GitAnalysisResponse.class
        );

        return ResponseEntity.ok(response.getBody());
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