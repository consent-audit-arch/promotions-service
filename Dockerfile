FROM eclipse-temurin:21-jre-jammy
RUN groupadd -r appuser && useradd -r -g appuser -m appuser
WORKDIR /app
COPY promotion-service/target/*.jar app.jar
COPY promotion-service/opa/policies /opa/policies
COPY promotion-service/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
RUN chown appuser:appuser app.jar /entrypoint.sh
EXPOSE 8080
ENV JAVA_OPTS=
ENTRYPOINT ["/entrypoint.sh"]
