package com.sample.url.exception;
/***
 * Custom Exception for Invalid URL Request scenario
 * @author Ritika Sao
 *
 */
public class UnProcessableEntityException extends RuntimeException {

	public UnProcessableEntityException(String message) {
		super(message);
	}
}
