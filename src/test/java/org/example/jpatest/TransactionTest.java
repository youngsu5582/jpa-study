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
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.example.jpatest.util.FileScanner.clearLog;
import static org.example.jpatest.util.FileScanner.readSqlLog;


/**
 * @see <a href="https://www.baeldung.com/transaction-configuration-with-jpa-and-spring">Transactions with Spring and JPA</a>
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        assertThatThrownBy(() -> reservationService.save(new Reservation(member, "실화")))
                .isInstanceOf(RuntimeException.class);

        assertThat(reservationRepository.existsByReservationName("실화"))
                .isFalse();
    }

    @Test
    @DisplayName("트랜잭션에 noRollbackFor 에 명시하면 롤백을 하지 않는다.")
    void 명시적_트랜잭션_롤백_금지() {
        final Member member = memberRepository.save(new Member("조이썬"));
        assertThatThrownBy(() -> reservationService.saveWithDeclare(new Reservation(member, "공포")))
                .isInstanceOf(RuntimeException.class);

        assertThat(reservationRepository.existsByReservationName("공포"))
                .isTrue();
    }

    @Test
    @DisplayName("현재 트랜잭션을 받아와서 트랜잭션의 상태를 롤백으로 바꿀수 있다.")
    void 프로그래밍적_트랜잭션_롤백_금지() {
        final Member member = memberRepository.save(new Member("조이썬"));
        assertThatThrownBy(() -> reservationService.saveWithProgrammatic(new Reservation(member, "유머")))
                .isInstanceOf(RuntimeException.class);

        assertThat(reservationRepository.existsByReservationName("유머"))
                .isTrue();
    }

    @Test
    @DisplayName("트랜잭션 readonly 플래그를 설정하면, flush 를 발생하지 않는다.")
    void some(){
        clearLog();
        memberRepository.saveWithTransactionalReadOnly(new Member("조이썬"));
        final String log = readSqlLog();
        assertThat(log).doesNotContain("insert");
    }
    @Test
    @DisplayName("트랜잭션 readonly 플래그를 설정해도, flush 를 발생하면 insert 문이 발생한다.")
    // 즉, readonly 플래그 중 WRITE 가 발생해도 예외를 발생하지 않는다. - 단순 힌트
    void some1(){
        memberRepository.saveWithTransactionalReadOnlyAndFlush(new Member("조이썬"));
        final String log = readSqlLog();
        assertThat(log).contains("insert");
    }
    @Test
    @DisplayName("")
    void some2(){
        memberRepository.findAllDefault();
        final String log = readSqlLog();

    }
}
