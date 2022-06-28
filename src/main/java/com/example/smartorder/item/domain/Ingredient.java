package com.example.smartorder.item.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ingredients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer stock;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private List<ItemIngredient> itemIngredients;
}
