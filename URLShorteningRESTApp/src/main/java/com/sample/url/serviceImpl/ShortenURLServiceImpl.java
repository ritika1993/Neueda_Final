package com.sample.url.serviceImpl;

import static com.sample.url.commons.KeyConverter.encodeToBase62;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.url.model.URLStatistics;
import com.sample.url.repository.URLRedisRepository;
import com.sample.url.repositoryConfig.URLRepositoryConfig;
import com.sample.url.service.ShortenURLService;
/***
 * This class contains the business logic of the application as Service Layer
 * @author Ritika Sao
 *
 */
@Service
public class ShortenURLServiceImpl implements ShortenURLService {

	private URLRepositoryConfig urlRepository;
	private URLRedisRepository urlRedisRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(ShortenURLServiceImpl.class);

	@Autowired
	public ShortenURLServiceImpl(URLRepositoryConfig urlRepository, URLRedisRepository urlRedisRepository) {
		this.urlRepository = urlRepository;
		this.urlRedisRepository = urlRedisRepository;
	}

	/***
	 * This method creates a shortened URL for the provided URL
	 * @return shortened URL
	 * @param localDomain 
	 * @param original URL
	 */
	@Override
	public String createShortenURL(String localDomain, String longUrl) {
		LOGGER.info("Shortening {}", longUrl);
		Long id = urlRepository.incrementID();
		String uniqueID = encodeToBase62(id);
		String shortenedURL = localDomain + uniqueID;
		Optional<URLStatistics> urlStatistics = urlRedisRepository.findById(longUrl);

		if (urlStatistics.isPresent()) {
			LOGGER.info("Shortened URL created : {}", shortenedURL);
			return urlStatistics.get().getUrl();
		} else {
			LOGGER.info("Shortened URL existed : {}", shortenedURL);
			return shortenedURL;
		}
	}

	/***
	 * This method updates the URL already existing for a shortening request, increments the no of requests by 1
	 * and last access time by current time
	 * @param Original URL
	 * @return URLStatistics
	 * 
	 */
	@Override	
	public Optional<URLStatistics> updateShortenURLWhenExists(String origURL) {
		try {
			Optional<URLStatistics> urlStat = findByOrigURL(origURL);
			if (urlStat.isPresent()) {
				Long requests = urlStat.get().getNoOfRequests();
				urlStat.get().setNoOfRequests(requests += 1);
				urlStat.get().setLastAccessed(LocalDateTime.now());
				urlRedisRepository.save(urlStat.get());

			}
			return urlStat;
		} catch (Exception e) {
			LOGGER.error(e.toString());
			return null;
		}
	}

	/***
	 * This method creates a new shortened URL for the request, sets no of requests = 1 and no of redirects =0
	 * and last access time as current time
	 * @param shortened URL
	 * @param Original URL
	 * @return URLStatistics
	 */
	@Override	
	public Optional<URLStatistics> saveShortenURLWhenNew(String shortenedURL, String longURL) {
		try {
			URLStatistics urlStatistics = new URLStatistics(shortenedURL, longURL, LocalDateTime.now());
			urlStatistics.setNoOfRedirects(0);
			urlStatistics.setNoOfRequests(1);
			urlRedisRepository.save(urlStatistics);
			return Optional.of(urlStatistics);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			return null;
		}
	}
	/***
	 * This method updates the provided URLStatistics for a redirect request, increments no of redirects by 1
	 * and replace last access time by current time
	 * @param URLStatistics
	 * @return URLStatistics
	 */
	@Override
	public URLStatistics changeURLStatisticsForRedirect(URLStatistics urlStat) {
		LOGGER.info("Inside changeURLStatisticsForRedirect method for {} : " + urlStat);
		if (urlStat != null) {
			Long redirects = urlStat.getNoOfRedirects();
			urlStat.setNoOfRedirects(redirects += 1);
			urlStat.setLastAccessed(LocalDateTime.now());
			urlRedisRepository.save(urlStat);
		}
		LOGGER.info("Updated changeURLStatisticsForRedirect method for {} : " + urlStat);
		return urlStat;
	}
	/***
	 * This method deletes a new shortened URL for the request
	 * @param Original URL
	 * @return integer value (0 if success, -1 if fail)
	 */
	@Override
	public int deleteStatisticsByURL(String longURL) {
		if (urlRedisRepository.findById(longURL).isPresent()) {
			urlRedisRepository.deleteById(longURL);
			return 0;
		}
		return -1;

	}
	/***
	 * This method deletes all shortened URLs
	 * @return integer value (0 if success, -1 if fail)
	 */
	@Override
	public int deleteAllURLStatistics() {
		if (urlRedisRepository.findAll().iterator().hasNext()) {
			urlRedisRepository.deleteAll();
			return 0;
		}
		return -1;

	}
	/***
	 * This method saves a URLStatistics for a URL 
	 * @param URLStatistics
	 */
	@Override
	public void saveStatistics(URLStatistics urlStat) {
		urlRedisRepository.save(urlStat);
	}
	/***
	 * This method returns a list of URLStatistics of all URLs
	 * @return List of URLStatistics
	 */
	@Override
	public List<URLStatistics> getAllURLStatistics() {
		LOGGER.info("Inside getAllURLStatistics method");
		List<URLStatistics> listOfURLs = new ArrayList<>();
		urlRedisRepository.findAll().forEach(listOfURLs::add);
		LOGGER.info("Returning list of URLs {}", listOfURLs);
		return listOfURLs;
	}
	/***
	 * This method returns URLStatistics for a shortened url
	 * @param shortened URL
	 * @return URLStatistics
	 */
	@Override
	public URLStatistics findByShortURL(String shortURL) {
		List<URLStatistics> urlStatList = getAllURLStatistics();
		LOGGER.info("Statistics {} ", urlStatList);
		if (urlStatList.size() != 0) {
			URLStatistics urlStat = urlStatList.stream().filter(x -> x.getUrl().equals(shortURL)).findAny()
					.orElse(null);
			LOGGER.info("Returning URLStatistics by short URL {} ", urlStat);
			return urlStat;
		} else {
			LOGGER.error("No URL exists ");
			return null;
		}
	}
	/***
	 * This method returns URLStatistics for a original url
	 * @param Original URL
	 * @return URLStatistics
	 */
	@Override
	public Optional<URLStatistics> findByOrigURL(String origURL) {
		return urlRedisRepository.findById(origURL);
	}
}
