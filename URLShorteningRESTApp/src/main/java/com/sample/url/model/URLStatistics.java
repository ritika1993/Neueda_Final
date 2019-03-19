package com.sample.url.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.hateoas.ResourceSupport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/***
 * DTO for Object Relation Mapping with Redis Inmemory Database
 * @author Ritika Sao
 *
 */
@RedisHash("URL")
@ApiModel(description = "Statistics of the URL shortened")
public class URLStatistics extends ResourceSupport {

	@Id
	/* Original URL */
	@ApiModelProperty(notes = "Original URL")
	private String origURL;
	
	/* Shortened URL */
	@ApiModelProperty(notes = " Shortened URL")
	private String url;
	
	/* No of times request for URL shortening */
	@ApiModelProperty(notes = "No of Shortening Request to URL")
	private long noOfRequests;
	
	/* No of times redirected to original URL */
	@ApiModelProperty(notes = "No of times redirected to original URL")
	private long noOfRedirects;
	
	/* Last time accessed either by request or redirect */
	@ApiModelProperty(notes = "Last timestamp of request/redirect")
	private LocalDateTime lastAccessed;

	
	public URLStatistics(String url, String origURL, LocalDateTime lastAccessed) {
		this.url = url;
		this.origURL = origURL;
		this.lastAccessed = lastAccessed;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOrigURL() {
		return origURL;
	}

	public void setOrigURL(String origURL) {
		this.origURL = origURL;
	}

	public long getNoOfRequests() {
		return noOfRequests;
	}

	public void setNoOfRequests(long noOfRequests) {
		this.noOfRequests = noOfRequests;
	}

	public long getNoOfRedirects() {
		return noOfRedirects;
	}

	public void setNoOfRedirects(long noOfRedirects) {
		this.noOfRedirects = noOfRedirects;
	}

	public LocalDateTime getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(LocalDateTime lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	@Override
	public String toString() {
		return "URLStatistics [origURL=" + origURL + ", url=" + url + ", noOfRequests=" + noOfRequests
				+ ", noOfRedirects=" + noOfRedirects + ", lastAccessed=" + lastAccessed + "]";
	}

}
