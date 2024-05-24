package org.example.jpatest.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LazyReservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private String reservationName;

    public LazyReservation(final Member member, final String reservationName) {
        this.member = member;
        this.reservationName = reservationName;
    }
}
