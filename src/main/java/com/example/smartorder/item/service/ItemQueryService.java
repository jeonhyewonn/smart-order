package com.example.smartorder.item.service;

import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemQueryService {
    private final ItemRepository itemRepository;

    public Page<Item> getAllItems(Pageable pageable) {
        return this.itemRepository.findAll(pageable);
    }
}
