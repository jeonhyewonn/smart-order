package com.example.smartorder.item.repository;

import com.example.smartorder.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
    @Query("SELECT i FROM Item i WHERE i.isDeleted = false")
    public List<Item> findAll();

    @Query(
            "SELECT distinct i " +
            "FROM Item i " +
            "LEFT JOIN FETCH i.itemIngredients map " +
            "JOIN FETCH map.ingredient igd " +
            "WHERE i.isDeleted = false AND i.id IN :ids " +
            "AND map.isDeleted = false AND igd.isDeleted = false"
    )
    public List<Item> findByIds(@Param("ids") List<Long> ids);
}
