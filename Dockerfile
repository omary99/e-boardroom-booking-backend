FROM eclipse-temurin:21-jdk-alpine as booking-base-env
VOLUME /tmp

# Create report directories
FROM booking-base-env as booking-reports-resources-env
RUN mkdir -p /storage/booking_report \
    && mkdir -p /reports/templates

# Set timezone
FROM booking-reports-resources-env as booking-timezone-env
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Africa/Dar_es_Salaam /etc/localtime \
    && echo "Africa/Dar_es_Salaam" > /etc/timezone

# Add Times New Roman fonts directly
FROM booking-timezone-env as booking-reports-library-env
RUN apk update && apk add --no-cache \
        fontconfig ttf-dejavu \
    && mkdir -p /usr/share/fonts/truetype/custom-fonts

# Rebuild font cache
RUN fc-cache -f -v

RUN apk add --no-cache libxext libxtst libxrender

# Copy JAR file to container
FROM booking-reports-library-env as booking-target-files-env
ARG JAR_FILE=boardroom-booking/target/*.jar
COPY ${JAR_FILE} app.jar

# Set entrypoint for application
FROM booking-target-files-env as booking-entrypoint-env
CMD ["java", "-jar", "app.jar"]
