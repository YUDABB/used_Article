package com.example.Used.Article.controller;

import com.example.Used.Article.domain.CartItem;
import com.example.Used.Article.domain.Member;
import com.example.Used.Article.service.CartService;
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
public class CartController {


        private final CartService cartService;
        private final MemberService memberService;
        private final OrderService orderService;

        // 장바구니 담기 & 상품 주문
        @PostMapping("/cart/{itemId}")
        public String processAction(@PathVariable("itemId") Long itemId,
                                @RequestParam("count") int count,
                                @RequestParam("action") String action) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Member member = memberService.getUser(username);
            Long memberId = member.getId();

            if ("order".equals(action)) { // 상품 주문
                orderService.order(memberId, itemId, count);
                return "redirect:/item";
            } else if ("cart".equals(action)) { // 장바구니 담기
                cartService.addCart(memberId, itemId, count);
                return "redirect:/cart/list";
            } else {
                return "redirect:/";
            }
        }

        // 장바구니 리스트
        @GetMapping("/cart/list")
        public String cartList(Model model) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Member member = memberService.getUser(username);
            Long memberId = member.getId();
            List<CartItem> cartItems = cartService.getCartItemsByMemberId(memberId);
            model.addAttribute("cartItems", cartItems);
            return "cartListForm";
        }

        // 장바구니 상품 취소
        @PostMapping("/cart/{cartId}/cancel/{cartItemId}")
        public String cancelCartItem(@PathVariable("cartId") Long cartId, @PathVariable("cartItemId") Long cartItemId) {
            cartService.cancelCartItem(cartId, cartItemId);
            return "redirect:/cart/list";
        }


    }


