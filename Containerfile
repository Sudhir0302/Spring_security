FROM docker.io/eclipse-temurin:21-jdk
ADD target/2FA-AUTH.jar 2FA-AUTH.jar
ENTRYPOINT ["java","-jar","/2FA-AUTH.jar"]
