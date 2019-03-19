package com.sample.url.model;

import org.springframework.hateoas.ResourceSupport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/***
 * Entity for a Response Structure to the response of all HTTP method requests to rest end points of API
 * @author Ritika Sao
 *
 */
@ApiModel(description = "URL Response with short URL and detailed message")
public class URLResponseBody extends ResourceSupport {

	// Message for success, failure and error scenario
	@ApiModelProperty(notes = " response message")
	private String message;	
	// shortened URL as response
	@ApiModelProperty(notes = "shortened url response")
	private String shortURLResponse;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getShortURLResponse() {
		return shortURLResponse;
	}
	public void setShortURLResponse(String shortURLResponse) {
		this.shortURLResponse = shortURLResponse;
	}	
}
