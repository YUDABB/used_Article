package com.example.Used.Article.service;

import com.example.Used.Article.domain.*;
import com.example.Used.Article.exception.NotEnoughStockException;
import com.example.Used.Article.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    @Transactional
    public Long addCart(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        Cart cart = cartRepository.findByMemberId(memberId);
        if (cart == null) {
            cart = new Cart();
            cart.setMember(member);
        }
        CartItem cartItem = new CartItem();
        cartItem.setItem(item);
        cartItem.setCart(cart);
        cartItem.setCart_count(count);

        cart.addItem(cartItem);

        cartRepository.save(cart);
        return cart.getId();
    }

    public List<CartItem> getCartItemsByMemberId(Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId);
        if (cart != null) {
            return cart.getCartItems();
        } else {
            return new ArrayList<>();
        }
    }
    public void cancelCartItem(Long cartId, Long cartItemId) {

        Cart cart = cartRepository.findOne(cartId);
        if (cart == null) {
            throw new NotEnoughStockException("장바구니를 찾을 수 없습니다.");
        }
        cart.removeCartItem(cartItemId);

        cartRepository.deleteCartItemById(cartItemId);
    }

}
