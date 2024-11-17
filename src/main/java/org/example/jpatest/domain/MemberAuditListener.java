package org.example.jpatest.domain;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class MemberAuditListener {
    private static final AtomicInteger updateCount = new AtomicInteger(0);

    @PreUpdate
    private void beforeAnyUpdate(final Member member) {
        updateCount.incrementAndGet();
        if (member.getId() == 0) {
            log.info("[USER AUDIT] About to add a user");
        } else {
            log.info("[USER AUDIT] About to update/delete user: " + member.getId());
        }
    }

    @PostUpdate
    private void afterAnyUpdate(final Member member) {
        updateCount.incrementAndGet();
        log.info("[USER AUDIT] add/update/delete complete for user: " + member.getId());
    }

    @PostLoad
    private void afterLoad(final Member member) {
        log.info("[USER AUDIT] user loaded from database: " + member.getId());
    }

    public int getUpdateCount() {
        return updateCount.get();
    }
}
