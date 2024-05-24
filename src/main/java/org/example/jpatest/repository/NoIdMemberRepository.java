package org.example.jpatest.repository;

import org.example.jpatest.domain.NoIdMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoIdMemberRepository extends JpaRepository<NoIdMember, Long> {
}
