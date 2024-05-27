FROM maven:3.8-openjdk-18
COPY target/parco-0.0.1.jar parco.jar
ENTRYPOINT ["java","-jar","/parco.jar"]