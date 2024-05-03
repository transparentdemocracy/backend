# Transparent Democray Voting backend

Makes the [public information](https://www.dekamer.be/kvvcr/index.cfm) of the government regarding motions and their votes easily searchable. Note that this is just the backend that exposes the information through rest services.

## Documentation

* [Ubiquitous language](doc/ubiquitous-language.md)
* [Application Architecture](doc/application-architecture.md)
* [ADR - Architectural Decision Records](ADR/index.md)
* [Codebase Guidelines](doc/codebase-guidelines.md)

## Technical Choices

For now, we have opted not using a database. The data that is served is loaded from prepared json files that are included in the jar and loaded into memory when the application starts up. The data files are not part of this project. It is expected that the companion project voting-data is located next to this project. Upon building the application, maven will copy those resources into the jar.

```log
...
INFO 27436 --- [           main] be.tr.democracy.main.VotingApplication   : No active profile set, falling back to 1 default profile: "default"
INFO 27436 --- [           main] b.t.d.inmem.DataFileMotionsReadModel     : Loading DataFileMotions from data/plenaries.json
INFO 27436 --- [           main] b.tr.democracy.inmem.JSONDataFileLoader  : Loading 300 PlenaryDTO in 123 milisec from data/plenaries.json
INFO 27436 --- [           main] b.tr.democracy.inmem.JSONDataFileLoader  : Loading 192 PoliticianDTO in 4 milisec from data/politicians.json
INFO 27436 --- [           main] b.tr.democracy.inmem.JSONDataFileLoader  : Loading 381567 VoteDTO in 212 milisec from data/votes.json
INFO 27436 --- [           main] b.t.d.inmem.DataFileMotionsReadModel     : Data loaded in memory.
...
```

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

### Run

#### Run the main application

To run the application, execute the script

```bash  
./runApplication     
```  
This will build and run the application. In order to have a frontend, you will need the companion project voting-website which will connect to this server. The configuration is currently limited to both application running on the same machine, 
