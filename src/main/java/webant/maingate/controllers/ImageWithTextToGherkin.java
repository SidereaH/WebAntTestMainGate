package webant.maingate.controllers;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import webant.maingate.models.AutoTest;
import webant.maingate.models.Test;
import webant.maingate.models.TestCase;
import webant.maingate.models.TestPriority;
import webant.maingate.services.TestService;

@RestController
@RequestMapping("/api/image_gherkin")
public class ImageWithTextToGherkin {


        private final String GHERKIN_SERVICE_URL;
        private final RestTemplate restTemplate;
    private final TestService testService;

    public ImageWithTextToGherkin(RestTemplateBuilder restTemplate, @Value("${image-to-gherkin.service.url}") String gherkinServiceUrl, TestService testService) {
            this.restTemplate = restTemplate.build();
            this.GHERKIN_SERVICE_URL = gherkinServiceUrl;
        this.testService = testService;
    }

        @PostMapping()
        public ResponseEntity<String> generateGherkin(@RequestBody GherkinRequest request) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<GherkinRequest> entity = new HttpEntity<>(request, headers);

                ResponseEntity<ImageGherkinResponse> response = restTemplate.exchange(
                        GHERKIN_SERVICE_URL,
                        HttpMethod.POST,
                        entity,
                        ImageGherkinResponse.class
                );
                var newTest = new Test();
                var newTestCase = new TestCase(
                        request.getDescription(),
                        request.getDescription(),
                        response.getBody().getGherkin(),
                        TestPriority.CRITICAL
                );

                newTest.setTestCase(
                        newTestCase
                );
                newTest.setAutoTest(new AutoTest("null"));
                newTest.setName(request.getDescription());
                newTest.setDescription(request.getDescription());

                testService.save(newTest , request.getProjectId());

                return ResponseEntity.ok(response.getBody().getGherkin());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"Failed to process request: " + e.getMessage() + "\"}");
            }
        }
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class GherkinRequest {
            private String description;
            private String base64_image;
            private String image_type;
            private Long projectId;

        }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageGherkinResponse {
        private String gherkin;
    }
    }

