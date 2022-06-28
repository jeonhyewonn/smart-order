package com.example.smartorder.order.service;

import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.exception.NotFoundItemException;
import com.example.smartorder.item.repository.ItemRepository;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.exception.NotFoundMemberException;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.exception.IncorrectTotalAmountException;
import com.example.smartorder.order.exception.NotFoundOrderException;
import com.example.smartorder.order.repository.OrderRepository;
import com.example.smartorder.order.service.dto.OrderCommand;
import com.example.smartorder.order.service.dto.OrderItemCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Order putOrder(Long memberId, OrderCommand newOrder) {
        Member member = this.memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<Long> itemIds = newOrder.getOrderItemCommands().stream().map(OrderItemCommand::getItemId).collect(toList());
        List<Item> items = this.itemRepository.findByIds(itemIds);
        if (items.size() != itemIds.size()) throw new NotFoundItemException();

        if (!newOrder.isTotalAmountCorrect(items)) throw new IncorrectTotalAmountException();

        Order order = Order.createBy(newOrder, member, items);
        this.orderRepository.save(order);

        return order;
    }

    public Order getOrder(Long memberId, Long orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);
        if (!order.getMember().getId().equals(memberId)) throw new NotFoundMemberException();

        return order;
    }

    @Transactional
    public void cancelOrder(Long memberId, Long orderId) {
        Order order = this.getOrder(memberId, orderId);

        order.cancel();
    }
}
