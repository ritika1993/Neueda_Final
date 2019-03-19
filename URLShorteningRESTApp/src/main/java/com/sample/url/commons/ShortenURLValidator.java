package com.sample.url.commons;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * This class is to validate the URL for http/https, blocks the local domain and ensure it to be active (returning 2XX response)
 * 
 * @author Ritika Sao
 *
 */
public class ShortenURLValidator {

	private static UrlValidator urlValidator;
	private static final String[] SCHEMES = { "http", "https" };
	private static final Logger LOGGER = LoggerFactory.getLogger(ShortenURLValidator.class);

	static {
		urlValidator = new UrlValidator(SCHEMES, UrlValidator.ALLOW_ALL_SCHEMES);
	}

	/***
	 * This method validates URL to be coming from HTTP or HTTPS and for HTTP Response of 2XX
	 * It invalidates URL containing the local domain as for simplicity the local domain links are
	 * blocked from shortening
	 * @param url
	 * @param localHost
	 * @return boolean for validated URL
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static boolean validateURL(String url, String localHost) throws UnknownHostException, IOException {
		/* block shortening for the localhost:/8086 */
		LOGGER.info("local URL {}", localHost);
		if (urlValidator.isValid(url) && !url.contains(localHost)) {			
			URL myURL = new URL(url);
			HttpURLConnection myConnection = (HttpURLConnection) myURL.openConnection();
			myURL.openConnection();
			LOGGER.info("URL response code {} ", myConnection.getResponseCode());
			if (myConnection.getResponseCode() >= 200 && myConnection.getResponseCode() < 300) {
				LOGGER.info("URL validated ... ");
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
