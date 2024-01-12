package com.javaserver.model;

// import org.hibernate.Session;
// import org.hibernate.SessionFactory;
// import org.hibernate.Transaction;
// import org.hibernate.query.Query;

// import com.javaserver.utils.HibernateUtil;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", username=" + username
                + ", dateofbirth=" + dateofbirth + ", password=" + password + "]";
    }

    // public String createUser() {
    // try {
    // SessionFactory sessionFactory = HibernateUtil.getSessionFactoryInstance();
    // try (Session session = sessionFactory.openSession()) {
    // session.beginTransaction();
    // session.persist(this);
    // session.getTransaction().commit();
    // }

    // } catch (ConstraintViolationException e) {
    // return "Username already exists";
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return "User created";
    // }

    // public String createUser() {
    // try {
    // if (isUsernameExists(this.getUsername())) {
    // return "Username already exists";
    // }

    // saveUser(this);
    // return "User created";
    // } catch (Exception e) {
    // // Log the exception for debugging purposes
    // e.printStackTrace();
    // return "Failed to create user";
    // }
    // }

    // private boolean isUsernameExists(String username) {
    // try (Session session =
    // HibernateUtil.getSessionFactoryInstance().openSession()) {
    // return (Long) session.createQuery("SELECT COUNT(u) FROM User u WHERE
    // u.username = :username")
    // .setParameter("username", username)
    // .uniqueResult() > 0;
    // }
    // }

    // private void saveUser(User user) {
    // try (Session session =
    // HibernateUtil.getSessionFactoryInstance().openSession()) {
    // Transaction transaction = session.beginTransaction();
    // session.persist(user);
    // transaction.commit();
    // }
    // }

    // public static boolean doesUserExist(String username) {
    // try {
    // SessionFactory sessionFactory = HibernateUtil.getSessionFactoryInstance();
    // try (Session session = sessionFactory.openSession()) {
    // // Create a query to check if the user exists
    // Query<Long> query = session.createQuery(
    // "SELECT COUNT(*) FROM User WHERE username = :username", Long.class);
    // query.setParameter("username", username);

    // // Execute the query and get the result
    // Long count = query.uniqueResult();

    // // If count > 0, the user exists; otherwise, they don't
    // return count > 0;
    // }

    // } catch (Exception e) {
    // // Handle exceptions appropriately (e.g., log or throw)
    // e.printStackTrace();
    // return false;
    // }
    // }

    // public static boolean isPasswordCorrect(String username, String password) {
    // try {
    // SessionFactory sessionFactory = HibernateUtil.getSessionFactoryInstance();
    // try (Session session = sessionFactory.openSession()) {
    // // Retrieve the user based on the username
    // Query<User> query = session.createQuery(
    // "FROM User WHERE username = :username", User.class);
    // query.setParameter("username", username);

    // // Get the user entity
    // User user = query.uniqueResult();

    // // Check if the user exists and the passwords match
    // return user != null && user.getPassword().equals(password);
    // }

    // } catch (Exception e) {
    // // Handle exceptions appropriately (e.g., log or throw)
    // e.printStackTrace();
    // return false;
    // }
    // }

    // public String getUser() {
    // System.out.println("Getting user from the database");
    // return "GET /user";
    // }

    // public String getUserById() {
    // System.out.println("Getting user by id from the database");
    // return "GET /user/:id";
    // }

    // public String getAllUsers() {
    // System.out.println("Getting all users from the database");
    // return "GET /allusers";
    // }
}
