package com.sample.url.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/***
 * Entity to create a URL request and map it to JSON Format to provide to the rest end points of API
 * @author Ritika Sao
 *
 */
@ApiModel(description = "URL Request")
public class URLRequest{
	
    // URL request
    @ApiModelProperty(notes = "request URL")
    private String url;
	
    public URLRequest() {
    }

    public URLRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }	
}
