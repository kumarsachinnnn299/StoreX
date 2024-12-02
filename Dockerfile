# https://www.youtube.com/watch?v=p3AIecyvok4&t=612s
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/SpringBoot-Ecom-0.0.1-SNAPSHOT.jar SpringBoot-Ecom.jar

EXPOSE 8080
ENTRYPOINT [ "java","-jar","SpringBoot-Ecom.jar" ]