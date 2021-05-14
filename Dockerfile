FROM maven:3.6-jdk-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean install

FROM jetty
COPY --from=build target/simple-servlet-0.1.war /var/lib/jetty/webapps/root.war
EXPOSE 8080
