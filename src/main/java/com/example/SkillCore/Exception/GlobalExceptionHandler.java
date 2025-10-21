package com.example.SkillCore.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.SkillCore.Exception.Exceptionclass.BadRequestException;
import com.example.SkillCore.Exception.Exceptionclass.InvalidCredentialsException;
import com.example.SkillCore.Exception.Exceptionclass.ResourceNotFoundException;
import com.example.SkillCore.Exception.Exceptionclass.UnauthorizedException;
import com.example.SkillCore.Exception.Exceptionclass.UserAlreadyExistsException;
import com.example.SkillCore.Models.ApiError;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	  @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
	        ApiError error = new ApiError(
	                HttpStatus.NOT_FOUND.value(),
	                "Resource Not Found",
	                ex.getMessage(),
	                request.getRequestURI()
	        );
	        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	    }
	  @ExceptionHandler(UserAlreadyExistsException.class)
	    public ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExistsException ex, HttpServletRequest request) {
	        ApiError error = new ApiError(
	                HttpStatus.CONFLICT.value(),
	                "Conflict",
	                ex.getMessage(),
	                request.getRequestURI()
	        );
	        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	    }
	  
	  @ExceptionHandler(InvalidCredentialsException.class)
	    public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
	        ApiError error = new ApiError(
	                HttpStatus.UNAUTHORIZED.value(),
	                "Invalid Credentials",
	                ex.getMessage(),
	                request.getRequestURI()
	        );
	        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	    }

	    @ExceptionHandler(UnauthorizedException.class)
	    public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
	        ApiError error = new ApiError(
	                HttpStatus.FORBIDDEN.value(),
	                "Unauthorized Access",
	                ex.getMessage(),
	                request.getRequestURI()
	        );
	        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	    }

	    @ExceptionHandler(BadRequestException.class)
	    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
	        ApiError error = new ApiError(
	                HttpStatus.BAD_REQUEST.value(),
	                "Bad Request",
	                ex.getMessage(),
	                request.getRequestURI()
	        );
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	    }
	
}
