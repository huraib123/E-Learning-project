package com.example.SkillCore.Controller;

import org.springframework.http.HttpHeaders;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

import org.aspectj.apache.bcel.util.ClassPath.ClassFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.SkillCore.Models.FileClass;
import com.example.SkillCore.Models.User;
import com.example.SkillCore.Repository.Filerepo;
import com.example.SkillCore.Repository.Userrepo;
import com.example.SkillCore.Security.JwtTokenProvider;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;


import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/classes")
public class FileController  {

	@Autowired
	private Filerepo fr;
	@Autowired
	 private  Userrepo ur;
	
	@Autowired
	private JwtTokenProvider tokenprovider;
	
	@PostMapping("/uploads")
	public ResponseEntity<?> uploadFile(
	        @RequestParam MultipartFile file,
	        @RequestHeader("Authorization") String token,
	        HttpServletRequest request) throws IOException, java.io.IOException {

	    // ✅ Define upload folder path
	    String uploadDir = System.getProperty("user.dir") + "/uploads";
	    File directory = new File(uploadDir);
	    if (!directory.exists()) {
	        directory.mkdirs(); // create folder if not exist
	    }

	    // ✅ Generate unique filename
	    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	    Path path = Paths.get(uploadDir, filename);
	    Files.write(path, file.getBytes());

	    // ✅ Build accessible image URL dynamically
	    String fileUrl = request.getScheme() + "://" +
	            request.getServerName() + ":" +
	            request.getServerPort() +
	            "/uploads/" + filename;

	    // ✅ Save file info in DB
	    FileClass mediaFile = new FileClass();
	    mediaFile.setFilename(filename);
	    mediaFile.setFiletype(file.getContentType());
	    mediaFile.setFilepath(path.toString());
	     // ✅ store image URL in DB
	    

	    
	    FileClass savedFile = fr.save(mediaFile);
	    
	    String usertoken=token.replace("Bearer","");
	    String username=tokenprovider.getUsernameFromToken(usertoken);
	    
	    Optional<User> optionaluser=ur.findByEmail(username);
	    if(optionaluser.isPresent()) {
	    	User user=optionaluser.get();
	    	user.setProfileimageurl(fileUrl);
	    	ur.save(user);
	    }
	    

	    
	    // ✅ Return image URL in response
	    return ResponseEntity.ok(Map.of(
	            
	            "imageurl", fileUrl,
	            "id",savedFile.getId()
	    ));
	}


	@GetMapping("/uploads/{id}")
	public ResponseEntity<byte[]> getFileById(@PathVariable Long id) {
	    try {
	        // Find the file entry in DB
	        Optional<FileClass> fileData = fr.findById(id);
	        if (fileData.isEmpty()) {
	            return ResponseEntity.notFound().build();
	        }

	        FileClass mediaFile = fileData.get();

	        // Load file from the path stored in DB
	        Path path = Paths.get(mediaFile.getFilepath());
	        if (!Files.exists(path)) {
	            return ResponseEntity.notFound().build();
	        }

	        // Read file bytes
	        byte[] fileBytes = Files.readAllBytes(path);

	        // Detect content type (image/png, image/jpeg, etc.)
	        String contentType = Files.probeContentType(path);

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .body(fileBytes);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().build();
	    }
	}
	
	@PutMapping("/uploads/profile")
	public ResponseEntity<User> updateProfileImage(
	        @RequestParam MultipartFile file,
	        @RequestHeader("Authorization") String token,
	        HttpServletRequest request) {
	    try {
	        // 1️⃣ Extract token and get username/email
	        String userToken = token.replace("Bearer ", "");
	        String email = tokenprovider.getUsernameFromToken(userToken);

	        // 2️⃣ Find user by email
	        Optional<User> optionalUser = ur.findByEmail(email);
	        if (optionalUser.isEmpty()) {
	            return ResponseEntity.status(404).body(null);
	        }
	        User user = optionalUser.get();

	        // 3️⃣ Define upload folder path
	        String uploadDir = System.getProperty("user.dir") + "/uploads";
	        File directory = new File(uploadDir);
	        if (!directory.exists()) {
	            directory.mkdirs();
	        }

	        // 4️⃣ Generate unique filename and save file
	        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	        Path path = Paths.get(uploadDir, filename);
	        Files.write(path, file.getBytes());

	        // 5️⃣ Build accessible image URL
	        String fileUrl = request.getScheme() + "://" +
	                request.getServerName() + ":" +
	                request.getServerPort() +
	                "/uploads/" + filename;

	        // 6️⃣ Save file info in DB
	        FileClass mediaFile = new FileClass();
	        mediaFile.setFilename(filename);
	        mediaFile.setFiletype(file.getContentType());
	        mediaFile.setFilepath(path.toString());
	        fr.save(mediaFile);

	        // 7️⃣ Update user's profile image URL
	        user.setProfileimageurl(fileUrl);
	        User updatedUser = ur.save(user);

	        // 8️⃣ Return updated user
	        return ResponseEntity.ok(updatedUser);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().build();
	    }
	}


}
