FROM amazoncorretto:11-alpine
WORKDIR /app
COPY target/credit_card-0.0.1.jar /app/app.jar
ENV SPRING_CONFIG_PORT=8088
ENV SPRING_CLOUD_CONFIG_URI=http://config-server:8888
EXPOSE $SPRING_CONFIG_PORT

# Establecer el comando para ejecutar la aplicación
CMD ["java", "-Dserver.port=${SPRING_CONFIG_PORT}", "-Dspring.cloud.config.uri=${SPRING_CLOUD_CONFIG_URI}", "-jar", "/app/app.jar"]