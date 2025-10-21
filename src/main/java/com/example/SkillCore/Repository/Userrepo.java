package com.example.SkillCore.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SkillCore.Models.User;

public interface Userrepo extends JpaRepository<User,Long> {

	Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
	
}
