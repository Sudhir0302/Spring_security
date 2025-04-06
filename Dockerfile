FROM openjdk:25-oracle
ADD target/2FA-AUTH.jar 2FA-AUTH.jar
ENTRYPOINT ["java","-jar","/2FA-AUTH.jar"]



#To build your docker image
#docker build -t sudhir03/2fa-auth:v1 .