package com.example.Used.Article.repository;

import com.example.Used.Article.domain.Cart;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final EntityManager em;

    public void save(Cart cart) {
        em.persist(cart);
    }

    public Cart findOne(Long id) {
        return em.find(Cart.class, id);
    }

    public Cart findByMemberId(Long memberId) {
        return em.createQuery("select c from Cart c where c.member.id = :memberId", Cart.class)
                .setParameter("memberId", memberId)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
    @Transactional
    public void deleteCartItemById(Long cartItemId) {
        em.createQuery("delete from CartItem ci where ci.id = :cartItemId")
                .setParameter("cartItemId", cartItemId)
                .executeUpdate();
    }

}
