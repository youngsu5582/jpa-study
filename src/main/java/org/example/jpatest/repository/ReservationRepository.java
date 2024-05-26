package org.example.jpatest.repository;

import org.example.jpatest.domain.Member;
import org.example.jpatest.domain.Reservation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByMember(Member member);

    @EntityGraph(attributePaths = "member")
    List<Reservation> findTop3ByMember(Member member);
    void deleteByMember(Member member);
    @Modifying
    @Query("delete from Reservation where member=:member")
    void deleteByMemberWithQuery(Member member);

    boolean existsByReservationName(String reservationName);
}

