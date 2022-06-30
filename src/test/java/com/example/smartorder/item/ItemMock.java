package com.example.smartorder.item;

import com.example.smartorder.item.domain.Ingredient;
import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.domain.ItemIngredient;

import java.util.List;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class ItemMock {
    private static class TestIngredient extends Ingredient {
        public TestIngredient(Long id, String name, int stock) {
            super();
            setField(this, "id", id);
            setField(this, "name", name);
            setField(this, "stock", stock);
            setField(this, "isDeleted", false);
        }
    }

    private static class TestItemIngredient extends ItemIngredient {
        public TestItemIngredient(Long id, Ingredient ingredient) {
            super();
            setField(this, "id", id);
            setField(this, "ingredient", ingredient);
            setField(this, "isDeleted", false);
        }
    }

    private static class TestItem extends Item {
        public TestItem() {
            super();
            setField(this, "id", 1L);
            setField(this, "name", "유자차");
            setField(this, "price", 5000.0);
            setField(this, "isDeleted", false);
            setField(this, "itemIngredients", List.of(
                    new TestItemIngredient(1L, new TestIngredient(1L, "유자청", 2)),
                    new TestItemIngredient(2L, new TestIngredient(2L, "설탕", 2))
            ));
        }
    }
    public final Item item = new TestItem();
}
