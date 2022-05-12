# the first stage of our build will extract the layers
FROM adoptopenjdk:14-jre-hotspot as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} aye.jar
RUN java -Djarmode=layertools -jar aye.jar extract

# the second stage of our build will copy the extracted layers
FROM adoptopenjdk:14-jre-hotspot
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
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]