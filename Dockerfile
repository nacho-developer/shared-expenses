FROM adoptopenjdk/openjdk17:alpine-jre

# Instalar Maven
RUN apk add --no-cache curl tar \
    && curl -L https://downloads.apache.org/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz \
    | tar -xz -C /usr/share \
    && mv /usr/share/apache-maven-3.8.4 /usr/share/maven \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Copiar archivos de la aplicación
COPY . /app
WORKDIR /app

# Construir aplicación con Maven
RUN mvn clean package

# Exponer puerto de la aplicación
EXPOSE 8080

# Ejecutar aplicación
CMD ["java", "-jar", "/app/target/shared-expenses.jar"]
