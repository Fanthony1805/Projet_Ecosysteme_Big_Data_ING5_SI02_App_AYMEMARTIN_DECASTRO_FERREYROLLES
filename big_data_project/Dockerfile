FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
# change keytab Path
COPY /home/a.ferreyrolles-ece/mykey.keytab mykey.keytab

COPY /etc/hadoop/conf/core-site.xml core-site.xml
COPY /etc/krb5.conf krb5.conf
COPY /etc/hbase/conf/hbase-site.xml hbase-site.xml
ENTRYPOINT ["java","-jar","/app.jar"]