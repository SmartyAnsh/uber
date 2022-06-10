# Getting Started

Uber Backend for Driver's Mobile App

## Pre-requisite

Java 11

## App Links 

* URL: [http://localhost:8080](http://localhost:8080)
* Swagger UI - [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* H2 DB Console - [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  * URL: jdbc:h2:~/uberDB
  * username: sa
  * password: password

## Main APIs
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
* Payment APIs
  * Receive Payment Callback - POST - http://localhost:8008/api/trip/paymentCallback

## Helper APIs 

* Customer APIs
  * Create Trip - POST - http://localhost:8008/api/customer/trip
  * Rate Driver after Trip Completion - POST - http://localhost:8008/api/customer/rateDriver

## BootStrapped Data to use APIs

* Customer
  * Id: 1, Name: John Smith
  * Id: 2, Name: Michael Jordan
  
* Driver
  * Id: 6, Name: Ionut Popa
  * Id: 11, Name: Adrian Crisan



