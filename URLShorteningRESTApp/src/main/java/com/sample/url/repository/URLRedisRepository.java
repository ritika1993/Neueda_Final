package com.sample.url.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sample.url.model.URLStatistics;
/***
 * Interface for DAO Operations
 * @author Ritika Sao
 *
 */
@Repository
public interface URLRedisRepository extends CrudRepository<URLStatistics, String>{

	
	
}
