package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    // 멤버 저장
    // 영속성 컨텍스트에 멤버를 저장하고 트랜잭션이 커밋되는 순간에 db에 저장된다.
    public void save(Member member) {
        em.persist(member);
    }

    // 단일 멤버 조회
    // Collection을 사용하지 않으면 메소드 타입은 Member로 선언한다
    // em.find(return type, primary key);
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // 전체 멤버 조회
    // findAll은 매개변수가 필요없다.
    public List<Member> findAll() { // 전체 조회는 jpql을 사용할 것
        return em.createQuery("select m from Member m", Member.class) // jpql, 반환 타입
                .getResultList();
    }

    // 이름으로 조회하는 법
    // 매개변수를 선언해줌과 동시에 setParameter 메소드가 추가되고 jpql 쿼리가 변경된다.
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

//    private EntityManager em;
//
//    public void save(Member member){
//        em.persist(member);
//    }
//
//    public Member findOne(Long id){
//        return em.find(Member.class, id);
//    }
//
//    public List<Member> findAll(){
//        return em.createQuery("select m from Member m", Member.class)
//                .getResultList();
//    }
//
//    public List<Member> findName(String name){
//        return em.createQuery("select m from Member m where m.name = :name", Member.class)
//                .setParameter("name", name)
//                .getResultList();
//    }
}
