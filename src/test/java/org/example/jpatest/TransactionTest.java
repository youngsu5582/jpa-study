package org.example.jpatest;

import org.example.jpatest.domain.Member;
import org.example.jpatest.domain.Reservation;
import org.example.jpatest.repository.MemberRepository;
import org.example.jpatest.repository.ReservationRepository;
import org.example.jpatest.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * @see <a href="https://www.baeldung.com/transaction-configuration-with-jpa-and-spring">Transactions with Spring and JPA</a>
 */
@SpringBootTest
class TransactionTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;

    @Test
    @DisplayName("트랜잭션 명시후, 런타임 예외가 발생하면 롤백을 한다.")
    void 트랜잭션_롤백() {
        final Member member = memberRepository.save(new Member("조이썬"));
        assertThatThrownBy(() -> reservationService.save(new Reservation(member, "실화")));

        assertThat(reservationRepository.existsByReservationName("실화"))
                .isFalse();
    }

    @Test
    @DisplayName("트랜잭션에 noRollbackFor 에 명시하면 롤백을 하지 않는다.")
    void 명시적_트랜잭션_롤백_금지() {
        final Member member = memberRepository.save(new Member("조이썬"));
        assertThatThrownBy(() -> reservationService.saveWithDeclare(new Reservation(member, "공포")));

        assertThat(reservationRepository.existsByReservationName("공포"))
                .isTrue();
    }

    @Test
    @DisplayName("현재 트랜잭션을 받아와서 트랜잭션의 상태를 롤백으로 바꿀수 있다.")
    void 프로그래밍적_트랜잭션_롤백_금지() {
        final Member member = memberRepository.save(new Member("조이썬"));
        assertThatThrownBy(() -> reservationService.saveWithProgrammatic(new Reservation(member, "유머")));

        assertThat(reservationRepository.existsByReservationName("유머"))
                .isTrue();
    }

}
