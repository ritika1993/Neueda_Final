package com.sample.url.repositoryConfig;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;


import redis.clients.jedis.Jedis;
/***
 * This class creates a unique ID for shortened URL generation
 * @author Ritika Sao
 *
 */
@Repository
@PropertySource(value = "classpath:application.properties")
public class URLRepositoryConfig  {

	private static final Logger LOGGER = LoggerFactory.getLogger(URLRepositoryConfig.class);

	private Jedis jedis;
	private static final String INCREMENTAL_ID = "id";
	
	@Value("${incremental.id}")
	private String incrementalIDValue;
	
	public URLRepositoryConfig() {

		this.jedis = new Jedis();

	}

	/***
	 * This method is to perform operation before initialisation of the bean of this class
	 * 
	 * It will check if the incremental ID exists or not as a key "ID"
	 * If not, it will create a new key
	 * If yes, it will check if it is more than 100 or not
	 * 		If no, it will set the ID to 100
	 * 
	 * Note: For this application , id is assumed as 100
	 */
	@PostConstruct
	public void init() {
		LOGGER.info("Inside Repository Config Init method");
		if(jedis.get(INCREMENTAL_ID)!= null) {
			Long initValue = Long.parseLong(jedis.get(INCREMENTAL_ID));
			
			if(initValue < 100) {
				LOGGER.info("Updating Increment ID");
				jedis.set(INCREMENTAL_ID, incrementalIDValue);
			}

		}else {
			LOGGER.info("Creating Increment ID");
			jedis.set(INCREMENTAL_ID, incrementalIDValue);
		}
				
	}
	/***
	 * This method increments value of the ID everytime by 1 to ensure unique key creation
	 * for the shortened URL
	 * @return incremented ID
	 */
	public Long incrementID() {

		Long id = jedis.incr(INCREMENTAL_ID);
		LOGGER.info("Incrementing ID: {}", id);
		return id;
	}


}
