package com.example.smartorder.item.controller;

import com.example.smartorder.item.controller.response.ItemResponse;
import com.example.smartorder.item.controller.response.PageResponse;
import com.example.smartorder.item.controller.response.Response;
import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.service.ItemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RequestMapping("/items")
@RestController
public class ItemController {
    private final ItemQueryService itemQueryService;

    @GetMapping
    public PageResponse getAllItems(Pageable pageable) {
        Page<Item> items = this.itemQueryService.getAllItems(pageable);

        List<Response> results = items.stream()
                .map(ItemResponse::new)
                .collect(toList());

        return new PageResponse(results, items.getTotalPages());
    }
}
