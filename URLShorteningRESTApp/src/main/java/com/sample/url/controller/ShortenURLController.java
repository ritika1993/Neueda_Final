package com.sample.url.controller;

import static com.sample.url.commons.ShortenURLValidator.validateURL;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.sample.url.api.URLShorteningAPI;
import com.sample.url.exception.URLNotFoundException;
import com.sample.url.exception.UnProcessableEntityException;
import com.sample.url.model.URLRequest;
import com.sample.url.model.URLResponseBody;
import com.sample.url.model.URLStatistics;
import com.sample.url.serviceImpl.ShortenURLServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import redis.clients.jedis.exceptions.JedisException;
import springfox.documentation.annotations.ApiIgnore;

/***
 * This is the rest controller to expose all the rest end points of the
 * application for URL Shortening
 * 
 * @author Ritika Sao
 *
 */

@RestController
@PropertySource(value = "classpath:application.properties")

@RequestMapping("/url")
public class ShortenURLController implements URLShorteningAPI {

	@Value("${local.domain}")
	private String localDomain;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShortenURLController.class);

	private final ShortenURLServiceImpl urlConverterService;

	@Autowired
	public ShortenURLController(ShortenURLServiceImpl urlConverterService) {
		this.urlConverterService = urlConverterService;
	}

	/***
	 * Rest End point to redirect to the original URL URI: Shortened Link :
	 * https://<your domain>/url/bqw
	 * 
	 * @param id       (unique string id generated for the shortened link) (bqw)
	 * @param request
	 * @param response
	 * @return RedirectView to the original URL
	 * @throws Exception
	 */

	@ApiIgnore // Swagger doesn't support Redirecting to a URL hence not exposing it there
	@GetMapping(value = "/{id}")
	public RedirectView redirectUrl(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String shortenURL = localDomain + id;
		LOGGER.info("Received shortened url to redirect: " + shortenURL);
		URLStatistics urlStat = urlConverterService.findByShortURL(shortenURL);
		urlStat = urlConverterService.changeURLStatisticsForRedirect(urlStat);
		if (urlStat != null) {
			LOGGER.info("After Redirect URL : " + urlStat);
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl(urlStat.getOrigURL());
			return redirectView;
		} else {
			throw new URLNotFoundException("Couldn't redirect to the original URL.. short URL not valid");
		}
	}

	/***
	 * Rest End point to shorten the URL provided URI:
	 * https://<your domain>/url/shorten
	 * 
	 * @param uRLRequest (URL in json format)
	 * @return Response Entity with the URL ResponseBody along with links to a
	 *         specific URL info page and all URL info list
	 * @throws Exception
	 */
	@ApiOperation(value = "Shorten the original URL", response = URLResponseBody.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully Shortened URL"),
			@ApiResponse(code = 404, message = "Couldn't shorten URL"),
			@ApiResponse(code = 422, message = "Wrong URL or Invalid Input provided"),
			@ApiResponse(code = 502, message = "Couldn't check for already existing shortened URL ") })
	@PostMapping(value = "/shorten", produces = "application/json")
	public ResponseEntity<URLResponseBody> saveURLStatistics(@RequestBody @Valid final URLRequest uRLRequest)
			throws Exception {
		if (!uRLRequest.getUrl().isEmpty() && !uRLRequest.getUrl().equals("")) {
			String shortenedUrl = "";
			String uniqueID = "";
			URLResponseBody urlResp = new URLResponseBody();
			LOGGER.info("Received url to shorten: " + uRLRequest.getUrl());
			String longUrl = uRLRequest.getUrl();
			// removing www from URL to avoid creating shorten link for identical sites
			if (longUrl.contains("www.")) {
				longUrl = longUrl.replaceAll("www.", "");
				LOGGER.info("www removed from URL {}" + longUrl);
			}
			if (validateURL(longUrl, localDomain)) {
				LOGGER.info("URL is valid {}" + longUrl);
				Optional<URLStatistics> urlStat = urlConverterService.findByOrigURL(longUrl);
				if (!urlStat.isPresent()) {
					shortenedUrl = urlConverterService.createShortenURL(localDomain, uRLRequest.getUrl());
					LOGGER.info("Shortened url to: " + shortenedUrl);
					if (shortenedUrl != null) {

						Optional<URLStatistics> urlStatistics = urlConverterService.saveShortenURLWhenNew(shortenedUrl,
								longUrl);

						if (urlStatistics.isPresent()) {
							urlResp.setShortURLResponse(shortenedUrl);
							uniqueID = shortenedUrl.substring(shortenedUrl.lastIndexOf('/') + 1);
							Link linkToStatistics = linkTo(methodOn(this.getClass()).getURLStatistics(uniqueID))
									.withRel("statistics");
							urlResp.add(linkToStatistics);
						} else {
							throw new JedisException("Shortened URL failed to save ");
						}
					} else {
						LOGGER.error("URL not shortened");
						throw new URLNotFoundException("URL not shortened !!");
					}
				} else {
					urlConverterService.updateShortenURLWhenExists(longUrl);
					urlResp.setShortURLResponse(urlStat.get().getUrl());
					uniqueID = urlStat.get().getUrl().substring(urlStat.get().getUrl().lastIndexOf('/') + 1);
					Link linkToStatistics = linkTo(methodOn(this.getClass()).getURLStatistics(uniqueID))
							.withRel("statistics");
					urlResp.add(linkToStatistics);
				}
				urlResp.setMessage("Success");
				Link linkAll = linkTo(methodOn(this.getClass()).getAllURLStatistics()).withRel("allShortenURLs");
				urlResp.add(linkAll);

				return new ResponseEntity<URLResponseBody>(urlResp, HttpStatus.OK);

			}
			LOGGER.error("Invalid URL");
			throw new UnProcessableEntityException("URL is either invalid or not responsive");
		}
		LOGGER.error("Blank URL");
		throw new UnProcessableEntityException("Please provide a URL.. URL found Blank");
	}

	/***
	 * Rest End point to get the statistics for a particular shortened URL by
	 * passing the unique id URI: https://<your domain>/url/statistics/bqw
	 * 
	 * @param id (bqw)
	 * @return Response Entity with an instance of URLStatistics with all the
	 *         statistical information of the URL along with link to open list of
	 *         all shortened URLs
	 */
	@ApiOperation(value = "Fetch Statistics of a shortened URL by ID", response = URLStatistics.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully found statistics for provided URL"),
				@ApiResponse(code = 404, message = "Couldn't find shortened URL") })
	@GetMapping(value = "/statistics/{id}", produces = { "application/hal+json" })
	public ResponseEntity<URLStatistics> getURLStatistics(@PathVariable String id) {

		String substr = localDomain + id;
		LOGGER.info("url : {}", substr);
		URLStatistics stat = urlConverterService.findByShortURL(substr);
		if (stat != null) {
			LOGGER.info("stat : {}", stat);
			Link linkAll = linkTo(methodOn(this.getClass()).getAllURLStatistics()).withRel("allShortenURLs");
			stat.add(linkAll);
			ResponseEntity<URLStatistics> result = new ResponseEntity<URLStatistics>(stat, HttpStatus.OK);
			return result;
		}
		LOGGER.error("URLStatistics returned as Null");
		throw new URLNotFoundException("Short URL entered doesn't exist");
	}

	/***
	 * Rest End point to return all the shortened URLs URI:
	 * https://<your domain>/url/allshortenURLs
	 * 
	 * @return Resources with List of URLStatistics along with link to individual
	 *         statistics of a URL
	 */
	@ApiOperation(value = "Fetch all URLs statistics", response = URLStatistics.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully found statistics for all URLs"),
							@ApiResponse(code = 404, message = "Couldn't find any shortened URL") })
	@GetMapping(value = "/allshortenURLs", produces = { "application/hal+json" })
	public Resources<URLStatistics> getAllURLStatistics() {
		List<URLStatistics> stat = urlConverterService.getAllURLStatistics();
		if (stat.size() != 0) {
			stat.forEach(url -> {
				String uniqueID = url.getUrl().substring(url.getUrl().lastIndexOf('/') + 1);
				Link link = linkTo(methodOn(this.getClass()).getURLStatistics(uniqueID)).withRel("statistics");
				url.add(link);
			});
			Resources<URLStatistics> result = new Resources<URLStatistics>(stat);
			return result;
		} else {
			throw new URLNotFoundException("No Shortened URLs exist");
		}
	}

	/***
	 * Rest End point to delete a particular shortened URL URI:
	 * https://<your domain>/url/deleteByURL
	 * 
	 * @param uRLRequest
	 * @return ResponseEntity with URLResponseBody displaying response of the delete
	 *         method
	 * @throws Exception
	 */
	@ApiOperation(value = "Delete Statistics of a shortened URL by ID", response = URLResponseBody.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully deleted statistics for provided URL"),
				@ApiResponse(code = 404, message = "Couldn't delete shortened URL") })
	@DeleteMapping(value = "/deleteByURL", produces = "application/json")
	public ResponseEntity<URLResponseBody> deleteURLStatistics(@RequestBody @Valid final URLRequest uRLRequest)
			throws Exception {
		LOGGER.info("Inside delete URL for {}: ", uRLRequest.getUrl());
		String longUrl = uRLRequest.getUrl();
		// removing www from URL to avoid creating shorten link for identical sites
		if (longUrl.contains("www.")) {
			longUrl = longUrl.replaceAll("www.", "");
			LOGGER.info("www removed from URL {}" + longUrl);
		}
		int result = urlConverterService.deleteStatisticsByURL(longUrl);
		if (result == 0) {
			URLResponseBody urlResp = new URLResponseBody();
			urlResp.setMessage("Success");
			urlResp.setShortURLResponse("Deleted URL = " + longUrl);
			Link linkDeleteAll = linkTo(methodOn(this.getClass()).deleteURLStatistics()).withRel("deleteAllURLs");
			urlResp.add(linkDeleteAll);
			return new ResponseEntity<URLResponseBody>(urlResp, HttpStatus.OK);
		}
		throw new URLNotFoundException("No such shortened URL found to be deleted ");
	}

	/***
	 * Rest End point to delete all the shortened URLs URI:
	 * https://<your domain>/url/deleteAll
	 * 
	 * @return ResponseEntity to return a URLResponseBody with response to deletion
	 *         of all URLs
	 * @throws Exception
	 */
	@ApiOperation(value = "Delete all shortened URLs", response = URLResponseBody.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully deleted all URLs"),
				@ApiResponse(code = 404, message = "Couldn't find any shortened URL") })
	@DeleteMapping(value = "/deleteAll", produces = "application/json")
	public ResponseEntity<URLResponseBody> deleteURLStatistics() throws Exception {
		LOGGER.info("Inside delete All URLs");
		int result = urlConverterService.deleteAllURLStatistics();
		if (result == 0) {
			URLResponseBody urlResp = new URLResponseBody();
			urlResp.setMessage("Success");
			urlResp.setShortURLResponse("Deleted All URLs");
			return new ResponseEntity<URLResponseBody>(urlResp, HttpStatus.OK);
		}
		throw new URLNotFoundException("No shortened URLs exist");
	}
}
