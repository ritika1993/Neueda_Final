package com.sample.url.service;

import java.util.List;
import java.util.Optional;

import com.sample.url.model.URLStatistics;
/***
 * Interface for the Service Layer of the application 
 * @author Ritika Sao
 *
 */
public interface ShortenURLService  {

	public String createShortenURL(String localURL, String longUrl);

	public void saveStatistics(URLStatistics urlStat);

	public List<URLStatistics> getAllURLStatistics();

	public URLStatistics findByShortURL(String shortURL);
	
	public Optional<URLStatistics> updateShortenURLWhenExists(String shortenedURL) ;
	
	public Optional<URLStatistics> saveShortenURLWhenNew(String shortenedURL, String longURL);
	
	public URLStatistics changeURLStatisticsForRedirect(URLStatistics urlStat);
	
	public int deleteStatisticsByURL(String longURL);
	
	public int deleteAllURLStatistics();
	
	public Optional<URLStatistics> findByOrigURL(String origURL);
}
