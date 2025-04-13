package webant.maingate.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/swagger")
public class SwaggerToGherkinController {

    private final RestTemplate restTemplate;
    private final String gherkinServiceUrl ;
    public SwaggerToGherkinController(RestTemplateBuilder restTemplateBuilder, @Value("${swagger-to-gherkin.service.url}") String gherkinServiceUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.gherkinServiceUrl = gherkinServiceUrl;
    }

    @PostMapping("/generate-gherkin")
    @PreAuthorize("hasAnyRole('DEVELOPER', 'ADMIN')")
    public ResponseEntity<String> generateGherkin(@RequestBody SwaggerGherkinRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SwaggerGherkinRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                gherkinServiceUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return ResponseEntity.ok(response.getBody());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SwaggerGherkinRequest {
        private String repoUrl;
        private String filePath;
    }
}