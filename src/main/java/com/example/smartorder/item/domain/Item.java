package com.example.smartorder.item.domain;

import com.example.smartorder.order.domain.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "items")
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id
    @GeneratedValue
    private String id;
    private String name;
    private Double price;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "item")
    private List<ItemIngredient> itemIngredients;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems;
}
