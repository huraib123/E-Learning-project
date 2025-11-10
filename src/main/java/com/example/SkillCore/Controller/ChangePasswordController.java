package com.example.SkillCore.Controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SkillCore.Models.PasswordHandling;
import com.example.SkillCore.Models.User;
import com.example.SkillCore.Repository.Userrepo;

@RestController
@RequestMapping("/api/auth")
public class ChangePasswordController {

    @Autowired
    private Userrepo ur;

    @Autowired
    private PasswordEncoder passenco;

    @PutMapping("/change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordHandling request, Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized access"));
        }

        String username = authentication.getName();
        Optional<User> optionalUser = ur.findByEmail(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        User user = optionalUser.get();

        // Check old password match
        if (!passenco.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Old password is incorrect"));
        }

        // Optional: prevent using the same password
        if (passenco.matches(request.getNewPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "New password cannot be the same as the old password"));
        }

        // Update password
        user.setPassword(passenco.encode(request.getNewPassword()));
        ur.save(user);

        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

   }
