## TODO use springboot plugin to build the image

./mvnw install
./mvnw -pl code/main exec:java -Dexec.mainClass="be.tr.democracy.main.GenerateDomainModelApplication"
docker buildx build --platform linux/amd64 -t transparent-democracy/voting-backend .