package org.example.jpatest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.jpatest.config.ClearFile;
import org.example.jpatest.domain.Member;
import org.example.jpatest.domain.NoIdMember;
import org.example.jpatest.repository.MemberRepository;
import org.example.jpatest.repository.NoIdMemberRepository;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.jpatest.util.FileScanner.readSqlLog;

@ClearFile
@SpringBootTest
class EntityStateDetectionTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NoIdMemberRepository noIdMemberRepository;

    @Test
    @DisplayName("자동 생성 전략시, ID가 자동으로 생성되기 위해 DB에 INSERT 문을 보낸다.")
    void some() {
        final Member member = new Member("joyson");
        memberRepository.save(member);
        assertThat(member.getId()).isNotNull();
        final String sqlLog = readSqlLog();
        assertThat(sqlLog).contains("insert");
    }

    @Test
    @DisplayName("수동 생성 전략시, ID가 없으면 예외를 발생한다.")
    @Transactional
    void some1() {
        final NoIdMember member = new NoIdMember("joyson");
        assertThatThrownBy(() -> entityManager.persist(member))
                .isInstanceOf(IdentifierGenerationException.class);
        assertThatThrownBy(() -> noIdMemberRepository.save(member))
                .isInstanceOf(JpaSystemException.class);
    }


    @Test
    @DisplayName("수동 생성 전략시, ID가 있는지 먼저 확인한다.")
    void some2() {
        noIdMemberRepository.save(new NoIdMember(1L,"3"));
        final String sqlLog = readSqlLog();
        assertThat(sqlLog).contains("select")
                .contains("insert");
    }
}
