package com.example.Used.Article.repository;

import com.example.Used.Article.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) { if (item.getId() == null) {em.persist(item);} else { em.merge(item); } }

    public void deleteItem(Item item) {
        em.remove(em.contains(item) ? item : em.merge(item));
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
    public List<Item> findByMemberId(Long memberId) {
        return em.createQuery("select i from Item i where i.user.id = :memberId", Item.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<Item> findByItemName(String keyword) {
        return em.createQuery("SELECT i FROM Item i WHERE i.name LIKE :keyword", Item.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    public List<Item> findByItemStatus(String keyword) {
        return em.createQuery("SELECT i FROM Item i WHERE i.detail LIKE :keyword", Item.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

}
