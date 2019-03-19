
## Project title
An application exposing API for URL shortening and persisting them.

## Motivation
Code devlepoment and design assignment.

## Tech/framework used

<b>Built with</b>
- JDK 8 or later (https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html)
- Spring Boot(https://spring.io/projects/spring-boot)
- Spring Rest (https://spring.io/guides/gs/rest-service/)
- Spring Data Redis(https://spring.io/projects/spring-data-redis)
- Redis (https://redis.io/)
- Spring HATEOAS (https://spring.io/projects/spring-hateoas)
- Swagger UI (https://swagger.io/tools/swagger-ui/)
- Docker (https://www.docker.com/)


## Installation
 ```
This application can be run using Eclipse or the docker image and Redis database setup.
To setup Redis database download the zip file from this link (http://download.redis.io/redis-stable.tar.gz) and then run redis-server.exe, now the application can be eassily made to run fully functional.
 ```
 
## Project Structure
```
This is a REST-API application for URL Shortening which provides following functionalities:
1. Original URL are shortened with a unique key generated using Base 62 characters (A-Z, a-z, 0-9) and their respective statistics
   are saved in the Redis Hash (In-Memory Database)
   Note : In this app, URL will be validated for HTTP/HTTPS request and response in 2XX. Any response other than 2XX will be prevented
   from shortening
   An "ID" key is used for incrementing the id (starting from 100 in this application) to ensure unique key creation for URL shortening
   If a URL is already shortened and exists in the database, the existing short URL will be returned as response else
   a the incoming URL will be shortened and persisted in the database
2. Shortened URLs can be redirected to their respective original URLs
   URLs are shortened on the local domain e.g. http://<your domain>/url/{key}
3. Statistics such as No of Requests for shortening, No of Redirects to original URL and Last Access Time can be fetched
   for all URLs and for specific shortened URL
   No of Requests will be 1 and No of Redirects will be 0 initially
   Everytime a URL is provided for shortening or redirecting, last time accessed will change to that time stamp.
4. URL statistics can be deleted for a original URL or all URLs which are shortened and saved in the database
   Note: Deletion depends on the requirement of exposure to be given to clients. 
5. Responses are binded with HyperLinks to other relevant rest end points using SPRING HATEAOS implementation
The REST end points of the application are exposed through Swagger UI for Clients

```
```
Link to Swagger UI - (http://<your domain>/swagger-ui.html#/)
```
## Screenshots


Swagger screenshots
1. Request for shortening of URL - 
![alt text](https://github.com/ritika1993/Neueda/blob/master/Swagger/Output_ScreenShots/shorten-request-swagger.png)

2. Find statistics of a URL by unique key -
![alt text](https://github.com/ritika1993/Neueda/blob/master/Swagger/Output_ScreenShots/display-stats-swagger.png)

3.Find statistics of all URLs -
![alt text](https://github.com/ritika1993/Neueda/blob/master/Swagger/Output_ScreenShots/display-all-swagger.png)

4. Delete a URL by original URL link -
![alt text](https://github.com/ritika1993/Neueda/blob/master/Swagger/Output_ScreenShots/delete-by-url-swagger.png)

5. Delete all URLs - 
![alt text](https://github.com/ritika1993/Neueda/blob/master/Swagger/Output_ScreenShots/delete-all-urls-swagger.png)





Postman Screenshots
1. Redirect to original URL -
![alt text](https://github.com/ritika1993/Neueda/blob/master/redirect-url-postman.png)

Include logo/demo screenshot etc.

## API Features

 #API end points URLs (Swagger)
![alt text](https://github.com/ritika1993/Neueda/blob/master/swagger-rest-urls.png)

```
 Redirect to original URL from short URL - http://<yourdomain>/url/{key}
 ```
## Tests
```
Unit test cases have been written using JUnit in Spring Boot and the test methods perform the following tasks:
1. Test for shortening URL and then retreiving the orignal URL based on the shortened URl and than matching them both.
2. Test for statistics check(number of request, number of redirects) of the shortened URL
3. Test for deletion of URLs from the database
```

## Contribute
A fron-end can easily be made either using Thymleaf in the same application doing AJAX call to the exposed Rest end-points,
or a seperate front-end application can be made which can consume the given rest end points.


## License
Anybody can use this project for learning purpose.
