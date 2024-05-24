package org.example.jpatest.repository;

import org.example.jpatest.domain.LazyReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LazyReservationRepository  extends JpaRepository<LazyReservation, Long> {

}
