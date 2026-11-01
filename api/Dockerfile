FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace
COPY .mvn ./.mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q dependency:go-offline
COPY src ./src
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
RUN groupadd --system app && useradd --system --gid app --home /app app \
 && mkdir -p /app/storage/documentos \
 && chown -R app:app /app
COPY --from=build --chown=app:app /workspace/target/*.jar /app/app.jar
USER app
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]