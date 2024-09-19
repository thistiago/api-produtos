package org.caixacvp.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
public class OpenApiController {

    @GetMapping("/api-docs.yaml")
    public ResponseEntity<String> getOpenApiYaml() throws IOException {
        ClassPathResource openApiResource = new ClassPathResource("openapi.yaml");

        String openApiYaml = new String(Files.readAllBytes(openApiResource.getFile().toPath()));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/yaml");

        return new ResponseEntity<>(openApiYaml, headers, HttpStatus.OK);
    }
}
