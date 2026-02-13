package com.library.util;

import com.library.entity.Book;
import com.library.entity.Member;
import com.library.entity.Borrow;
import com.library.entity.Purchase;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                
                // Hibernate settings
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.h2.Driver");
                settings.put(Environment.URL, "jdbc:h2:./librarydb");
                settings.put(Environment.USER, "sa");
                settings.put(Environment.PASS, "");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");
                
                configuration.setProperties(settings);
                
                // Add annotated entity classes
                configuration.addAnnotatedClass(Book.class);
                configuration.addAnnotatedClass(Member.class);
                configuration.addAnnotatedClass(Borrow.class);
                configuration.addAnnotatedClass(Purchase.class);
                
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
