FROM openjdk:8-jdk-alpine
EXPOSE 8065
ADD target/device-manager-0.0.1-SNAPSHOT.jar /device-manager.jar
ENV TZ Asia/Shanghai
COPY localtime /etc/localtime
ENTRYPOINT [  "java", "-jar", "-Xms2048m", "-Xmx4000m", "-XX:+HeapDumpOnOutOfMemoryError", "/device-manager.jar", "--spring.profiles.active=test" ]