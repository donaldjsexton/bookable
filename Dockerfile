FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /build
COPY backend/pom.xml backend/pom.xml
COPY backend/src backend/src

RUN mvn -f backend/pom.xml -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /opt/booking-crm
ENV JAVA_OPTS=""
ENV SERVER_PORT=8085
ENV SPRING_WEB_RESOURCES_STATIC_LOCATIONS=classpath:/static/,file:/opt/booking-crm/frontend/

COPY --from=build /build/backend/target/booking-crm-0.0.1-SNAPSHOT.jar /opt/booking-crm/app.jar
COPY frontend /opt/booking-crm/frontend

EXPOSE 8085
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /opt/booking-crm/app.jar"]
