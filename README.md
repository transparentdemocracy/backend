# Transparent Democray Voting backend

Makes the [public information](https://www.dekamer.be/kvvcr/index.cfm) of the government regarding motions and their votes easily searchable. Note that this is just the backend that exposes the information through rest services.

## Documentation

* [Ubiquitous language](doc/ubiquitous-language.md)
* [Different Models](doc/different-models.md)
* [Application Architecture](doc/application-architecture.md)
* [ADR - Architectural Decision Records](ADR/index.md)
* [Codebase Guidelines](doc/codebase-guidelines.md)

## Technical Choices

For now, we have opted not using a database. The data that is served is loaded from prepared json files that are included in the jar and loaded into memory when the application starts up. The data files are not part of this project. It is expected that the companion project voting-data is located next to this project. Upon building the application, maven will copy those resources into the jar.

If you run the application directly from your IDEA, make sure to have run the mvn build once before. Otherwise, the data files from the other project will not be copied and thus not present.


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

### Setting up the server infrastructure

You can skip this if the server infrastructure has already been set up. Go immediately to the next section.

If you are setting up the infrastructure for the first time, follow these steps:
- Either follow this heavenly tutorial: https://earthly.dev/blog/deploy-dockcontainers-to-awsecs-using-terraform/
- Or skip it if you understand what all declarations in `deployment/terraform/application.tf` do.)

Both ways use `deployment/terraform/application.tf`. Make sure you fill your AWS access key ID and secret access key in there.
You can start the creation of the infrastructure, command by command from the following file, 
or you can just run it all at once with this command:

   ```bash
   cd backend/deployment
   ./runTerraform.sh
   ```

The created app_url is currently wddp-load-balancer-761216200.eu-west-3.elb.amazonaws.com. 
You can check this in your `terraform.tfstate`.

After a few seconds, max minutes, test the availability of this backend on the app_url, mentioned above.
For example, with: http://wddp-load-balancer-761216200.eu-west-3.elb.amazonaws.com/plenaries/?page=1&size=10.

And/or if the website (front-end) is already deployed, you can test there in a more visual way whether the backend works.

### Deploy

1. Install Docker Desktop 

   Also install Docker Desktop [on Mac](https://docs.docker.com/desktop/install/mac-install/), not `brew install docker`! 
   See https://github.com/replicate/cog/issues/1382#issuecomment-1869183604.)

2. Build the Docker image:

   ```bash
   cd backend
   ./deployment/buildImage.sh
   ```

3. Execute the docker image locally to test if it works:

   ```bash
   ./deployment/runImage.sh
   ```

   Note that the docker image was built for a linux/amd64 platform (with the intention to run it on AWS), it will not match if you run another
   platform (Mac/Windows). You'll get `WARNING: The requested image's platform (linux/amd64) does not match the detected host platform
   (linux/arm64/v8) and no specific platform was requested` and potentially errors that don't occur when running this application locally.

5. Push the docker image to the Elastic Container Registry (ECR) you've just created:

   ```bash
   ./deployment/pushImageToECR.sh
   ```

6. Run terraform again, such that the infrastructure will run a docker container with the new docker image that was just uploaded to the
   Elastic Container Registry (ECR):

   ```bash
   ./deployment/pushImageToECR.sh
   ```

7. Test the availability of the backend on the app_url Terraform has created.
   That's currently on http://wddp-load-balancer-761216200.eu-west-3.elb.amazonaws.com/plenaries/?page=1&size=10.

   And/or if the website (front-end) is already deployed, you can test there in a more visual way whether the backend works.
