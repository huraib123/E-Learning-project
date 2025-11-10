package com.example.SkillCore.Controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.nio.file.*;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getVideo(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/videos").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
