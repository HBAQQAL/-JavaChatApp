package com.javaserver.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static volatile SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactoryInstance() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
                    } catch (Exception e) {
                        System.out.println();
                        e.printStackTrace();
                    }
                }
            }
        }
        return sessionFactory;
    }

    public static synchronized void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
