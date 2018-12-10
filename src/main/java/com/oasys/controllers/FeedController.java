package com.oasys.controllers;

import com.oasys.entities.Event;
import com.oasys.entities.Person;
import com.oasys.entities.Post;
import com.oasys.repository.PersonRepository;
import com.oasys.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FeedController {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FeedService feedService;

    @RequestMapping("/feed/posts/{page}")
    public List<Post> getFeedPosts(@PathVariable int page, Principal principal) {
        if (principal == null) {
            return new ArrayList<>();
        }
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        return feedService.getUserFeedPosts(user, page);
    }

    @RequestMapping("/feed/events/{page}")
    public List<Event> getFeedEvents(@PathVariable int page, Principal principal) {
        if (principal == null) {
            return new ArrayList<>();
        }
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        return feedService.getUserFeedEvents(user, page);
    }
}
