package com.example.SkillCore.Controller;

import java.lang.System.Logger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.SkillCore.Models.User;
import com.example.SkillCore.Models.Verify;
import com.example.SkillCore.Repository.Userrepo;
import com.example.SkillCore.Repository.VerifyRepo;
import com.example.SkillCore.Security.JwtTokenProvider;



import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final Userrepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender mailSender;
    private final VerifyRepo verifyRepo;



    

    public AuthController(Userrepo userRepo, PasswordEncoder passwordEncoder,
                          AuthenticationManager authManager,
                          JwtTokenProvider jwtTokenProvider,
                          JavaMailSender mailSender,
                          VerifyRepo verifyRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mailSender = mailSender;
        this.verifyRepo = verifyRepo;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        
    	
    	
    	if (user.getPassword() == null || user.getPassword().isEmpty()) {
    		
            return ResponseEntity.badRequest().body("Password cannot be empty");
        }

    	else if (userRepo.existsByEmail(user.getEmail())&& user.isVerified()) {
            return ResponseEntity.badRequest().body("User already exists, proceed with login");
        }
    	else if (userRepo.existsByEmail(user.getEmail())&& user.isVerified()==false) {
            return ResponseEntity.badRequest().body("User not registered");
        }
    	
    	else {
            
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setVerified(false);
            

            // Generate OTP
            String otp = String.format("%06d", new Random().nextInt(999999));
            LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

            Verify ver = new Verify();
            ver.setEmail(user.getEmail());
            ver.setOtp(otp);
            ver.setExpirytime(expiry);
            verifyRepo.save(ver);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Verify your SkillCore account");
            message.setText("Your OTP is: " + otp + "\nIt expires in 10 minutes.");
            mailSender.send(message);
            userRepo.save(user);

            return ResponseEntity.ok("User registered! Check email for OTP");
    	}

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        String token = jwtTokenProvider.generateToken(auth);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("email", user.getEmail());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String email, @RequestParam String otp) {
        var otpOpt = verifyRepo.findByEmailAndOtp(email, otp);
        if (otpOpt.isEmpty()) return ResponseEntity.badRequest().body("Invalid OTP");

        var ver = otpOpt.get();
        if (ver.getExpirytime().isBefore(LocalDateTime.now()))
            return ResponseEntity.badRequest().body("OTP expired");

        var userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");

        User user = userOpt.get();
        user.setVerified(true);
        userRepo.save(user);
        verifyRepo.delete(ver);

        return ResponseEntity.ok("User verified successfully!");
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        var userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();
        user.setVerified(false);
        verifyRepo.findByEmail(email).ifPresent(verifyRepo::delete);

        if (user.isVerified()) {
            return ResponseEntity.badRequest().body("User already verified. Please login.");
        }

        // Generate new OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        // If old OTP exists, delete it
        

        // Save new OTP
        Verify ver = new Verify();
        ver.setEmail(email);
        ver.setOtp(otp);
        ver.setExpirytime(expiry);
        verifyRepo.save(ver);

        // Send OTP mail again
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Resend OTP - SkillCore Verification");
        message.setText("Your new OTP is: " + otp + "\nIt expires in 10 minutes.");
        mailSender.send(message);

        return ResponseEntity.ok("A new OTP has been sent to your registered email.");
    }

}
