package com.sample.common.domains;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(name = "partners")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Partner extends Auditable {

    @Id
    @GeneratedValue(generator = "partner_generator")
    @SequenceGenerator(name = "partner_generator", sequenceName = "partner_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    public Partner() {
    }

    public Partner(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Partner partner = (Partner) o;

        return new EqualsBuilder()
                .append(id, partner.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Partner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
