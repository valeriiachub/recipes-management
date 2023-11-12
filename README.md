# Recipes Management Application

## Overview
This Recipes Management Application is a Spring Boot-based RESTful service that allows users to manage cooking recipes. 
It supports operations such as adding a new recipe, retrieving a list of recipes, retrieving a list of recipes by specified category etc. 

## Features
- REST API to create, read recipes
- Filtering recipes by categories
- Data persistence using JPA and H2 database

## Prerequisites
- JDK 11 or newer
- Maven 3.6.3 or newer

## Installation

To get the application running locally on your machine, follow the steps below:

1. Clone this repository
   ```shell
   git clone https://github.com/your-username/recipes-management.git
2. Navigate to the project directory
    ```shell
    cd recipes-management
3. Build the project using Maven
    ```shell
   mvn clean install
4. Run the application
    ```shell
   mvn spring-boot:run

The application should now be running at http://localhost:8081.
During the startup, the application will automatically load a predefined set of recipes into the H2 database, ensuring that you have sample data to work with immediately after the launch.
For a convenient way to test and interact with the application's API, a Postman collection ([Recipes.postman_collection.json](Recipes.postman_collection.json))has been provided. You can import this collection into Postman to quickly start sending requests and evaluating responses from the application's endpoints.