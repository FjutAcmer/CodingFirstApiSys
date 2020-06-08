FROM java:8
VOLUME /tmp
ADD target/cf.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]