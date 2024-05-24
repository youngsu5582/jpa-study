package org.example.jpatest.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    @Transient
    private boolean isRemoved=false;

    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;
    private String reservationName;

    public Reservation(final Member member, final String reservationName) {
        this.member = member;
        this.reservationName = reservationName;
    }
    @PreRemove
    private void remove() {
        this.isRemoved = true;
    }
    public boolean getIsRemoved() {
        return this.isRemoved;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", isRemoved=" + isRemoved +
                ", member=" + member +
                ", reservationName='" + reservationName + '\'' +
                '}';
    }
}
