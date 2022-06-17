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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Order putOrder(String memberId, OrderCommand newOrder) {
        Member member = this.memberRepository.findById(memberId);
        if (member == null) throw new NotFoundMemberException();

        String[] itemIds = newOrder.getOrderItemCommands().stream().map(OrderItemCommand::getItemId).toArray(String[]::new);
        List<Item> items = this.itemRepository.findByIds(itemIds);
        if (items.size() != itemIds.length) throw new NotFoundItemException();

        if (!newOrder.isTotalAmountCorrect(items)) throw new IncorrectTotalAmountException();

        Order order = Order.createBy(newOrder, member, items);
        this.orderRepository.save(order);

        return order;
    }

    public Order getOrder(String memberId, String orderId) {
        Order order = this.orderRepository.findById(orderId);
        if (order == null) throw new NotFoundOrderException();
        if (!order.getMember().getId().equals(memberId)) throw new NotFoundMemberException();

        return order;
    }
}