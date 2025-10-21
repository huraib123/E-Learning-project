package com.example.SkillCore.Controller;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SkillCore.Models.PasswordForgot;
import com.example.SkillCore.Models.User;
import com.example.SkillCore.Repository.PasswordForgotRepo;
import com.example.SkillCore.Repository.Userrepo;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class PasswordForgotController {
 
	@Autowired
	 private PasswordForgotRepo passforrepo;
	 
	@Autowired
	 private Userrepo ur;
	
	@Autowired
	private PasswordEncoder passenco;
	
	@Autowired
	private JavaMailSender mailsender;
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotpassword(@RequestParam String email) throws MessagingException{
		
		Optional<User> opt=ur.findByEmail(email);
		if(opt.isEmpty()) {
			return ResponseEntity.status(404).body("user not found");
		}
		
		//Generating otp
		 String otp = String.format("%06d", new Random().nextInt(999999));
		 LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);
		 
		 //saving otp in db
		 
		 PasswordForgot pf=new PasswordForgot();
		 pf.setEmail(email);
		 pf.setOtp(otp);
		 pf.setExpirytime(expiryTime);
		 passforrepo.save(pf);
		 
		 //sending mail
		 SimpleMailMessage message= new SimpleMailMessage();
		 message.setTo(email);
		 message.setSubject("Reset your Password");
		 message.setText("your Otp is "+otp +" and valid for 10 minutes");
		 mailsender.send(message);
		 return ResponseEntity.ok("otp send to mail:" +email);
	}
	
	@PostMapping("/reset-password")
 
	public ResponseEntity<?> resetpassword(@RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword){
		
		Optional<PasswordForgot> pfor=passforrepo.findByEmailAndOtp(email, otp);
		if(pfor.isEmpty()) {
			return ResponseEntity.badRequest().body("Invalid otp");
		}
		
		PasswordForgot pf=pfor.get();
		
		if(pf.getExpirytime().isBefore(LocalDateTime.now())) {
			return ResponseEntity.badRequest().body("OTP expired");
		}
		Optional<User> userOpt = ur.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
		
        User u=userOpt.get();
        u.setPassword(passenco.encode(newPassword));
        ur.save(u);
        
        passforrepo.delete(pf);
        return ResponseEntity.ok("Password Reset Sucessfull");
		
	}
	 
	
}
