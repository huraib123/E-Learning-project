package com.example.SkillCore.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SkillCore.Models.User;
import com.example.SkillCore.Repository.Userrepo;
import com.example.SkillCore.Security.JwtTokenProvider;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final Userrepo userRepo;
    @Autowired
    private final JwtTokenProvider tokenprovider;

    public UserController(Userrepo userRepo) {
        this.userRepo = userRepo;
		this.tokenprovider = null;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepo.existsById(id)) return ResponseEntity.notFound().build();

        userRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam String email) {
        Optional<User> userOpt = userRepo.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        User user=userOpt.get();

        return ResponseEntity.ok(user);
    }

    @PutMapping("/updateprofile")
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody User userProfileRequest) {
        try {
            
            String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
            String username = tokenprovider.getUsernameFromToken(jwt);
            User existingUser = userRepo.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
           
            existingUser.setFullname(userProfileRequest.getFullname());
            existingUser.setEmail(userProfileRequest.getEmail());
            existingUser.setPhonenumber(userProfileRequest.getPhonenumber());
            User user=userRepo.save(existingUser);
            
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    
}
