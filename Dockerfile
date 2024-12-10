FROM openjdk:23
EXPOSE 8080

COPY backend/target/CookMe.jar CookMe.jar

ENTRYPOINT ["java", "-jar", "CookMe.jar"]