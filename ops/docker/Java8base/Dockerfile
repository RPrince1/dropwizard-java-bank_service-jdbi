FROM centos
USER root
MAINTAINER ICC

RUN mkdir -p /app/java

# Install Java 8 (1.8.0_20 or above) and add to system PATH
RUN curl http://pgjnk01x.unix.morrisons.net:10000/software/java/java8/jdk1.8.0_31.tar.gz -o /app/java/jdk1.8.0_31.tar.gz && \
    tar xzvf /app/java/jdk1.8.0_31.tar.gz -C /app/java
ENV PATH $PATH:/app/java/jdk1.8.0_31/jre/bin

ENV JAVA_HOME /app/java/jdk1.8.0_31
