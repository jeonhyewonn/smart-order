package com.example.smartorder.item.controller;

import com.example.smartorder.item.controller.response.ItemResponse;
import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.service.ItemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RequestMapping("/items")
@RestController
public class ItemController {
    private final ItemQueryService itemQueryService;

    @GetMapping
    public Stream<ItemResponse> getAllItems() {
        List<Item> items = this.itemQueryService.getAllItems();

        return items.stream().map(ItemResponse::new);
    }
}
