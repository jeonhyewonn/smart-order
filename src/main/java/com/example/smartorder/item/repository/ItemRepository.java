package com.example.smartorder.item.repository;

import com.example.smartorder.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Override
    @Query("SELECT i FROM Item i WHERE i.isDeleted = false")
    public List<Item> findAll();

    @Query("SELECT i FROM Item i WHERE i.isDeleted = false AND i.id IN :ids")
    public List<Item> findByIds(@Param("ids") List<Long> ids);
}
