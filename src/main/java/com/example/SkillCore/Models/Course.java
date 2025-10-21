package com.example.SkillCore.Models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
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
public class Course {
  
	@Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false)
	public String title;
	
	@Column(columnDefinition="TEXT")
	public String description;
	
	public LocalDateTime getDuration() {
		return duration;
	}

	public void setDuration(LocalDateTime duration) {
		this.duration = duration;
	}

	public String category;
	
	public String level;
	
	public String thumbnailurl;
	
	public LocalDateTime duration;
	
	public double price;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getThumbnailurl() {
		return thumbnailurl;
	}

	public void setThumbnailurl(String thumbnailurl) {
		this.thumbnailurl = thumbnailurl;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public User getInstructor() {
		return instructor;
	}

	public void setInstructor(User instructor) {
		this.instructor = instructor;
	}

	public List<Enrollment> getTotalcourseenrolls() {
		return totalcourseenrolls;
	}

	public void setTotalcourseenrolls(List<Enrollment> totalcourseenrolls) {
		this.totalcourseenrolls = totalcourseenrolls;
	}

	@ManyToOne
	@ JoinColumn(name="instructor_id",nullable=false)
	private User instructor;
	
	@OneToMany(mappedBy="course")
	private List<Enrollment> totalcourseenrolls;
}
