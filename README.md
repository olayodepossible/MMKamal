# MMKamal
## This project adopt microservice achitecture. It consist of 4 services (Eureka Service, Api-Gateway service, AuthService, and SmsService)

###
``` 
# Language: Java (Spring Boot framework)
# Database: Postgres 
The project implementation come with Flyway database migration, so once you have the repo cloned, 
setup your Database source url, username and password inside application.yml
# Run mvn clean install
start each service, starting from Eureka service
The Api-Gateway server which is an entry point for all other services run on localhost:8072. Hence, to gain access to other services:
AuthService : localhost:8072/api/v1/auth/users/**
Sms-Service : localhost:8072/api/v1/sms/**

# 
```
