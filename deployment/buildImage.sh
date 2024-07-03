## TODO use springboot plugin to build the image

./mvnw install
docker buildx build --platform linux/amd64 -t transparent-democracy/voting-backend .