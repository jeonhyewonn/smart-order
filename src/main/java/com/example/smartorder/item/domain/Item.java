package com.example.smartorder.item.domain;

import com.example.smartorder.entity.AuditingEntity;
import com.example.smartorder.order.domain.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "item")
    private List<ItemIngredient> itemIngredients;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems;

    public Boolean hasSufficientIngredients(int quantity) {
        return this.getItemIngredients().stream()
                .allMatch(map -> map.getIngredient().hasStock(quantity));
    }

    public void deductIngredients(int quantity) {
        for (ItemIngredient itemIngredient : this.itemIngredients) {
            itemIngredient.getIngredient().deductStock(quantity);
        }
    }
}
