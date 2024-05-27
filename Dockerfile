FROM maven:3.8-openjdk-18
ARG stripe_private_key
COPY target/parco-0.0.1.jar parco.jar
# create folders for pictures
RUN mkdir "listingPictures"
RUN mkdir "profilePictures"
ENV stripe_private_key $stripe_private_key
ENTRYPOINT ["java","-jar","/parco.jar"]