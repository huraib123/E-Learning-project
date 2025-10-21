package com.example.SkillCore.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SkillCore.Models.Course;
import com.example.SkillCore.Models.Enrollment;
import com.example.SkillCore.Models.User;

public interface Enrollmentrepo extends JpaRepository<Enrollment,Long> {

	List<Enrollment> findByUser(User user);
    List<Enrollment> findByCourse(Course course);
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
}
