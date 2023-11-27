package com.javaserver.model;

import com.javaserver.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "username")
    private String username;

    @Column(name = "dateofbirth")
    private String dateofbirth;

    @Column(name = "password")
    private String password;

    public User() {
    }

    public User(String firstname, String lastname, String username, String dateofbirth, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.dateofbirth = dateofbirth;
        this.password = password;
    }

    public String createUser() {
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactoryInstance();
            org.hibernate.Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(this);
            session.getTransaction().commit();
            session.close();

        } catch (ConstraintViolationException e) {
            return "username already exists";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user created";
    }

    public String getUser() {
        System.out.println("getting user fromt the database");
        return "GET /user";
    }

    public String getUserById() {
        System.out.println("getting user by id from the database");
        return "GET /user/:id";
    }

    public String getAllUsers() {
        System.out.println("getting all users from the database");
        return "GET /allusers";
    }

}
