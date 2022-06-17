package com.example.smartorder.item;

import com.example.smartorder.item.domain.Item;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class ItemMock {
    private static class TestItem extends Item {
        public TestItem(String id, String name, Double price) {
            super();
            this.setId(id);
            this.setName(name);
            this.setPrice(price);
            this.setIsDeleted(false);
            this.setCreatedAt(LocalDateTime.now());
        }
    }
    public static final Item item = new TestItem("itemId", "유자차", 5000.0);
}
