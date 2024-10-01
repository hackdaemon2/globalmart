package com.globalmart.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventories")
@NoArgsConstructor
public class InventoryEntity extends AbstractEntity {

    @Column(name = "stock_level")
    @Positive(message = "invalid value for stock_level")
    private int stockLevel;

    @Column(name = "reorder_threshold")
    @Positive(message = "invalid value for reorder_threshold")
    private int reorderThreshold;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

}
