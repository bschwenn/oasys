package com.oasys.controllers;

import com.oasys.entities.Person;
import com.oasys.entities.Post;
import com.oasys.repository.PersonRepository;
import com.oasys.repository.PostRepository;
import com.oasys.service.GroupPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private GroupPermissionService groupPermissionService;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/posts/{pid}")
    public Post getPost(@PathVariable Long pid) {
        Optional<Post> postBox = postRepository.findById(pid);
        if (postBox.isPresent()) {
            return postBox.get();
        } else {
            return null;
        }
    }

    @PostMapping("/posts")
    public Post createPost(Principal principal, @RequestBody Post post) {
        String username = principal.getName();
        if (!groupPermissionService.isInGroup(username, post.getGid())) {
            return null;
        }
        Person user = personRepository.findByUsername(username);
        post.setCreatorUid(user.getUid());
        postRepository.save(post);
        // Timestamp will be null on return even though it gets set
        return post;
    }
}
