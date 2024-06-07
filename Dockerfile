FROM amazoncorretto:22
# Copying the cache files of the readmodels
COPY /target /target
# Copying the actual application
COPY code/main/target/main-1.0.0-SNAPSHOT.jar voting-backend.jar

ENTRYPOINT ["java","-jar","/voting-backend.jar"]