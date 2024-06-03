## TODO use springboot plugin to build the image
## Todo the caches will only be generated when the application started up before, The mvn clean removes them intentionally
./mvnw -q install
docker buildx build --platform linux/amd64 -t transparent-democracy/voting-backend .