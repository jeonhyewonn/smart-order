package com.example.smartorder.user.repository;

import com.example.smartorder.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public void save(User user) {
        this.em.persist(user);
    }

    public User findByAccessId(String accessId) {
        List<User> results = this.em.createQuery("SELECT u FROM User u WHERE u.account.accessId = :accessId", User.class)
                .setParameter("accessId", accessId)
                .getResultList();
        if (results.isEmpty()) return null;

        return results.get(0);
    }

    public User findById(UUID id) {
        return this.em.find(User.class, id);
    }
}
