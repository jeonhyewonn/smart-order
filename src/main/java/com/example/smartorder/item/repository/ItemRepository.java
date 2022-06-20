package com.example.smartorder.item.repository;

import com.example.smartorder.item.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ItemRepository {
    private final EntityManager em;

    public List<Item> findAll() {
        return this.em.createQuery("SELECT i FROM Item i WHERE i.isDeleted = :isDeleted", Item.class)
                .setParameter("isDeleted", false)
                .getResultList();
    }

    public List<Item> findByIds(List<String> ids) {
        return this.em.createQuery("SELECT i FROM Item i WHERE i.isDeleted = :isDeleted AND i.id IN :ids", Item.class)
                .setParameter("isDeleted", false)
                .setParameter("ids", ids)
                .getResultList();
    }
}
