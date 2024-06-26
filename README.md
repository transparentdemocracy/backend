# Transparent Democray Voting backend

Makes the [public information](https://www.dekamer.be/kvvcr/index.cfm) of the government regarding motions and their votes easily searchable. Note that this is just the backend that exposes the information through rest services.

## Documentation

* [Ubiquitous language](doc/ubiquitous-language.md)
* [Different Models](doc/different-models.md)
* [Application Architecture](doc/application-architecture.md)
* [Cloud Architecture](doc/deployment.md)
* [How to deploy](doc/deployment.md)
* [ADR - Architectural Decision Records](ADR/index.md)
* [Codebase Guidelines](doc/codebase-guidelines.md)

## Technical Choices

The data is stored in a database. Adding the data is done by calling the POST endpoints. TODO: explain authentication.

## Get started setup

### Repository setup

|**Folder**||**Description**|
|--- |---|---|
|ADR|Home of the Architectural Decision records|
|code|Here be the source code containing the actual application logic|
|deployment|Here be the code for building and deployment of the application.|
|doc|Home of the documentation |
|.mvn|Standard maven wrapper home. Nothing to see here.|
|scripts|Easy to use scripts that could help the developer with lots of tasks|

### Build

You will need to have Java 21 installed on your system.

You can build everything locally using maven and the following commands

+ To build it from the terminal

```bash
./mvnw install
```

+ To run the tests only

```bash
./mvnw test
```

### Run

#### Run the main application

To run the application, execute the script

```bash  
./runApplication     
```  
This will build and run the application. In order to have a frontend, you will need the companion project voting-website which will connect to this server. The configuration is currently limited to both application running on the same machine.

If you run the application directly from your IDEA, make sure to have run the mvn build once before. Otherwise, the data files from the other project will not be copied and thus not present. 

#### The application is a spring boot application with actuator enabled

+ http://localhost:8080/actuator/mappings will show the mappings
+ http://localhost:8080/actuator/info will show some information
+ http://localhost:8080/actuator/health will show its health status


### Deploy

For deployment see [How to deploy](doc/deployment.md)