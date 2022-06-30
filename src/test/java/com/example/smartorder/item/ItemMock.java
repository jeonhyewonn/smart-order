package com.example.smartorder.item;

import com.example.smartorder.item.domain.Ingredient;
import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.domain.ItemIngredient;

import java.util.List;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class ItemMock {
    private static class TestIngredient extends Ingredient {
        public TestIngredient() {
            super();
            setField(this, "id", 1L);
            setField(this, "name", "유자청");
            setField(this, "stock", 2);
            setField(this, "isDeleted", false);
        }
    }

    private static class TestItemIngredient extends ItemIngredient {
        public TestItemIngredient() {
            super();
            setField(this, "id", 1L);
            setField(this, "ingredient", new TestIngredient());
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
            setField(this, "itemIngredients", List.of(new TestItemIngredient()));
        }
    }
    public final Item item = new TestItem();
}
