package com.example.smartorder.item.domain;

import com.example.smartorder.entity.AuditingEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ingredients")
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer stock;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemIngredient> itemIngredients;

    public boolean hasStock(int cnt) {
        return this.getStock() >= cnt;
    }

    public void deductStock(int cnt) {
        this.setStock(this.getStock() - cnt);
    }
}
