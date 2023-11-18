#
# Build stage
#
FROM gradle:8.1.1-jdk17 as compiler

ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME

# copy source code
COPY build.gradle.kts settings.gradle.kts gradlew $APP_HOME
COPY gradle.properties $APP_HOME
COPY gradle gradle
COPY micronaut-cli.yml micronaut-cli.yml
COPY src src

# create application jar
RUN gradle clean build -x test

# move application jar
RUN mv ./build/libs/*-all.jar service.jar
#
# Run stage
#
FROM openjdk:17-jdk-alpine


ENV APP_HOME=/usr/app/
ENV MAX_RAM_PERCENTAGE="-XX:MaxRAMPercentage=70"
ENV MIN_RAM_PERCENTAGE="-XX:MinRAMPercentage=70"

COPY --from=compiler $APP_HOME/service.jar $APP_HOME/service.jar

WORKDIR $APP_HOME

ENV JAVA_OPTS="$SECURITY_OPTS $MAX_RAM_PERCENTAGE $MIN_RAM_PERCENTAGE"
ENTRYPOINT exec java $JAVA_OPTS -jar service.jar
EXPOSE 8080