package com.example.Used.Article.repository;

import jakarta.persistence.EntityManager;

import com.example.Used.Article.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByUsername(String username) {
        return em.createQuery("select o from Order o join o.member m where m.username = :username", Order.class)
                .setParameter("username", username)
                .getResultList();
    }

}

