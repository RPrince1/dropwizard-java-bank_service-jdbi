FROM morrisons/loyalty-clubs:morrisonJava8base

MAINTAINER ICC

# Make application directories
RUN mkdir -p /app/mus/service/bin && \
    mkdir -p /app/mus/service/config && \
    mkdir -p /app/mus/service/logs

# Copy application build artifacts (configs for all envs passed in; change which with docker run command argument)
COPY loyalty-clubs-standalone.jar /app/mus/service/bin/
COPY runApp /app/mus/service/bin/
COPY application*.yml /app/mus/service/config/

# Run the application
WORKDIR /app/mus/service/bin
ENTRYPOINT  ["/bin/bash"]