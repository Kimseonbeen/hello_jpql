package org.example;

import org.example.jpql.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("userA");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("userB");
            member2.setTeam(team);
            member2.setType(MemberType.ADMIN);
            em.persist(member2);




            /***
             *              // 타입 정보가 명확할때
             *             TypedQuery<String> query1 = em.createQuery("select m.username from Member m", String.class);
             *             TypedQuery<Member> query2 = em.createQuery("select m from Member m", Member.class);
             *
             *             // String: username, int: age 타입 정보가 명확하지 않음
             *             Query query3 = em.createQuery("select m.username, m.age from Member m");
             */

            em.flush();
            em.clear();

            /***
             * 프로젝션
             * List<MemberDTO> result = em.createQuery("select new org.example.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
             *                 .getResultList();
             *
             * MemberDTO memberDTO = result.get(0);
             * System.out.println("memberDTO.getUseranme() = " + memberDTO.getUseranme());
             * System.out.println("memberDTO.getAge() = " + memberDTO.getAge());
             */

            /***
             * 페이징 처리
             * List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
             *                     .setFirstResult(10)      // 시작
             *                     .setMaxResults(20)       // 결과 개수
             *                     .getResultList();        //끝.
             *
             *             System.out.println("result.size =" + result.size());
             *             for (Member member1 : result) {
             *                 System.out.println("member1 = " + member1);
             *             }
             *
             *             String query = "select m.username, 'HELLO', TRUE FROM Member m " +
             *                             "where m.type = :userType";
             *             List<Object[]> result = em.createQuery(query)
             *                     .setParameter("userType", MemberType.ADMIN)
             *                     .getResultList();
             *
             *             for (Object[] objects : result) {
             *                 System.out.println("objects[0] = " + objects[0]);
             *                 System.out.println("objects[1] = " + objects[1]);
             *                 System.out.println("objects[2] = " + objects[2]);
             *             }
             *
             */

            /**
             * 조건식 - CASE 식
             *
             *             String query = "select " +
             *                             "case when m.age <= 10 then '학생요금' " +
             *                             "     when m.age >= 60 then '경로요금' " +
             *                             "     else '일반요금' END " +
             *                             "from Member m";
             *             List<String> resultList = em.createQuery(query, String.class)
             *                     .getResultList();
             *
             *             for (String s : resultList) {
             *                 System.out.println("s = " + s);
             *             }
             *
             * coalesce null 치환 반환
             *
             *             String query = "select coalesce(m.username, '이름없는 회원') from Member m";
             *             List<String> resultList = em.createQuery(query, String.class)
             *                     .getResultList();
             *
             *             for (String s : resultList) {
             *                 System.out.println("s = " + s);
             *             }
             *
             *
             * */

            /** 
             * Member m에서 select m.team을 하게되면 묵시적 내부 조인이 일어남 
             * 묵시적 조인은 기피해야한다. 유지보수 시 어려움
             * */
            String query = "select m.username From Team t join t.members m";
            String resultList = em.createQuery(query, String.class)
                    .getResultList().toString();

            System.out.println("resultList = " + resultList);



            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}