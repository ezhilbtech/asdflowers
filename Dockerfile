FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY ASD_Flowers /app

RUN apt-get update && apt-get install -y maven

WORKDIR /app

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java","-jar","target/ASD_Flowers-0.0.1-SNAPSHOT.jar"]
