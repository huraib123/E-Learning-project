package com.example.SkillCore.Controller;
import org.springframework.security.core.Authentication;

import com.example.SkillCore.Models.Course;
import com.example.SkillCore.Models.User;
import com.example.SkillCore.Repository.Courserepo;
import com.example.SkillCore.Repository.Userrepo;
import com.example.SkillCore.Security.CustomUserDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private Courserepo cr;

    @Autowired
    private Userrepo ur; // ✅ Add this to fetch instructor

    // ✅ CREATE COURSE WITH FILE UPLOAD + DURATION PARSER
    @PostMapping("/uploads")
    public Course createCourse(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String category,
            @RequestParam double price,
            @RequestParam String duration,  // Input like "3h 20m"
            @RequestParam String level,
            @RequestParam String subject,
            @RequestParam Long instructorId,
            @RequestParam("video") MultipartFile videoFile
    ) {
        // --- Parse duration input like "3h 20m" ---
        Duration parsedDuration = parseDuration(duration);
        String durationString = parsedDuration.toString(); // Store as ISO string: PT3H20M

        // --- Save uploaded file locally ---
        String uploadDir = "uploads/videos/";
        File directory = new File(uploadDir);
        if (!directory.exists()) directory.mkdirs();

        String fileName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
        File destinationFile = new File(uploadDir + fileName);

        try {
            videoFile.transferTo(destinationFile);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading video: " + e.getMessage());
        }

        // ✅ Fetch the instructor entity from DB
        User instructor = ur.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found with ID: " + instructorId));

        // --- Create and populate course object ---
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setSubject(subject);
        course.setCategory(category);
        course.setPrice(price);
        course.setLevel(level);
        course.setDuration(durationString);
        
        course.setInstructor(instructor); // ✅ Correct type
        course.setThumbnailurl(destinationFile.getPath()); // optional rename to videoUrl

        return cr.save(course);
    }

    // ✅ Helper method: Parse "3h 20m" or "45m"
    private Duration parseDuration(String input) {
        input = input.toLowerCase().replaceAll("\\s+", "");

        int hours = 0;
        int minutes = 0;

        try {
            if (input.contains("h")) {
                String hoursPart = input.substring(0, input.indexOf("h"));
                hours = Integer.parseInt(hoursPart);
                input = input.substring(input.indexOf("h") + 1);
            }

            if (input.contains("m")) {
                String minutesPart = input.substring(0, input.indexOf("m"));
                if (!minutesPart.isEmpty()) {
                    minutes = Integer.parseInt(minutesPart);
                }
            }

            return Duration.ofHours(hours).plusMinutes(minutes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid duration format. Use '3h 20m' or '45m'.");
        }
    }

    // ✅ GET ALL COURSES
    @GetMapping("/allcourses")
    public List<Course> getAllCourses() { 
        return cr.findAll();
    }

    // ✅ GET COURSE BY ID
    @GetMapping("/allcourses/mycourses")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCoursesByLoggedInInstructor(Authentication authentication) {
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        List<Course> courses = cr.findCoursesByInstructorId(user.getId());
        return courses.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No courses found for instructor ID " + user.getId())
                : ResponseEntity.ok(courses);
    }


 

    // ✅ DELETE COURSE
    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable Long id) {
        if (!cr.existsById(id)) {
            return "Course not found";
        }
        cr.deleteById(id);
        return "Course deleted successfully";
    }
}
