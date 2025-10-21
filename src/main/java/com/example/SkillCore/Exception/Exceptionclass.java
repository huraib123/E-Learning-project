package com.example.SkillCore.Exception;

public class Exceptionclass {

	public class ResourceNotFoundException extends RuntimeException {
	    public ResourceNotFoundException(String message) {
	        super(message);
	    }
	}
	
	public class UserAlreadyExistsException extends RuntimeException {
	    public UserAlreadyExistsException(String message) {
	        super(message);
	    }
	}
	
	public class InvalidCredentialsException extends RuntimeException {
	    public InvalidCredentialsException(String message) {
	        super(message);
	    }
	}
	public class UnauthorizedException extends RuntimeException {
	    public UnauthorizedException(String message) {
	        super(message);
	    }
	}
	public class BadRequestException extends RuntimeException {
	    public BadRequestException(String message) {
	        super(message);
	    }
	}
}
