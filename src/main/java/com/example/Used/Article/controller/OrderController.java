package com.example.Used.Article.controller;

import com.example.Used.Article.domain.Order;
import com.example.Used.Article.domain.Member;
import com.example.Used.Article.service.MemberService;
import com.example.Used.Article.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;

    // 상품 주문
    @PostMapping("/order/{itemId}")
    public String order(@PathVariable("itemId") Long itemId,
                        @RequestParam("count") int count) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberService.getUser(username);
        Long memberId = member.getId();

        orderService.order(memberId, itemId, count);
        return "redirect:/item";
    }

    // 주문한 상품들
    @GetMapping("/order/list")
    public String orderList(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Order> orders = orderService.findOrders(username);
        model.addAttribute("orders", orders);

        return "orderListForm";
    }


    // 주문한 상품 취소
    @PostMapping("/order/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/order/list";
    }
}
