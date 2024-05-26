package org.example.jpatest.service;

import org.example.jpatest.domain.Reservation;
import org.example.jpatest.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    @Transactional
    public void save(final Reservation reservation) {
        reservationRepository.save(reservation);
        throw new RuntimeException("ss");
    }

    @Transactional(noRollbackFor = {RuntimeException.class})
    public void saveWithDeclare(final Reservation reservation) {
        reservationRepository.save(reservation);
        throw new RuntimeException("ss");
    }

    public void saveWithProgrammatic(final Reservation reservation) {
        try {
            reservationRepository.save(reservation);
            throw new RuntimeException("ss");

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus()
                    .setRollbackOnly();
        }
    }
}
