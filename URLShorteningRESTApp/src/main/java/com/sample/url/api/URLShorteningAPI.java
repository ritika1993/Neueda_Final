package com.sample.url.api;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import com.sample.url.model.URLRequest;
import com.sample.url.model.URLResponseBody;
import com.sample.url.model.URLStatistics;

import io.swagger.annotations.Api;

@Api(value="URL Shortening Application")
public interface URLShorteningAPI {

	
	public RedirectView redirectUrl(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
			throws IOException, URISyntaxException, Exception ;	
	
	public ResponseEntity<URLResponseBody> saveURLStatistics(@RequestBody @Valid final URLRequest uRLRequest)
			throws Exception;	
	
	public ResponseEntity<URLStatistics> getURLStatistics(@PathVariable String id);	
	
	public Resources<URLStatistics> getAllURLStatistics();	
	
	public ResponseEntity<URLResponseBody> deleteURLStatistics(@RequestBody @Valid final URLRequest uRLRequest)
			throws Exception;	
	
	public ResponseEntity<URLResponseBody> deleteURLStatistics() throws Exception;
}
