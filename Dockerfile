FROM adoptopenjdk:11-jre-hotspot

WORKDIR /app

COPY build/libs/my-app-*.jar app.jar

# Instalar PostgreSQL
RUN apt-get update && \
    apt-get install -y postgresql && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Copiar el archivo de configuración de la base de datos
COPY src/main/resources/application.yml .

# Agregar el usuario y la base de datos de PostgreSQL
USER postgres
RUN /etc/init.d/postgresql start && \
    psql --command "CREATE USER myuser WITH SUPERUSER PASSWORD 'mypassword';" && \
    createdb -O myuser mydb

# Exponer el puerto 8080 para la aplicación Micronaut
EXPOSE 8080

# Cambiar al usuario "root" y ejecutar la aplicación
USER root
ENTRYPOINT ["java","-jar","app.jar"]
