package com.example.SkillCore.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SkillCore.Models.Course;

public interface Courserepo extends JpaRepository<Course,Long>{

	
	List<Course> findCoursesByInstructorId(Long instructorId);
	
}
