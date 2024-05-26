FROM openjdk:18-slim
COPY target/parco-0.0.1.jar parco.jar
ENTRYPOINT ["java","-jar","/parco.jar"]