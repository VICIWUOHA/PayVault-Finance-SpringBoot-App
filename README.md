# SpringBoot PayVault Finance API

In this project i am building a simple financial PayCard application using Java SpringBoot framework. This allows Users to own Virtual Cards with varying balances.
As a Data Engineer, i am working with a broader scope of building fullstack data platforms/ apps that can serve data via extensible API's. Springbooot was a great choice . I could have achieved same with python's FastAPI/ Django/Flask. But the JVM seems to be a new found love being the base language of many Apache tools.


## Getting Started


### Controllers
Controllers in Spring are just similar to controllers in most MVC frameworks. They simply handle and respond to web requests.

### Repository
Extends the Spring data CrudRepository Interface allowing us to implement some common functionalities easily which our Controller may need. eg; findById



#### Authentication
Within Our Application , we want to be sure that endpoints are accessed only if a user is authenticated and authorized to do so.
Here is our logic:

- IF the user/customer is authenticated

- ... AND they are authorized as a "PayCard owner"

- ... ... AND they own the requested PayCard

- THEN complete the user's request (GET/POST/PUT)

- BUT don't allow users to access PayCards they do not own.


## Running The Application

- Simply startup the Spring Application by running the command below in your terminal
    
        `./gradlew bootRun`

- Run Tests with `./gradlew test`
- Access endpoints from via http://localhost/8080
- Stop Application Server with `./gradlew -stop`


## CRUD Operations
- Test The API Endpoints via curl or POSTMAN with the request formats below (using any of the test users in [/SecurityConfig](https://github.com/VICIWUOHA/PayVault-Finance-SpringBoot-App/blob/main/src/main/java/vicmicroservices/payvault/SecurityConfig.java) ).


***CREATE***
````
 curl -X POST "http://localhost:8080/api/v1/paycards/create" \
-u VictorI:123abcxyz \
-H "Content-Type: application/json" \
-d '{"balance": 60}'

````

***GET***

````
 curl -X GET "http://localhost:8080/api/v1/paycards/list_paycards?sort=id" \
-u VictorI:123abcxyz
````


***UPDATE*** - _for a PayCard ith id of "1"_
`````
 curl -X PUT "http://localhost:8080/api/v1/paycards/1" \
-u VictorI:123abcxyz \
-H "Content-Type: application/json" \
-d '{"balance": 155}'
`````

***DELETE*** _for a PayCard ith id of "1"_

```` 
 curl -X DELETE "http://localhost:8080/api/v1/paycards/1" \
-u VictorI:123abcxyz

````