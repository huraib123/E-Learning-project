package com.example.SkillCore.Models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean verified = false;

    @Column(unique = true)
    private String email;

    
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String fullname;

    @Column(unique = true)
    private String phonenumber;

    private String profileimageurl;

    public String getProfileimageurl() {
		return profileimageurl;
	}

	public void setProfileimageurl(String profileimageurl) {
		this.profileimageurl = profileimageurl;
	}

	@CreationTimestamp
    private LocalDateTime toc;

    @CreationTimestamp
    private LocalDateTime tod;

    @OneToMany(mappedBy = "instructor")
    @JsonManagedReference
    private List<Course> totalcourses;

    @OneToMany(mappedBy = "user")
    private List<Enrollment> totalenrolls;

	

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	

	public LocalDateTime getToc() {
		return toc;
	}

	public void setToc(LocalDateTime toc) {
		this.toc = toc;
	}

	public LocalDateTime getTod() {
		return tod;
	}

	public void setTod(LocalDateTime tod) {
		this.tod = tod;
	}

	public List<Course> getTotalcourses() {
		return totalcourses;
	}

	public void setTotalcourses(List<Course> totalcourses) {
		this.totalcourses = totalcourses;
	}

	public List<Enrollment> getTotalenrolls() {
		return totalenrolls;
	}

	public void setTotalenrolls(List<Enrollment> totalenrolls) {
		this.totalenrolls = totalenrolls;
	}

	public String getPassword() {
		return password;
	}
}
