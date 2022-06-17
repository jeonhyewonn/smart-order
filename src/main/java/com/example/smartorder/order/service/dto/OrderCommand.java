package com.example.smartorder.order.service.dto;

import com.example.smartorder.item.domain.Item;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
@Builder
public class OrderCommand {
    private List<OrderItemCommand> orderItemCommands;
    private Double totalAmount;

    public boolean isTotalAmountCorrect(List<Item> items) {
        HashMap<String, Item> itemMap = new HashMap<>();
        for(Item item : items) {
            itemMap.put(item.getId(), item);
        }

        Double payAmountSum = this.getOrderItemCommands().stream()
                .reduce(0.0, (subTotal, orderItem)
                        -> subTotal + orderItem.getPayAmount(itemMap.get(orderItem.getItemId())),
                        Double::sum
                );

        return this.getTotalAmount().equals(payAmountSum);
    }
}
