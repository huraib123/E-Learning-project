package com.example.SkillCore.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SkillCore.Models.PasswordForgot;

public interface PasswordForgotRepo extends JpaRepository<PasswordForgot,Long>{
	
	Optional<PasswordForgot> findByEmailAndOtp(String email, String otp);

}
