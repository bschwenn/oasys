package com.oasys.service;

import com.oasys.entities.Flock;
import com.oasys.entities.Person;
import com.oasys.entities.Post;
import com.oasys.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> getUserFeed(Person user) {
        // TODO (Ben): not using a query here because we're probably going to want granular control
        // over what goes in a feed, but this will be a silly/inefficient method as number of posts
        // grows.
        List<Post> feedPosts = new ArrayList<>();
        for (Flock f : user.getFlocks()) {
            feedPosts.addAll(postRepository.getGroupFeed(f.getGid()));
        }
        return feedPosts;
    }
}
