package com.example.SkillCore.Controller;

import org.springframework.http.HttpHeaders;

import java.io.File;
import java.nio.file.Files;

import java.util.Optional;

import org.aspectj.apache.bcel.util.ClassPath.ClassFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.SkillCore.Models.FileClass;
import com.example.SkillCore.Repository.Filerepo;

import io.jsonwebtoken.io.IOException;
import org.springframework.http.MediaType;

import java.nio.file.Path;


@RestController
@RequestMapping("/api/classes")
public class FileController  {

	@Autowired
	private Filerepo fr;
	
	@PostMapping("/upload")
	public ResponseEntity<?> uploadfile(@RequestParam MultipartFile file, @RequestHeader("Authorization") String token) throws IOException, java.io.IOException{
		
		File directory = new File("uploads");
		if (!directory.exists()) {
		    directory.mkdirs(); // ✅ Creates "uploads" folder if it doesn't exist
		}

		java.nio.file.Path path = java.nio.file.Paths.get(directory + file.getOriginalFilename());
		Files.write(path, file.getBytes());

		Files.write(path,file.getBytes());
		
		 FileClass mediaFile = new FileClass();
		    mediaFile.setFilename(file.getOriginalFilename());
		    mediaFile.setFiletype(file.getContentType());
		    mediaFile.setFilepath(path.toString());

		    fr.save(mediaFile);
		
		    return ResponseEntity.ok("File Uploaded Sucessfull at "+path);
	}
	 @GetMapping("/download/{id}")
	    public ResponseEntity<byte[]> downloadFromDatabase(@PathVariable Long id) {
	        Optional<FileClass> fileOptional = fr.findById(id);
	        if (fileOptional.isPresent()) {
	            FileClass classFile = fileOptional.get();
	           String type= classFile.getFiletype();
	            return ResponseEntity.ok() .contentType(MediaType.parseMediaType(type))
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + classFile.getFilename() + "\"")
	                    .body(classFile.getFiledata());
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
}
