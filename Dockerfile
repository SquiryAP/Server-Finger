# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Кэшируем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем остальные файлы проекта
COPY src ./src

# Сборка jar-файла
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем jar из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Открываем порт (по умолчанию Spring Boot использует 8080)
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
