# Getting Started

## Pre-requisite

Java 11

## App Links 

* URL: [http://localhost:8080](http://localhost:8080)
* Swagger UI - [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)
* H2 DB Console - [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  * URL: jdbc:h2:~/uberDB
  * username: sa
  * password: password

## APIs
* Driver APIs 
  * Register Driver - POST - http://localhost:8080/api/driver/register
  * Register Vehicle - POST - http://localhost:8080/api/driver/registerVehicle
  * Get Driver Profile - GET - http://localhost:8080/api/driver/profile/{id}
  * Get Driver Payments - GET - http://localhost:8080/api/driver/payments/{id}
  * Get Driver Trips - GET - http://localhost:8080/api/driver/trips/{id}
* Trip APIs
  * Get Trip Details - GET - http://localhost:8080/api/trip/details/{id}
  * Acquire Trip - POST - http://localhost:8080/api/trip/acquire
  * Start Trip - POST - http://localhost:8080/api/trip/start
  * Terminate Trip - POST - http://localhost:8080/api/trip/terminate


