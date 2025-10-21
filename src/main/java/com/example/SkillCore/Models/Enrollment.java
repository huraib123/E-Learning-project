package com.example.SkillCore.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public LocalDateTime getEnrolledat() {
		return enrolledat;
	}

	public void setEnrolledat(LocalDateTime enrolledat) {
		this.enrolledat = enrolledat;
	}

	public LocalDateTime getCompletedat() {
		return completedat;
	}

	public void setCompletedat(LocalDateTime completedat) {
		this.completedat = completedat;
	}

	@Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne()
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne()
	@JoinColumn(name="course_id")
	private Course course;
	
	private boolean completed=false;
	private LocalDateTime enrolledat;
	
	private LocalDateTime completedat;
	
	}
