package com.hello.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        JpaMain jpaMain = new JpaMain();

        jpaMain.createQuery(em);

        emf.close();
    }

    public void createMember(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Member member = new Member();
            member.setName("HelloB");
            member.setId(1L);

            em.persist(member);

            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }
    }

    public void findMember(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Member member = em.find(Member.class, 1L);
            System.out.println("findMember = " + member.getId());
            System.out.println("findMember = " + member.getName());
            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }
    }

    public void updateMember(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Member member = em.find(Member.class, 1L);
            member.setName("reNameA");

            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }
    }

    public void removeMember(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Member member = em.find(Member.class, 1L);

            em.remove(member);

            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }
    }

    public void createQuery(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            List<Member> members = em.createQuery("select m from Member as m where m.name like 'Hello%'", Member.class)
//                    .setFirstResult(5) // 페이징 활용
//                    .setMaxResults(10)
                    .getResultList();

            for (Member member : members) {
                System.out.println(member.getName());
            }
            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }

    }
}
