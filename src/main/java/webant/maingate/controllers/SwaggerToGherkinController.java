package webant.maingate.controllers;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import webant.maingate.models.AutoTest;
import webant.maingate.models.Test;
import webant.maingate.models.TestCase;
import webant.maingate.models.TestPriority;
import webant.maingate.services.TestService;

@RestController
@RequestMapping("/api/swagger")
public class SwaggerToGherkinController {

    private final RestTemplate restTemplate;
    private final String gherkinServiceUrl ;
    private final TestService testService;

    public SwaggerToGherkinController(RestTemplateBuilder restTemplateBuilder, @Value("${swagger-to-gherkin.service.url}") String gherkinServiceUrl, TestService testService) {
        this.restTemplate = restTemplateBuilder.build();
        this.gherkinServiceUrl = gherkinServiceUrl;
        this.testService = testService;
    }

    @PostMapping("/generate-gherkin")
    @PreAuthorize("hasAnyRole('DEVELOPER', 'ADMIN')")
    public ResponseEntity<String> generateGherkin(@RequestBody SwaggerGherkin request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SwaggerGherkinRequest> entity = new HttpEntity<>(new SwaggerGherkinRequest(request.getRepoUrl(), request.getFilePath()), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                gherkinServiceUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        var newTest = new Test();
        var newTestCase = new TestCase(
                request.getName(),
                request.getDescription(),
                response.getBody(),
                TestPriority.CRITICAL
        );

        newTest.setTestCase(
            newTestCase
        );
        newTest.setAutoTest(new AutoTest("null"));
        newTest.setName(request.getName());
        newTest.setDescription(request.getDescription());

        testService.save(newTest , request.getProjectId());

        return ResponseEntity.ok(response.getBody());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SwaggerGherkin{
        private String name;
        private String description;
        private Long projectId;
        private String priority;
        private String repoUrl;
        private String filePath;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SwaggerGherkinRequest {
        private String repoUrl;
        private String filePath;
    }

}