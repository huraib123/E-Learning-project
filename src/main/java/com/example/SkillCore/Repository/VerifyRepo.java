package com.example.SkillCore.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SkillCore.Models.Verify;

public interface VerifyRepo extends JpaRepository<Verify,Long> {

	 Optional<Verify> findByEmailAndOtp(String email, String otp);
	 Optional<Verify> findByEmail(String email);
}
