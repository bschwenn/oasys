package com.oasys.service;

import com.oasys.config.Constants;
import com.oasys.entities.Event;
import com.oasys.entities.Person;
import com.oasys.entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Date;

@Service
public class FeedService {
    @Autowired
    private EntityManager em;

    public List<Post> getUserFeedPosts(Person user, int lastPage) {
        // TODO (Ben): we're probably going to want the feed to be more customizable
        TypedQuery<Post> postQuery = em.createQuery(
                String.format(
                        "SELECT DISTINCT p " +
                        "FROM Post p, MemberRecord m, FollowRecord f " +
                        "WHERE m.member.uid = %s " +
                        "AND (p.gid = m.flock.gid " +
                        "OR (p.gid = f.flock.gid AND p.kind='public')) " +
                        "ORDER BY p.timestamp DESC",
                        user.getUid()
                ),
                Post.class
        );
        postQuery.setFirstResult(lastPage * Constants.PAGE_SIZE);
        postQuery.setMaxResults(Constants.PAGE_SIZE);
        List<Post> posts = postQuery.getResultList();
        return posts;
    }

    public List<Event> getUserFeedEvents(Person user, int lastPage) {
        TypedQuery<Event> eventQuery = em.createQuery(
                String.format(
                        "SELECT DISTINCT e " +
                                "FROM Event e, MemberRecord m, FollowRecord f " +
                                "WHERE m.member.uid = %s " +
                                "AND e.time >= NOW() " +
                                "AND (e.gid = m.flock.gid " +
                                "OR (e.gid = f.flock.gid AND e.isPublic)) " +
                                "ORDER BY e.time ASC",
                        user.getUid()
                ),
                Event.class
        );
        eventQuery.setFirstResult(lastPage * Constants.PAGE_SIZE);
        eventQuery.setMaxResults(Constants.PAGE_SIZE);
        return eventQuery.getResultList();
    }
}
