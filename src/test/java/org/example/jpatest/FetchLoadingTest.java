package org.example.jpatest;

import static org.example.jpatest.util.FileScanner.readSqlLog;

import org.example.jpatest.config.ClearFile;
import org.example.jpatest.domain.LazyReservation;
import org.example.jpatest.domain.Member;
import org.example.jpatest.domain.Reservation;
import org.example.jpatest.repository.LazyReservationRepository;
import org.example.jpatest.repository.MemberRepository;
import org.example.jpatest.repository.ReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@ClearFile
@SpringBootTest
class FetchLoadingTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LazyReservationRepository lazyReservationRepository;

    @Test
    @DisplayName("JPA 는 EAGER LOADING 으로 지정한 값을 JOIN 문으로 한번에 받아온다.")
    void JPA_EAGER_LOADING() {
        final Member member = new Member("조이썬");
        memberRepository.save(member);
        final Reservation reservation = new Reservation(member, "공포");
        reservationRepository.save(reservation);

        reservationRepository.findById(member.getId());

        final String sqlLog = readSqlLog();
        assertThat(sqlLog).contains("left join")
                .contains("m1_0.id=r1_0.member_id");
    }

    @Test
    @DisplayName("JPA 는 LAZY LOADING 으로 지정한 값을 필요할때 받아온다. ")
    void JPA_LAZY_LOADING() {
        final Member member = new Member("조이썬");
        memberRepository.save(member);
        final LazyReservation reservation = new LazyReservation(member, "공포");
        lazyReservationRepository.save(reservation);

        lazyReservationRepository.findById(member.getId());

        final String sqlLog = readSqlLog();
        assertThat(sqlLog).contains("""
                        select
                                r1_0.id,
                                r1_0.member_id,
                                r1_0.reservation_name\s
                            from
                                reservation r1_0\s
                            where
                                r1_0.member_id=?
                        """)
                .doesNotContain("left join");
    }

    @Test
    @DisplayName("Spring Data JPA 는 EAGER LOADING 이여도 메소드 파싱 쿼리일 시 조인문을 사용하지 않고, 값을 받아온다.")
    void SPRING_DATA_EAGER_LOADING_NOT_USE_JOIN() {

        final Member member = new Member("조이썬");
        memberRepository.save(member);
        final Reservation reservation = new Reservation(member, "공포");
        reservationRepository.save(reservation);

        reservationRepository.findByMember(member);

        final String sqlLog = readSqlLog();
        assertThat(sqlLog).contains("""
                        select
                                r1_0.id,
                                r1_0.member_id,
                                r1_0.reservation_name\s
                            from
                                reservation r1_0\s
                            where
                                r1_0.member_id=?
                        """)
                .doesNotContain("left join");
    }

    @Test
    @DisplayName("Spring Data JPA 는 메소드 파싱 쿼일 시 EntityGraph 를 명시하면 조인문을 사용해 값을 받아온다.")
    void SPRING_DATA_EAGER_LOADING_WITH_ENTITY_GRAPH_USE_JOIN() {

        final Member member = new Member("조이썬");
        memberRepository.save(member);
        final Reservation reservation = new Reservation(member, "공포");
        reservationRepository.save(reservation);

        reservationRepository.findTop3ByMember(member);

        final String sqlLog = readSqlLog();
        assertThat(sqlLog).contains("left join");
    }


}
