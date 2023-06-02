package com.hello.jpa;

import com.hello.jpa.cascade.Child;
import com.hello.jpa.cascade.Parent;
import com.hello.jpa.extendsMapping.Movie;
import com.hello.jpa.jpashop.domain.Address;
import com.hello.jpa.jpashop.domain.Member;
import com.hello.jpa.jpashop.domain.Period;
import com.hello.jpa.team.Team;
import com.hello.jpa.team.TeamMember;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();
        EntityManager em = emf.createEntityManager();

        JpaMain jpaMain = new JpaMain();
        jpaMain.valueType(em);

        emf.close();
    }

    public void valueType(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            Address address = new Address("seoul", "gangnam", "235236");
            Period period = new Period(LocalDateTime.now(), LocalDateTime.now());

            Member member = new Member();
            member.setName("member1");
            member.setHome_address(address);
            member.setPeriod(period);
            em.persist(member);

            Address address1 = new Address("busan", address.getStreet(), address.getZipcode());

            Member member1 = new Member();
            member1.setName("member2");
            member1.setHome_address(address1);
            member1.setPeriod(period);
            em.persist(member1);

//            member.getHome_address().setCity("busan") // 이렇게 하면 member1, member2 모두 업데이트 되버림;

            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }

    }


    public void cascadeTest(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
//            findParent.getChildList().remove(0);

            em.remove(findParent);

            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }
    }
    public void extendsMapping(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            Movie movie = new Movie();
            movie.setName("어벤저스");
            movie.setActor("aaa");
            movie.setDirector("bbbb");

            em.persist(movie);

            em.flush();
            em.clear();

            Movie movie1 = em.find(Movie.class, movie.getId());
            System.out.println("findMovie = " + movie1.getName());
            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }
    }

    public void TeamMember(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);


            TeamMember member = new TeamMember();
            member.setName("member1");
            member.setTeam(team);
            member.setCreatedBy("lee");
            member.setCreatedDate(LocalDateTime.now());
            em.persist(member);

            em.flush();
            em.clear();


            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }
    }

    public void proxy(EntityManager em, PersistenceUnitUtil persistenceUnitUtil) {

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            TeamMember member = new TeamMember();
            member.setName("member1");
            member.setTeam(team);
            member.setCreatedBy("lee");
            member.setCreatedDate(LocalDateTime.now());
            em.persist(member);

            em.flush();
            em.clear();

            TeamMember refMember = em.getReference(TeamMember.class, member.getId());
            // getReference -> 실제 엔티티가 아닌 프록시 객체를 전달해줌.
            // 프록시 객체로 일단 준 후, 실제로 어떤값을 사용하거나 하면 쿼리를 날려 DB를 조회하고 엔티티와 프록시 객체를 연결시킴

            // 프록시 객체가 실제로 로딩이 되었는지 확인하는 메소드
            System.out.println(persistenceUnitUtil.isLoaded(refMember));

            TeamMember findMember = em.find(TeamMember.class, member.getId());
            System.out.println("##########################################");
            System.out.println("findMember = " + findMember.getClass());
            System.out.println("refMember = " + refMember.getClass());
            System.out.println("refMember = " + refMember.getName());
            System.out.println("##########################################");
            System.out.println(persistenceUnitUtil.isLoaded(refMember));
            // 이렇게 하면 실제 find를 했지만 findMember도 프록시 객체가 전달되어 있음.
            // 이는 findMember 와 refMember의 동등 비교를 보장하기 위해서 그런 것.


            em.flush();
            em.clear();

            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
    }

    public void lazyLoading(EntityManager em) {

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Team team1 = new Team();
            team1.setName("TeamB");
            em.persist(team1);

            TeamMember member = new TeamMember();
            member.setName("member1");
            member.setTeam(team);
            member.setCreatedBy("lee");
            member.setCreatedDate(LocalDateTime.now());
            em.persist(member);

            TeamMember member1 = new TeamMember();
            member1.setName("member2");
            member1.setTeam(team1);
            em.persist(member1);

            em.flush();
            em.clear();

            // 즉시로딩인 상태에서, JPQL을 이렇게 작성하면, TeamMember 가져오고 + 각 TeamMember Team을 조회하는 쿼리가 추가적으로 더 나가게됨 ==> N+1
//            List<TeamMember> members = em.createQuery("select m from TeamMember m", TeamMember.class).getResultList();

            // fetch join을 사용하여 미리 가져올 값을 선택해주면, 미리 조인해서 가져오기 때문에 N+1을 막을 수 있다.
            List<TeamMember> members = em.createQuery("select m from TeamMember m join fetch m.team", TeamMember.class).getResultList();

            for (TeamMember teamMember : members) {
                System.out.println("############# " + teamMember.getTeam().getName());
            }

            transaction.commit();
        }catch (Exception e) {
            transaction.rollback();
        }finally {
            em.close();
        }
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
            List<TeamMember> members = em.createQuery("select m from TeamMember as m where m.name like 'Hello%'", TeamMember.class)
//                    .setFirstResult(5) // 페이징 활용
//                    .setMaxResults(10)
                    .getResultList();

            for (TeamMember member : members) {
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
            Member member1 = new Member();
            Member member2 = new Member();

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
