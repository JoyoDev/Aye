FROM openjdk:11-jre-slim-buster
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} aye.jar

# run as non-root user
RUN addgroup   --system --gid 1001 aye
RUN adduser  --system --uid  1001   --group aye
RUN  chown -R aye:aye /opt
RUN mkdir /logs && chown -R aye:aye /logs

# Install python (required for anchore cli)
RUN apt-get update
RUN apt-get -y install python3
RUN apt-get -y install python3-setuptools
RUN apt-get -y install python3-pip

# Install anchore cli
RUN pip3 install anchorecli

ENTRYPOINT ["java","-jar","/aye.jar"]