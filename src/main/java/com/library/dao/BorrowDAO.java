package com.library.dao;

import com.library.entity.Borrow;
import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class BorrowDAO {
    
    public void save(Borrow borrow) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(borrow);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    public void update(Borrow borrow) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(borrow);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    public Borrow findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Borrow.class, id);
        }
    }
    
    public List<Borrow> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Borrow", Borrow.class).list();
        }
    }
    
    public List<Borrow> findActiveByMemberId(Long memberId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Borrow where member.id = :memberId and isReturned = false";
            Query<Borrow> query = session.createQuery(hql, Borrow.class);
            query.setParameter("memberId", memberId);
            return query.list();
        }
    }
    
    public List<Borrow> findOverdueBorrows() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Borrow where isReturned = false and dueDate < current_date()";
            return session.createQuery(hql, Borrow.class).list();
        }
    }
}
