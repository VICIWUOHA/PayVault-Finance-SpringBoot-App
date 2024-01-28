##

In this project i am building a simple financial Paycard application using Java SpringBoot framework.
As a Data Engineer, i am working with a broader scope of building fullstack data platforms/ apps that can serve data via extensible API's. Springbooot was a great choice . I could have acheived same with python's FastAPI/ Django/Flask. But the JVM seems to be a new found love being the base language of many Apache tools.


## Getting Started


## Controllers
Controllers in Spring are just similar to controllers in most MVC frameworks. They simply handle and respond to web requests.


## Application


### Authentication
Within the Application , we want to be sure that endpoints are accessed only if a user is authenticated and authorized to do so.
Here is our logic:

- IF the user/customer is authenticated

- ... AND they are authorized as a "PayCard owner"

- ... ... AND they own the requested PayCard

- THEN complete the users's request

- BUT don't allow users to access PayCards they do not own.


## Repository