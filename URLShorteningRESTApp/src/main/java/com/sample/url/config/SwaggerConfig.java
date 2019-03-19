package com.sample.url.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sample.url.controller.ShortenURLController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/***
 * This class creates configuration for rest end points exposure to Swagger UI
 * 
 * @author Ritika Sao
 *
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = ShortenURLController.class)
public class SwaggerConfig {

	/***
	 * Creates a Docket Bean for configuring Swagger 2 
	 * Sets the controller package which needs to be exposed
	 * @return Docket 
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.sample.url.controller"))
				.paths(PathSelectors.regex("/.*")).build().apiInfo(apiInfo());
	}

	/***
	 * This method creates an ApiInfo to be displayed in the Swagger UI
	 * @return ApiInfo
	 */
	private ApiInfo apiInfo() {
		return new ApiInfo("URL SHORTENING REST API", "Rest Application for URL shortening.", "1.0.0",
				"Terms of service", new Contact("Ritika Sao", "www.linkedin.com/in/ritika-sao", "saoritika@gmail.com"),
				"License of API", "1.0.0", Collections.emptyList());
	}
}
