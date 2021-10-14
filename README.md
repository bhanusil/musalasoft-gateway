~~~~
Requirement

This sample project is managing gateways - master devices that control multiple peripheral devices.
Your task is to create a REST service (JSON/HTTP) for storing information about these gateways and their associated devices. This information must be stored in the database.
When storing a gateway, any field marked as “to be validated” must be validated and an error returned if it is invalid. Also, no more than 10 peripheral devices are allowed for a gateway.
The service must also offer an operation for displaying information about all stored gateways (and their devices) and an operation for displaying details for a single gateway. Finally, it must be possible to add and remove a device from a gateway.

Each gateway has:
* a unique serial number (string),
* human-readable name (string),
* IPv4 address (to be validated),
* multiple associated peripheral devices.

Each peripheral device has:
* a UID (number),
* vendor (string),
* date created,
* status - online/offline.

Other considerations
Please, provide
-	Basic UI - recommended or (providing test data for Postman (or other rest client) if you do not have enough time.
-	Meaningful Unit tests.
-	Readme file with installation guides.
-	An automated build.
~~~~

## Overview
To full fill the requirement above I have created a project using spring boot. In this project I have used multiple 
software components. Below described the usage of each software component. 
* MySQL - Store Data
* Flyway - For database version control
* Lombok - To generate constructors, getters and setters
* MapStruct - To map the data between DTO and Entity classes

## Prerequisites
* Java 1.8
* MySQL
* Apache Maven

## Setup & Build
1. Please create database new schema inside your MySQL server. You can use below command to create a schema.
*     create schema {schema name};
      Ex : create schema gatewaydb;

2. Please go to the **`application.properties`** file and change below parameters which align with your MySQL 
   connection.
*     spring.datasource.url=jdbc:mysql://{ip}:{port}/{schema name}
      spring.datasource.username={username}
      spring.datasource.password={password}

      flyway.url = jdbc:mysql://{ip}:{port}/{schema name}
      flyway.schemas = {schema name} 
      flyway.user = {username}
      flyway.password = {password}

      Ex :  spring.datasource.url=jdbc:mysql://localhost:3306/gatewaydb
            spring.datasource.username=root
            spring.datasource.password=password
            flyway.url = jdbc:mysql://localhost:3306/gatewaydb
            flyway.schemas = gatewaydb
            flyway.user = root
            flyway.password = password

3. Use this maven command to build the application.
*     mvn clean install

4. Above command was successful means it will generate the **gateway-0.0.1-SNAPSHOT.jar** file under **/target** folder. To run the jar file please 
   navigate to the correct folder location then run this command. 
*     java -jar gateway-0.0.1-SNAPSHOT.jar

5. Then application will start under **`9001`** port. You can use provided postman collection to test the 
   application. `Initially application without any data. So you have to use create API's'`
   
    `Aside to that please make sure create the gateway before create the device. Because device required gateway as
    foreign key.`

#### _Created by : Bhanuja De Silva (bhanusil11@gmail.com) / Date : 10.10.2021_
