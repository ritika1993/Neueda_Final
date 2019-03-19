package com.sample.url.exception;

import java.time.LocalDateTime;
/***
 * This class is an Entity for generating an Exception Response for all exceptions handled at URLResponseEntityExceptionHandler
 * @author Ritika Sao
 *
 */
public class ExceptionResponse {

	// Information of the URL request
	private String requestInfo;
	// Details of Response
	private String details;
	// Timestamp of Response
	private LocalDateTime timeStamp;	
	
	public ExceptionResponse(String requestInfo, String details, LocalDateTime timeStamp) {
		this.requestInfo = requestInfo;
		this.details = details;
		this.timeStamp = timeStamp;
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public String getDetails() {
		return details;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}	
}
