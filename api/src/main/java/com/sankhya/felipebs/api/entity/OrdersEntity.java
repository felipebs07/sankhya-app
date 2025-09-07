package com.sankhya.felipebs.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrdersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public OrdersEntity(BigDecimal total, LocalDateTime createdAt) {
        this.total = total;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrdersEntity ordersEntity = (OrdersEntity) o;
        return Objects.equals(id, ordersEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

