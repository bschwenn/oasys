package com.oasys.controllers;

import com.oasys.entities.*;
import com.oasys.repository.*;
import com.oasys.service.GroupPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private PinRecordRepository pinRecordRepository;
    @Autowired
    private LikeRecordRepository likeRecordRepository;

    @GetMapping("/posts/{pid}")
    public Post getPost(@PathVariable Long pid, Principal principal) {
        Optional<Post> postBox = postRepository.findById(pid);
        String username = principal.getName();
        if (postBox.isPresent()) {
            Post post = postBox.get();
            if (!groupPermissionService.isInGroup(username, post.getGid())) {
                return null;
            }
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

    @PostMapping("/current_user/pin/{pid}")
    public Post pinPost(Principal principal, @PathVariable Long pid) {
        if (principal == null) return null;
        Optional<Post> postBox = postRepository.findById(pid);
        if (!postBox.isPresent()) {
            return null;
        }

        Person user = personRepository.findByUsername(principal.getName());
        Post post = postBox.get();

        user.pin(post, pinRecordRepository);
        return post;
    }

    @PostMapping("/current_user/like/{pid}")
    public Post likePost(Principal principal, @PathVariable Long pid) {
        if (principal == null) return null;
        Optional<Post> postBox = postRepository.findById(pid);
        if (!postBox.isPresent()) {
            return null;
        }

        Person user = personRepository.findByUsername(principal.getName());
        Post post = postBox.get();

        user.like(post, likeRecordRepository);
        return post;
    }

    @GetMapping("/posts/{pid}/likers")
    public Set<Person> getLikers(@PathVariable Long pid) {
        Optional<Post> postBox = postRepository.findById(pid);
        if (!postBox.isPresent()) {
            return new HashSet<>();
        }

        Post post = postBox.get();
        return post.getLikeRecords().stream().map(LikeRecord::getPerson).collect(Collectors.toSet());
    }

    @GetMapping("/posts/{pid}/pinners")
    public Set<Person> getPinners(@PathVariable Long pid) {
        Optional<Post> postBox = postRepository.findById(pid);
        if (!postBox.isPresent()) {
            return new HashSet<>();
        }

        Post post = postBox.get();
        return post.getPinRecords().stream().map(PinRecord::getPerson).collect(Collectors.toSet());
    }
}
