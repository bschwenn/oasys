package com.oasys.controllers;

import com.oasys.entities.Comment;
import com.oasys.entities.Person;
import com.oasys.entities.Post;
import com.oasys.repository.CommentRepository;
import com.oasys.repository.PersonRepository;
import com.oasys.repository.PostRepository;
import com.oasys.service.GroupPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private GroupPermissionService groupPermissionService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CommentRepository commentRepository;

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

    @PostMapping("/posts/{pid}/comment")
    public Post addComment(Principal principal, @PathVariable Long pid, @RequestBody String commentBody) {
        String username = principal.getName();
        Optional<Post> postBox = postRepository.findById(pid);
        if (!postBox.isPresent()) {
            return null;
        }
        Post post = postBox.get();
        if (!groupPermissionService.isInGroup(username, post.getGid())) {
            return null;
        }
        Person user = personRepository.findByUsername(username);
        Comment comment = new Comment(commentBody, user.getUid(), post.getPid());
        commentRepository.save(comment);
        post.addComment(comment);
        postRepository.save(post);
        return post;
    }

    @GetMapping("/posts/{pid}/comments")
    public List<Comment> getPostComments(@PathVariable Long pid) {
        Optional<Post> postBox = postRepository.findById(pid);
        if (postBox.isPresent()) {
            return postBox.get().getComments();
        } else {
            return new ArrayList<>();
        }
    }
}
