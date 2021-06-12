package com.sample.common.domains;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "items")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item extends Auditable {

    @Id
    @GeneratedValue(generator = "item_generator")
    @SequenceGenerator(name = "item_generator", sequenceName = "item_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Partner seller;

    @Column(name = "available")
    private Boolean available;

    protected Item() {
    }

    public Item(Long id, String name, BigDecimal price, Partner seller, Boolean available) {
        this.id = id;
        this.price = price;
        this.seller = seller;
        this.available = available;
    }

    public Item(Long id) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Partner getSeller() {
        return seller;
    }

    public void setSeller(Partner seller) {
        this.seller = seller;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return new EqualsBuilder()
                .append(id, item.id)
                .append(name, item.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", available=" + available +
                '}';
    }
}
