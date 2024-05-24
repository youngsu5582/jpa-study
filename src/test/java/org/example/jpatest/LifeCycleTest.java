package org.example.jpatest;

import org.example.jpatest.config.ClearFile;
import org.example.jpatest.domain.Member;
import org.example.jpatest.domain.Reservation;
import org.example.jpatest.repository.MemberRepository;
import org.example.jpatest.repository.ReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ClearFile
@SpringBootTest
class LifeCycleTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Test
    @Transactional
    @DisplayName("JPA 가 제공해주는 로직은 엔티티 생명주기에서 작동한다.")
    void JPA_제공_메소드_생명주기() {
        final Member member = new Member("조이썬");
        memberRepository.save(member);
        final Reservation reservation = new Reservation(member, "공포");
        reservationRepository.save(reservation);

        reservationRepository.delete(reservation);

        assertThat(reservation.getIsRemoved()).isTrue();
    }
    @Test
    @DisplayName("Spring Data JPA 메소드 파싱 쿼리 역시 엔티티 생명주기에서 작동한다.")
    @Transactional
    void SPRING_DATA_JPA_제공_메소드_생명주기(){
        final Member member = new Member("조이썬");
        memberRepository.save(member);
        final Reservation reservation = new Reservation(member, "공포");
        reservationRepository.save(reservation);

        reservationRepository.deleteByMember(member);

        assertThat(reservation.getIsRemoved()).isTrue();
    }
    @Test
    @DisplayName("JPQL 쿼리 메소드는 엔티티 생명주기를 거치지 않는다.")
    @Transactional
    void JPQL_쿼리_메소드_생명주기(){
        final Member member = new Member("조이썬");
        memberRepository.save(member);
        final Reservation reservation = new Reservation(member, "공포");
        reservationRepository.save(reservation);

        reservationRepository.deleteByMemberWithQuery(member);

        assertThat(reservation.getIsRemoved()).isFalse();
    }
}
