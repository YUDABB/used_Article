package com.example.Used.Article.service;

import com.example.Used.Article.domain.*;
import com.example.Used.Article.domain.Item;
import com.example.Used.Article.domain.Member;
import com.example.Used.Article.domain.OrderItem;
import com.example.Used.Article.repository.ItemRepository;
import com.example.Used.Article.repository.MemberRepository;
import com.example.Used.Article.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, orderItem);

        orderRepository.save(order);
        return order.getId();

    }

    // 주문취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    // 주문 조회
    public List<Order> findOrders(String username) {
        return orderRepository.findAllByUsername(username);
    }
}
