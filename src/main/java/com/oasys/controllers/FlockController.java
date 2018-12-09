package com.oasys.controllers;

import com.oasys.config.Constants;
import com.oasys.entities.Flock;
import com.oasys.entities.Person;
import com.oasys.entities.Post;
import com.oasys.repository.FlockRepository;
import com.oasys.repository.MemberRecordRepository;
import com.oasys.repository.PersonRepository;
import com.oasys.repository.PostRepository;
import com.oasys.service.GroupPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class FlockController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FlockRepository flockRepository;
    @Autowired
    private MemberRecordRepository memberRecordRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private GroupPermissionService groupPermissionService;

    @RequestMapping("/flocks/{fid}")
    public Flock getFlock(@PathVariable Long fid) {
        Optional<Flock> flockBox = flockRepository.findById(fid);
        if (flockBox.isPresent()) {
            return flockBox.get();
        } else {
            return null;
        }
    }

    @PostMapping("/flocks")
    @PreAuthorize("authentication.isAuthenticated()")
    public Flock addFlock(@RequestBody Flock flock) {
        Person user = personRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()
        );
        flock.setAdmins(new HashSet<>(Arrays.asList(user)));
        flockRepository.save(flock);
        return flock;
    }

    @PostMapping("/flocks/{fid}/members/{username}")
    // Look out below for Object vs. String
    @PreAuthorize("@groupPermissionEvaluator.isGroupAdmin(authentication.principal.toString(), #fid)")
    public Flock addPersonToFlock(@PathVariable String username, @PathVariable Long fid) {
        Person user = personRepository.findByUsername(username);
        Optional<Flock> flockBox = flockRepository.findById(fid);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Person initiator = personRepository.findByUsername((String) principal);
        if (flockBox.isPresent()) {
            Flock flock = flockBox.get();
            user.addFlock(flock, initiator.getUid(), memberRecordRepository);
            personRepository.save(user);
            return flock;
        } else {
            return null; // TODO (BEN): error handling :)
        }
    }

    @DeleteMapping("/flocks/{fid}/members/{username}")
    public Flock removePersonFromFlock(@PathVariable String username, @PathVariable Long fid) {
        Person user = personRepository.findByUsername(username);
        Optional<Flock> flockBox = flockRepository.findById(fid);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String deleter = (String) principal;
        if (!username.equals(deleter) && !groupPermissionService.isGroupAdmin(deleter, fid) || !flockBox.isPresent()) {
            return null; // Prob should redirect to some kind of unauthorized page
        } else {
            Flock flock = flockBox.get();
            user.removeFlock(flock);
            personRepository.save(user);
            return flock;
        }
    }

    @RequestMapping("/flocks/{fid}/feed/{page}")
    @PreAuthorize("@groupPermissionEvaluator.isInGroup(authentication.principal.toString(), #fid)")
    public List<Post> getFlockPosts(@PathVariable long fid, @PathVariable int page) {
        Pageable pageRequest = PageRequest.of(page, Constants.PAGE_SIZE, Sort.Direction.DESC, "timestamp");
        return postRepository.findAll(pageRequest).stream().collect(Collectors.toList());
    }

    @RequestMapping("/flocks/{fid}/public_feed/{page}")
    public List<Post> getFlockPublicPosts(@PathVariable long fid, @PathVariable int page) {
        Pageable pageRequest = PageRequest.of(page, Constants.PAGE_SIZE, Sort.Direction.DESC, "timestamp");
        return postRepository.getPublicFeed(fid, pageRequest).stream().collect(Collectors.toList());
    }

    @RequestMapping("/flocks/{fid}/admins")
    public Set<Person> getFlockAdmins(@PathVariable long fid) {
        Optional<Flock> flockBox = flockRepository.findById(fid);
        return flockBox.isPresent() ? flockBox.get().getAdmins() : new HashSet<>();
    }

    @RequestMapping("/flocks/name/{flockName}")
    public Flock getFlockByName(@PathVariable String flockName) {
        Flock f = flockRepository.findByName(flockName);
        return f;
    }
}
