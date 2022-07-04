package com.example.smartorder.item.repository;

import com.example.smartorder.item.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ItemCustomRepository {
    public Page<Item> findAll(Pageable pageable);
}
