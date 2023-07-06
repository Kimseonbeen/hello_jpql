package org.example;

import org.example.jpql.*;

import javax.persistence.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);


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
             */

            String query = "select m.username, 'HELLO', TRUE FROM Member m " +
                            "where m.type = :userType";
            List<Object[]> result = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : result) {
                System.out.println("objects[0] = " + objects[0]);
                System.out.println("objects[1] = " + objects[1]);
                System.out.println("objects[2] = " + objects[2]);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}