package com.example.SkillCore.Controller;

import com.example.SkillCore.Models.Course;
import com.example.SkillCore.Models.Enrollment;
import com.example.SkillCore.Models.User;
import com.example.SkillCore.Repository.Courserepo;
import com.example.SkillCore.Repository.Enrollmentrepo;
import com.example.SkillCore.Repository.Userrepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private Enrollmentrepo enrollmentRepository;

    @Autowired
    private Userrepo userRepository;

    @Autowired
    private Courserepo courseRepository;

    // ✅ Enroll user in a course
    @PostMapping("/enroll")
    public ResponseEntity<String> enrollUser(@RequestParam Long userId, @RequestParam Long courseId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (userOpt.isEmpty() || courseOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User or Course not found");
        }

        // Prevent duplicate enrollments
        if (enrollmentRepository.findByUserAndCourse(userOpt.get(), courseOpt.get()).isPresent()) {
            return ResponseEntity.badRequest().body("User already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(userOpt.get());
        enrollment.setCourse(courseOpt.get());
        enrollmentRepository.save(enrollment);

        return ResponseEntity.ok("User enrolled successfully!");
    }

    // ✅ Get all enrollments
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentRepository.findAll());
    }

    // ✅ Get all courses a user is enrolled in
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserEnrollments(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(enrollmentRepository.findByUser(userOpt.get()));
    }

    // ✅ Get all students enrolled in a course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getCourseEnrollments(@PathVariable Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Course not found");
        }
        return ResponseEntity.ok(enrollmentRepository.findByCourse(courseOpt.get()));
    }

    // ✅ Unenroll user from a course
    @DeleteMapping("/unenroll")
    public ResponseEntity<String> unenrollUser(@RequestParam Long userId, @RequestParam Long courseId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (userOpt.isEmpty() || courseOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User or Course not found");
        }

        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByUserAndCourse(userOpt.get(), courseOpt.get());
        if (enrollmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User is not enrolled in this course");
        }

        enrollmentRepository.delete(enrollmentOpt.get());
        return ResponseEntity.ok("User unenrolled successfully!");
    }
}
