package com.oasys.controllers;

import com.oasys.entities.Flock;
import com.oasys.entities.Post;
import com.oasys.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;

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
    public Post createPost(@RequestBody Post post) {
        postRepository.save(post);
        // Timestamp will be null on return even though it gets set
        return post;
    }
}
