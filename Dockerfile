FROM jetty
ADD target/simple-servlet-0.1.war /var/lib/jetty/webapps/root.war
EXPOSE 8080
