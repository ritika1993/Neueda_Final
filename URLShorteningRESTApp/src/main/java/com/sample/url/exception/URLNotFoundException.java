package com.sample.url.exception;
/***
 * Custom Exception for URL Not Found Scenario
 * @author Ritika Sao
 *
 */
public class URLNotFoundException extends RuntimeException {
	
	public URLNotFoundException(final String message) {		
		super(message);
	}
}
