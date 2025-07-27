#Frontend Build

#Node Image
FROM node:20-alpine AS frontend-build
#Erstelle/Wechsle working directory
WORKDIR /app
#Kopiere den frontend-Ordner in app
COPY frontend/ ./frontend/
#Welchsle nun in das kopierte frontend-Ordner
WORKDIR /app/frontend
#Führe die Befehle aus um das Frontend zu erstellen
RUN npm install && npm run build

#Backend Build

#Java Build
FROM maven:3.9-eclipse-temurin-21 AS backend-build
#Wechsle in die working directory
WORKDIR /app
#Kopiere die pom.xml um die Dependencys für den build zu haben
COPY pom.xml ./
#Kopiere den src-Ordner, welcher das gesamte Backend beinhaltet in app
COPY src/ ./src/
#Starte Maven und kompiliere die pom.xml
#DskipTests überspringt die Ausführung von Tests
RUN mvn clean package -DskipTests

#Final Image

#Java Build
FROM eclipse-temurin:21-jdk
#Wechsle in die working directory
WORKDIR /app

#Kopiere die backend JAR die beim builden im target Ordner gelandet ist
COPY --from=backend-build /app/target/*.jar app.jar

# Optional: Kopiere das gebaute Frontend in einen Pfad (z. B. für Spring Static Resources)
COPY --from=frontend-build /app/frontend/dist/ ./static/

ENTRYPOINT ["java", "-jar", "app.jar"]