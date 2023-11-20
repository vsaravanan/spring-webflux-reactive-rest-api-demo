# spring-webflux-reactive-rest-api-demo
 
Building Reactive app with Spring Webflux. 
[![Project Status: Active – The project has reached a stable, usable state and is being actively developed.](https://www.repostatus.org/badges/latest/active.svg)]

**Examples**

* Reactive Endpoints
* Mono, Flux structures
* Functional Reactive Endpoints
* WebClient & WebTestClient
* R2DBC with PostgreSQL

## Requirements
1.  Java - 21
2.  Maven- 3.9.4
3.  Docker- 20.x.x

**Running the Database**
Type the following command in your terminal to run the database -

     docker-compose up 

**Running the App**
Type the following command in your terminal to run the app -

     mvn clean install

The app will start running at  [http://localhost:8080](http://localhost:8080/).

**Running the Tests**
Type the following command in your terminal to run the tests -

     ./gradlew test

---

## Requests

<code>
 
    ###
    http://localhost:8080/swagger-ui/index.html

    ###  
    GET http://localhost:8080/api/v1/users  

    ###  
    GET http://localhost:8080/api/v1/users/init
      
    ###  
    POST http://localhost:8080/api/v1/users/webflux
    Content-Type: application/json  
      
    {  
      "name": "Santhosh",  
      "score": 52  
    }  
      
    ###  
    PUT http://localhost:8080/api/v1/users/24  
    Content-Type: application/json  
      
    {  
      "name": "saravandev",  
      "score": 52  
    }  
    
    ###  
    DELETE http://localhost:8080/api/v1/users/24  

    ###  
    GET http://localhost:8080/api/v1/users/1  
      
    ###  
    GET http://localhost:8080/api/v1/users/flux  
      
    ###  
    GET http://localhost:8080/api/v1/users/guests/1
</code>
