package com.example.SkillCore.Controller;

import com.example.SkillCore.Models.Course;
import com.example.SkillCore.Repository.Courserepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private Courserepo courseRepository;

    // ✅ Create a new course
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseRepository.save(course));
    }

    // ✅ Get all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    // ✅ Get course by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.isPresent()
                ? ResponseEntity.ok(course.get())
                : ResponseEntity.status(404).body("Course not found");
    }

    // ✅ Update a course
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Course not found");
        }

        Course course = courseOpt.get();
        course.setTitle(updatedCourse.getTitle());
        course.setDescription(updatedCourse.getDescription());
        course.setInstructor(updatedCourse.getInstructor());
        course.setDuration(updatedCourse.getDuration());

        return ResponseEntity.ok(courseRepository.save(course));
    }

    // ✅ Delete a course
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Course not found");
        }
        courseRepository.deleteById(id);
        return ResponseEntity.ok("Course deleted successfully");
    }
}
