FROM openjdk:11-jre
RUN mkdir app
ARG JAR_FILE
ADD /target/${JAR_FILE} /app/challenge-authorize.jar
WORKDIR /app
ENTRYPOINT java -jar challenge-authorize.jar