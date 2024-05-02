# Transparent Democray Voting backend

Makes the [public information](https://www.dekamer.be/kvvcr/index.cfm) of the government regarding motions and their votes easily searchable. Note that this is just the backend that exposes the information through rest services.

## Documentation

* [Ubiquitous language](doc/ubiquitous-language.md)
* [Application Architecture](doc/application-architecture.md)
* [ADR - Architectural Decision Records](ADR/index.md)
* [Codebase Guidelines](doc/codebase-guidelines.md)

## Technical Choices


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

You can build everything locally using maven using the following commands

+ To build it from the terminal

```bash
./mvnw install
```

+ To run the tests only

```bash
./mvnw test
```

+ To run the Integration tests

+ To build a fat, executable jar

+ To Build the **docker** image from the fat, executable jar.

### Run

#### Run the main application

To run the application, execute the script

```bash  
./runApplication     
```  
This will build and run the application.

### Docker scripts

