package com.globalmart.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_currency", columnList = "currency"),
                @Index(name = "idx_total_amount", columnList = "total_amount"),
        })
@NoArgsConstructor
public class OrderEntity extends AbstractEntity {

    @Column(name = "status")
    @Size(message = "invalid length for status", min = 2, max = 255)
    private String status;

    @Column(name = "total_amount")
    @Positive(message = "invalid value for total_amount")
    private BigDecimal totalAmount;

    @Column(name = "currency")
    @Size(message = "invalid length for currency", min = 3, max = 3)
    private String currency;

    @Column(name = "order_reference", unique = true)
    @Size(message = "invalid length for orderReference", min = 2, max = 255)
    private String orderReference;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<ProductEntity> products;

}
