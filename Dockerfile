#Build stage
FROM amazoncorretto:21-alpine3.18 as build

WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src
RUN ./mvnw package

# Run stage
FROM amazoncorretto:21-alpine3.18

WORKDIR /opt/app
COPY --from=build /opt/app/target/*.jar ./api.jar

CMD ["java","-jar","api.jar"]
EXPOSE 8080
