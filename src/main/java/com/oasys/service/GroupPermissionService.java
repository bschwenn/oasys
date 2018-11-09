package com.oasys.service;

import com.oasys.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;

@Component(value="groupPermissionEvaluator")
public class GroupPermissionService {
    @Autowired
    private EntityManager em;

    public boolean isInGroup(Authentication authentication, Long gid) {
        String username = (String) authentication.getPrincipal();
        TypedQuery<Person> query = em.createQuery(
                String.format(
                        "SELECT p " +
                        "FROM Person p, Flock f, Member m " +
                        "WHERE p.username = '%s' " +
                        "AND m.gid = f.gid " +
                        "AND m.uid = p.uid " +
                        "AND f.gid = %s", username, gid
                ),
                Person.class
        );
        try {
            Person user = query.getSingleResult();
            return user != null;
        } catch (NoResultException e) {
            return false;
        }
    }
}