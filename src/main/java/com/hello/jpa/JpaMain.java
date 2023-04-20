package com.hello.jpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        JpaMain jpaMain = new JpaMain();

        jpaMain.detachEntityManager(em);

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

    public void flushEntityManager(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Member member1 = new Member(152L, "152L");
            Member member2 = new Member(162L, "162L");

            // 플러쉬 모드를 세팅할 수 있음.
//            em.setFlushMode(FlushModeType.AUTO); // 트랜잭션 커밋이나, 쿼리를 실행할때 flush (기본세팅값)
//            em.setFlushMode(FlushModeType.COMMIT); // 트랜잭션 커밋을 하는 경우에만 flush

            em.persist(member1);
            em.persist(member2);

            em.flush(); // 기본적으로는 트랜잭션 commit시 flush가 자동으로 호출됨.
                        // 만약 commit전에 미리 쿼리나 데이터를 확인하고 싶다면 flush를 이용하면 commit 이전에 데이터베이스에 변경내용을 동기화시킴.
                        // 단, 실제로 변경된 데이터가 반영되는것은 commit 이후에 반영됨.
            System.out.println("==============================");

            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }
    }

    public void detachEntityManager(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Member member = em.find(Member.class, 151L);
            member.setName("151L_New");

            em.detach(member); // detach를 호출하면 영속성 컨텍스트에서 해당 객체를 제거한다. 따라서 객체의 정보가 변경되어도 데이터베이스에 반영되지 않는다.
                               // 영속 컨텍스트에서 제거된 상태를 '준영속 상태' 라고 한다.
            em.clear(); // 영속성 컨텍스트를 완전히 초기화 한다.

            em.close(); // 영속성 컨텍스트를 종료한다. -> 이때는 영속성 컨텍스트가 아예 종료된 것 이기때문에 jpa에서 객체를 관리하지 않는다. 따라서 객체를 변경하거나 해도 데이터베이스에 반영되지 않는다.

            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }
    }
}
