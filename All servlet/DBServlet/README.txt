DBServlet - Servlet CRUD with MySQL (Apache Tomcat 9)

Contents:
- index.html
- WEB-INF/web.xml
- WEB-INF/classes/ (put DatabaseServlet.class here after compilation)
- WEB-INF/lib/ (put your MySQL Connector/J JAR here)

1. Create the database and table in MySQL:

   CREATE DATABASE student;
   USE student;
   CREATE TABLE student (
       id INT PRIMARY KEY AUTO_INCREMENT,
       name VARCHAR(100) NOT NULL,
       email VARCHAR(100) NOT NULL
   );

2. Open DatabaseServlet.java (provided separately in this ZIP's source folder)
   and change PASSWORD if your MySQL root password is not "ssn1".

3. Compile DatabaseServlet.java using the Servlet API JAR from your Tomcat 9 install.
   Example:
   javac -cp "/path/to/apache-tomcat-9/lib/servlet-api.jar" -d WEB-INF/classes DatabaseServlet.java

4. Copy the MySQL Connector/J JAR into WEB-INF/lib.

5. Deploy the DBServlet folder into Tomcat's webapps directory, or package it as a WAR.
   Open http://localhost:8080/DBServlet/

Compatibility:
- This project uses javax.servlet and is intended for Apache Tomcat 9.
- For Tomcat 10+, javax.servlet imports must be changed to jakarta.servlet.
