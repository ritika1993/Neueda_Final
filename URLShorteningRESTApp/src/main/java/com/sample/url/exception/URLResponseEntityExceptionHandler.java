package com.sample.url.exception;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import redis.clients.jedis.exceptions.JedisException;
/***
 * This class handles the Exceptions from the application and process a respective Response Entity
 * @author Ritika Sao
 *
 */
@ControllerAdvice
@RestController
public class URLResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

	/***
	 * This method automatically handles the generic Exception and creates a ResponseEntity with proper error message 
	 * @param e
	 * @param we
	 * @return ResponseEntity with a ExceptionResponse message
	 */
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception e, WebRequest we ){		
		ExceptionResponse exceptionResponse = new ExceptionResponse(we.getDescription(false),e.getMessage(),LocalDateTime.now());
		return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	/***
	 * This method automatically handles the custom URLNotFoundException and creates a ResponseEntity with proper error message
	 * @param e
	 * @param we
	 * @return ResponseEntity with a ExceptionResponse message
	 */
	@ExceptionHandler(URLNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleURLNotFoundException(URLNotFoundException e, WebRequest we ){		
		ExceptionResponse exceptionResponse = new ExceptionResponse(we.getDescription(false),e.getMessage(),LocalDateTime.now());
		return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	/***
	 * This method automatically handles the custom UnProcessableEntityException and creates a ResponseEntity with proper error message
	 * @param e
	 * @param we
	 * @return ResponseEntity with a ExceptionResponse message
	 */
	@ExceptionHandler(UnProcessableEntityException.class)
	public final ResponseEntity<Object> handleUnProcessableEntityException(UnProcessableEntityException e, WebRequest we ){		
		ExceptionResponse exceptionResponse = new ExceptionResponse(we.getDescription(false),e.getMessage(),LocalDateTime.now());
		return new ResponseEntity(exceptionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	/***
	 * This method automatically handles the JedisException and creates a ResponseEntity with proper error message
	 * @param e
	 * @param we
	 * @return ResponseEntity with a ExceptionResponse message
	 */
	@ExceptionHandler(JedisException.class)
	public final ResponseEntity<ExceptionResponse> handleJedisException(JedisException e, WebRequest we ){		
		ExceptionResponse exceptionResponse = new ExceptionResponse(we.getDescription(false),e.getMessage(),LocalDateTime.now());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_GATEWAY);
	}
	
}
