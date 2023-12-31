package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {
    private final InitMemberService initMemberService;

    // 스프링 라이프사이클의 문제로 @PostConstruct와 @Transactional 을 동시에 사용할 수 없다.
    // 그래서 InitMemberService 를 생성해서 분리시킴
    @PostConstruct
    public void init() {
        initMemberService.init();

    }
    @Component
    static class InitMemberService {
        @PersistenceContext private EntityManager em;

        @Transactional
        public void init() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);

            for (int i = 0; i < 100; i++) {
                Team selectedTam = i % 2 == 0 ? teamA : teamB;
                em.persist(new Member("member"+i, i, selectedTam));
            }
        }
    }
}
