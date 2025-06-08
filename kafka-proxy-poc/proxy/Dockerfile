FROM eclipse-temurin:21-jdk as builder

# download and untar the kroxylicious image from https://github.com/kroxylicious/kroxylicious/releases/download/v0.12.0/kroxylicious-app-0.12.0-bin.tar.gz
RUN apt-get update && apt-get install -y wget tar unzip
RUN wget -O /tmp/kroxylicious-app-0.12.0-bin.tar.gz https://github.com/kroxylicious/kroxylicious/releases/download/v0.12.0/kroxylicious-app-0.12.0-bin.tar.gz && \
    mkdir -p /opt/kroxylicious && \
    tar -xzf /tmp/kroxylicious-app-0.12.0-bin.tar.gz -C /opt/kroxylicious --strip-components=1 && \
    rm /tmp/kroxylicious-app-0.12.0-bin.tar.gz && \
    mkdir /opt/gradle && \
    wget https://services.gradle.org/distributions/gradle-8.14.2-bin.zip && \
    unzip gradle-8.14.2-bin.zip -d /opt/gradle && \
    ln -s /opt/gradle/gradle-8.14.2/bin/gradle /usr/bin/gradle


WORKDIR /opt/filter
COPY mock-filter/* .
RUN gradle clean build -x test
# Copy the built filter jar to the kroxylicious libs directory

FROM eclipse-temurin:21-jre
VOLUME ["/opt/kroxylicious/config"]
COPY --from=builder /opt/kroxylicious /opt/kroxylicious
COPY --from=builder /opt/filter/build/libs/*.jar /opt/kroxylicious/libs/filter.jar

RUN chmod +x /opt/kroxylicious/bin/kroxylicious-start.sh && \
    chmod +x /opt/kroxylicious/bin/run-java.sh
WORKDIR /opt/kroxylicious
EXPOSE 9092
CMD ["sh", "-c", "./bin/kroxylicious-start.sh --config=config/proxy-config.yaml"]
