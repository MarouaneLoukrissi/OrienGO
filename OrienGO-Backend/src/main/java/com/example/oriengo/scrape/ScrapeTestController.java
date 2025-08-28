package com.example.oriengo.scrape;

import com.example.oriengo.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personalized-job/scrape")
@RequiredArgsConstructor
@Slf4j
public class ScrapeTestController {

    private final RestTemplate restTemplate;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<List<Map>>> previewScrape(@RequestParam("title") String title) {
        try {
            String url = "http://localhost:5000/jobs/search?search_term=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
            ResponseEntity<Map[]> response = restTemplate.getForEntity(url, Map[].class);
            List<Map> data = response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();

            ApiResponse<List<Map>> api = ApiResponse.<List<Map>>builder()
                    .code("SUCCESS")
                    .status(200)
                    .message("Scraping de test exécuté (aucune sauvegarde en base)")
                    .data(data)
                    .build();
            return ResponseEntity.ok(api);
        } catch (Exception e) {
            log.error("Erreur lors du scraping de test: {}", e.getMessage(), e);
            ApiResponse<List<Map>> api = ApiResponse.<List<Map>>builder()
                    .code("ERROR")
                    .status(500)
                    .message("Echec lors de l'appel à l'API Flask")
                    .data(Collections.emptyList())
                    .build();
            return ResponseEntity.status(500).body(api);
        }
    }
}
