package com.oasys.service;

import com.oasys.config.Constants;
import com.oasys.entities.Person;
import com.oasys.entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class FeedService {
    @Autowired
    private EntityManager em;

    public List<Post> getUserFeed(Person user, int lastPage) {
        // TODO (Ben): we're probably going to want the feed to be more customizable
        TypedQuery<Post> postQuery = em.createQuery(
                String.format(
                        "SELECT p " +
                        "FROM Post p, MemberRecord m " +
                        "WHERE m.member.uid = %s " +
                        "AND p.gid = m.flock.gid " +
                        "ORDER BY p.timestamp DESC",
                        user.getUid()
                ),
                Post.class
        );
        postQuery.setFirstResult(lastPage * Constants.PAGE_SIZE);
        postQuery.setMaxResults(Constants.PAGE_SIZE);
        return postQuery.getResultList();
    }
}
