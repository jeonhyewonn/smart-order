package com.example.smartorder.item.service;

import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemQueryService {
    private final ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return this.itemRepository.findAll();
    }
}
