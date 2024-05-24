package org.example.jpatest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@Getter
public class NoIdMember {
    @Id
    private Long id;
    String name;

    public NoIdMember(final String name) {
        this.name = name;
    }

    public NoIdMember(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }
}
