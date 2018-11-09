package com.oasys.controllers;

import com.oasys.entities.Flock;
import com.oasys.entities.Person;
import com.oasys.entities.Post;
import com.oasys.repository.PersonRepository;
import com.oasys.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FeedController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/feed")
    public List<Post> getFeed() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            // TODO (return not authorized)
            return null;
        }
        String username = (String) principal;
        Person user = personRepository.findByUsername(username);
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
