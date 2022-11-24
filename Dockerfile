FROM maven:3-openjdk-18 AS builder
WORKDIR /usermanager/app/src/

COPY src ./
COPY pom.xml ../
RUN mvn -f /usermanager/app/pom.xml clean package -DskipTests

FROM openjdk:18
WORKDIR /usermanager/lib/
COPY --from=builder /usermanager/app/target/userservice-0.0.1-SNAPSHOT.jar ./userservice.jar
EXPOSE 9080
ENTRYPOINT ["java","-jar","/usermanager/lib/userservice.jar"]