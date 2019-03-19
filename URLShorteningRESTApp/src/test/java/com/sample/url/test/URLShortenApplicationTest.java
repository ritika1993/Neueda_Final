package com.sample.url.test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.sample.url.model.URLStatistics;
import com.sample.url.repository.URLRedisRepository;
import com.sample.url.repositoryConfig.URLRepositoryConfig;
import com.sample.url.serviceImpl.ShortenURLServiceImpl;

/***
 * Test class for unit test cases of URL shortening functionalities
 * 
 * @author Ritika Sao
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PropertySource(value = "classpath:application.properties")
public class URLShortenApplicationTest {

	private static Logger LOGGER = LoggerFactory.getLogger(URLShortenApplicationTest.class);

	/***
	 * List of sites to be tested for URL Shortening and verifying with Original URL
	 */
	private static List<String> URL_1 = Arrays.asList(
			"https://github.blog/2019-03-14-githubs-site-policy-updates-are-ready-for-your-feedback/",
			"https://httpstatuses.com/403",
			"https://mvnrepository.com/open-source/aop-programming");
	/***
	 * List of sites to be tested for URL Statistics
	 */
	private static List<String> URL_2 = Arrays.asList(
			"https://mvnrepository.com/open-source/application-metrics",
			"https://www.tutorialspoint.com/spring_boot/spring_boot_bootstrapping.htm",
			"https://dzone.com/articles/global-exception-handling-with-controlleradvice");

	/***
	 * List of sites to be tested for deletion
	 */
	private static List<String> URL_3 = Arrays.asList(
			"https://mvnrepository.com/open-source/cache-implementations",
			"https://docs.spring.io/spring/docs/3.0.0.M3/reference/html/ch16s11.html",
			"https://docs.spring.io/spring/docs/3.0.0.M3/reference/html/ch16s10.html");
	@Autowired
	private URLRedisRepository urlRedis;
	/***
	 * Domain Name 
	 * Note: Defined the local domain as constant (can be fetched by properties file)
	 */
	private static final String LOCAL_DOMAIN = "https://localhost:8086/";

	private static URLRepositoryConfig urlRepo;
	private static ShortenURLServiceImpl service;

	/***
	 * Method to perform initialisation of repository and service beans
	 */
	@Before
	public void beforeConfiguration() {
		LOGGER.info("Inside Before config ========================================");

		urlRepo = new URLRepositoryConfig();
		service = new ShortenURLServiceImpl(urlRepo, urlRedis);
		
		LOGGER.info("Before config performed ========================================");

	}

	/***
	 * Test Method to perform: 
	 * 1. URL shortening on the list of sites 
	 * 2. Saving to the database
	 * 3. Verifying the URL provided for shortening with the original URL
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testForURLShortening() throws Exception {
		LOGGER.info("Inside Test 1 ========================================================");
		for (String url : URL_1) {
			String shortURL = service.createShortenURL(LOCAL_DOMAIN, url);
			Assert.assertNotNull(shortURL);
			service.saveShortenURLWhenNew(shortURL, url);
			LOGGER.debug("short URL {}", shortURL);
			URLStatistics origURL = service.findByShortURL(shortURL);
			Assert.assertNotNull(origURL);
			Assert.assertEquals(origURL.getOrigURL(), url);
		}
		LOGGER.info("End Test 1 ========================================================");
	}

	/***
	 * Test method to perform: 1. Create shorten URLs for a list of sites 2. Save it
	 * to the database 3. Increment no of requests for already existing URL and
	 * create new statistics for new URL request 4. Perform Redirect and change
	 * statistics 5. Verify increment of count of no of requests and redirects
	 * 
	 * @throws Exception
	 */
	@Test
	public void testForURLStatistics() throws Exception {
		LOGGER.info("Inside Test 2 ========================================================");

		for (String url : URL_2) {
			LOGGER.info("Test: No of Requests for URL {} : ", url);
			String shortURL = service.createShortenURL(LOCAL_DOMAIN, url);
			Assert.assertNotNull(shortURL);
			Optional<URLStatistics> statBeforeSaved = service.saveShortenURLWhenNew(shortURL, url);
			LOGGER.info("before saved URL {}", statBeforeSaved.get());
			Optional<URLStatistics> statAfterSaved = service.updateShortenURLWhenExists(url);
			LOGGER.info("After saved URL {}", statAfterSaved.get());
			Assert.assertEquals(statAfterSaved.get().getNoOfRequests(), statBeforeSaved.get().getNoOfRequests() + 1);
			Assert.assertEquals(statBeforeSaved.get().getNoOfRedirects(), statAfterSaved.get().getNoOfRedirects());
			LOGGER.info("Test: No of Redirects for URL {} : ", url);
			Optional<URLStatistics> statRedirect = service.findByOrigURL(url);
			if (statRedirect.isPresent()) {
				Long noOfRedirects = statRedirect.get().getNoOfRedirects();
				LOGGER.info("Before redirect URL {}", statRedirect.get());
				service.changeURLStatisticsForRedirect(statRedirect.get());
				LOGGER.info("After redirect URL {}", statRedirect.get());
				Assert.assertEquals(statRedirect.get().getNoOfRedirects(), noOfRedirects + 1);
			} else {
				Assert.fail("No URL found for Redirection Test");
			}
		}
		LOGGER.info("End Test 2 ========================================================");
	}

	/***
	 * Test Method to delete a list of URLs from the database and checks if they are deleted or not
	 */
	@Test
	public void testDeleteURLs() throws Exception{
		
		LOGGER.info("Inside Test 3 ========================================================");

		for (String url : URL_3) {			
			String shortURL = service.createShortenURL(LOCAL_DOMAIN, url);
			Assert.assertNotNull(shortURL);
			Optional<URLStatistics> statBeforeSaved = service.saveShortenURLWhenNew(shortURL, url);
			LOGGER.info("After saved URL {}", statBeforeSaved.get());
			service.deleteStatisticsByURL(url);
			Optional<URLStatistics> statRedirect = service.findByOrigURL(url);
		    	Assert.assertTrue("Deletion Test for URL "+ url +" passed", !statRedirect.isPresent());			
		}
		LOGGER.info("End Test 3 ========================================================");
	}
	/***
	 * Method to delete all the URLs shortened for the test from the database
	 */
	@AfterClass
	public static void afterTest() {
		LOGGER.info("After Test  ========================================================");
		URL_1.forEach(url -> {
			service.deleteStatisticsByURL(url);
		});

		URL_2.forEach(url -> {
			service.deleteStatisticsByURL(url);
		});
		
		URL_3.forEach(url -> {
			service.deleteStatisticsByURL(url);
		});
		LOGGER.info("After Test performed ========================================");
	}

}
