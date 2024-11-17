package org.example.jpatest.repository;

import org.example.jpatest.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Transactional(readOnly = true)
    default Member saveWithTransactionalReadOnly(final Member entity) {
        final Member member = this.save(entity);
        return member;
    }

    @Transactional(readOnly = true)
    default Member saveWithTransactionalReadOnlyAndFlush(final Member entity) {
        final Member member = this.save(entity);
        this.flush();
        return member;
    }

    @Transactional(readOnly = true)
    default List<Member> findAllDefault() {
        return this.findAll();
    }
}
