# Fetch Rewards Coding Excercise

This is a spring boot application, used to complete the requirements of Fetch Rewards Backend Engineer Coding Exercise. The functional requirements that are covered for this challenge are:
- Add Transaction for a specific Fetch Rewards payer and date
- Spent points from Fetch Rewards payer by using the oldest points first
- Providing Fetch Rewards payer balance
- Storing transactions in memory (So everytime the service is restarted the data will be cleared)

Non-functional requirements covered:
- A README with installation and usage instruction ( You are reading this file now :smiley: )
- A Restful API service with 3 endpoints (add, spent, balance)
- Handled API exceptions (Showing human-readable message for unavailability or any other errors)
- Basic Unit Test cases written in Junit


## Requirements

For building the application you need:
- JDK 1.8 +
- Maven 3.x

For running the application you need:
- Java 8 or greater

## How to Run the application

This application is packaged as a jar, and it contains embedded tomcat. So no additional server is required to be installed.

Steps:
* Clone this repository
* Fulfill the requirements as per your need
* Go to the root directory of the application
* Option#1 (running the application)<br />
  To the run the project immediately without building, a jar has been added in ```/target``` directory. So by using the following commands you can run the application.
```
        java -jar target/points-0.0.1-SNAPSHOT.jar
```
* Option#2 (building the application and running: for this option you will require Maven 3.x)<br />
  If you want to build the project and run the application, then execute the following commands:
```
        mvn clean package
        
        java -jar target/points-0.0.1-SNAPSHOT.jar
        or
        mvn spring-boot:run
```
Once the application is started, in the console it will show:
```
2022-11-17 18:23:13.942  INFO 44162 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8710 (http) with context path ''
2022-11-17 18:23:13.959  INFO 44162 --- [           main] c.fetchrewards.points.PointsApplication  : Started PointsApplication in 8.022 seconds (JVM running for 8.878)
```

## About this service
This service will add a transaction of a Fetch Reward payer with timestamp, then spent points according to the oldest transaction and then finally provide the current balance of each payer.
To achieve the goal of this service, following three (3) endpoints has been used:
- Add Transaction ```http://localhost:8710/api/v1/points/add```
- Spent Points ```http://localhost:8710/api/v1/points/spent```
- Points Balance ```http://localhost:8710/api/v1/points/balance```

Note: You can use ```http://127.0.0.1:8710/``` instead of ```http://localhost:8710/```

Once the application is running, by using postman (my recommendation) or other alternatives, the APIs can be called.

### Add Transaction
- URL: ```http://localhost:8710/api/v1/points/add```
- Call Type: ```POST```
- Request Body: ```{
  "payer": "DANNON",
  "points": 0,
  "timestamp": "2022-11-02T14:00:00Z"
  }```
- Sample Success Response: ```{
  "status": true,
  "message": "success",
  "data": []
  }```

### Points Spent
- URL: ```http://localhost:8710/api/v1/points/spend```
- Call Type: ```POST```
- Request Body: ```{
  "points": 5000
  }```
- Sample Success Response: ```{
  "status": true,
  "message": "success",
  "data": [
  {
  "payer": "DANNON",
  "points": -100
  },
  {
  "payer": "MILLER COORS",
  "points": -4700
  },
  {
  "payer": "UNILEVER",
  "points": -200
  }
  ]
  }```

### Points Balance
- URL: ```http://localhost:8710/api/v1/points/balance```
- Call Type: ```GET```
- Sample Success Response: ```{
  "status": true,
  "message": "success",
  "data": {
  "UNILEVER": 0,
  "MILLER COORS": 5300,
  "DANNON": 1000
  }
  }```

Thank you!
