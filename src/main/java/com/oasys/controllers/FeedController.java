package com.oasys.controllers;

import com.oasys.entities.Person;
import com.oasys.entities.Post;
import com.oasys.repository.PersonRepository;
import com.oasys.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedController {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FeedService feedService;

    @RequestMapping("/feed")
    public List<Post> getFeed() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            // TODO (return not authorized)
            return null;
        }
        String username = (String) principal;
        Person user = personRepository.findByUsername(username);
        return feedService.getUserFeed(user);
    }
}
