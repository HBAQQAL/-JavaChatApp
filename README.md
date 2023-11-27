# overview

An interactive real-time chat desktop application built in Java with JavaFX for the user interface. This project is part of a
school Java course, showcasing the integration of socket programming for communication, JavaFX for a modern UI, and fundamental
Java concepts for the backend logic.

# features

to start the project u need to create a file called hibernate.cfg.xml in the resources folder and add the following code to it

```xml
    <?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration SYSTEM "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
         <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/chatappdatabase</property>
         <property name="hibernate.connection.username">hamza</property>
         <property name="hibernate.connection.password">hamza</property>


        <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
        <property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>
        <property name="hibernate.show_sql">true</property>
        <property name="hibernate.format_sql">true</property>
        <mapping class="com.javaserver.model.User" />
    </session-factory>
</hibernate-configuration>
```
