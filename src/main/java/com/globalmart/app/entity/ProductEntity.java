package com.globalmart.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        name = "products",
        indexes = {
                @Index(name = "idx_price", columnList = "price"),
                @Index(name = "idx_currency", columnList = "currency")
        })
@NoArgsConstructor
public class ProductEntity extends AbstractEntity {

    @Column(name = "name")
    @Size(message = "invalid length for name", min = 2, max = 255)
    private String name;

    @Column(name = "product_code", unique = true, nullable = false)
    @Size(message = "invalid length for productCode", min = 2, max = 255)
    private String productCode;

    @Column(name = "description")
    @Size(message = "invalid length for description", min = 2, max = 255)
    private String description;

    @Column(name = "price")
    @Positive(message = "invalid value for price")
    private BigDecimal price;

    @Column(name = "currency")
    @Size(message = "invalid length for currency", min = 3, max = 3)
    private String currency;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity seller;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private Set<OrderEntity> orders;

}
