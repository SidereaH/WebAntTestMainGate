package webant.maingate.controllers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import webant.maingate.models.AutoTest;
import webant.maingate.models.Test;
import webant.maingate.models.TestCase;
import webant.maingate.models.TestPriority;
import webant.maingate.services.TestService;

@RestController
@RequestMapping("/api/video-analysis")
public class VideoToGhercin {



        private final String VIDEO_ANALYSIS_SERVICE_URL = "http://localhost:8084";
        private final RestTemplate restTemplate;
    private final TestService testService;

    public VideoToGhercin(RestTemplateBuilder restTemplate, TestService testService) {
            this.restTemplate = restTemplate.build();
        this.testService = testService;
    }

        @PostMapping("/upload-video-base64")
        public ResponseEntity<String> uploadVideoBase64(@RequestBody GherkinRequest request) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<VideoUploadRequest> entity = new HttpEntity<>(new VideoUploadRequest( request.getVideo_base64()), headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        VIDEO_ANALYSIS_SERVICE_URL + "/upload-video-base64",
                        HttpMethod.POST,
                        entity,
                        String.class
                );
                var newTest = new Test();
                var newTestCase = new TestCase(
                        request.getDescription(),
                        request.getDescription(),
                        response.getBody(),
                        TestPriority.CRITICAL
                );

                newTest.setTestCase(
                        newTestCase
                );
                newTest.setAutoTest(new AutoTest("null"));
                newTest.setName(request.getDescription());
                newTest.setDescription(request.getDescription());

                testService.save(newTest , request.getProjectId());

                return ResponseEntity.ok(response.getBody());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"Failed to upload video: " + e.getMessage() + "\"}");
            }
        }

        @GetMapping("/get-test-cases")
        public ResponseEntity<String> getTestCases() {
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        VIDEO_ANALYSIS_SERVICE_URL + "/get-test-cases",
                        HttpMethod.GET,
                        null,
                        String.class
                );

                return ResponseEntity.ok(response.getBody());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"Failed to get test cases: " + e.getMessage() + "\"}");
            }
        }
        @AllArgsConstructor
        @Getter
        @Setter
        public static class VideoUploadRequest {
            private String video_base64;

            public String getVideo_base64() {
                return video_base64;
            }

            public void setVideo_base64(String video_base64) {
                this.video_base64 = video_base64;
            }
        }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GherkinRequest {
        private String description;
        private String image_type;
        private Long projectId;
        private String video_base64;

    }

}
