package com.oasys.controllers;

import com.oasys.config.Constants;
import com.oasys.entities.*;
import com.oasys.repository.*;
import com.oasys.service.GroupPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private MemberRequestRepository memberRequestRepository;

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
        return flockRepository.save(flock);
    }

    @PostMapping("/flocks/{fid}/members/{uid}")
    // Look out below for Object vs. String
    @PreAuthorize("@groupPermissionEvaluator.isGroupAdmin(authentication.principal.toString(), #fid)")
    public Flock addPersonToFlock(@PathVariable long uid, @PathVariable Long fid) {
        Optional<Person> userBox = personRepository.findById(uid);
        Optional<Flock> flockBox = flockRepository.findById(fid);
        if (!userBox.isPresent() || !flockBox.isPresent()) return null;
        Person user = userBox.get();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Person initiator = personRepository.findByUsername((String) principal);
        if (flockBox.isPresent()) {
            Flock flock = flockBox.get();
            flock.removeMemberRequest(user, memberRequestRepository);
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
            user.removeFlock(flock, memberRecordRepository);
            personRepository.save(user);
            return flock;
        }
    }

    @RequestMapping("/flocks/{fid}/feed/{page}")
    public List<Post> getFlockPosts(@PathVariable long fid, @PathVariable int page, Principal principal) {
        Pageable pageRequest = PageRequest.of(page, Constants.PAGE_SIZE, Sort.Direction.DESC, "timestamp");
        if (principal != null && groupPermissionService.isInGroup(principal.getName(), fid)) {
            return postRepository.findByGid(fid, pageRequest).stream().collect(Collectors.toList());
        } else {
            return postRepository.getPublicFeed(fid, pageRequest).stream().collect(Collectors.toList());
        }
    }

    @RequestMapping("/flocks/{fid}/admins")
    public Set<Person> getFlockAdmins(@PathVariable long fid) {
        Optional<Flock> flockBox = flockRepository.findById(fid);
        return flockBox.isPresent() ? flockBox.get().getAdmins() : new HashSet<>();
    }

    @GetMapping("/is_admin/{fid}")
    public Person currentIsAdmin(@PathVariable long fid, Principal principal) {
        Optional<Flock> flockBox = flockRepository.findById(fid);
        if (principal == null)
            return null;
        Person current = personRepository.findByUsername(principal.getName());
        return flockBox.isPresent() && flockBox.get().getAdmins().contains(current) ? current : null;
    }

    @RequestMapping("/flocks/{fid}/requests")
    public Set<Person> getMemberRequests(@PathVariable long fid) {
        Optional<Flock> flockBox = flockRepository.findById(fid);
        if(!flockBox.isPresent())
            return new HashSet<>();

        Set<MemberRequest> requests = flockBox.get().getMemberRequests();

        Set<Person> requesters = new HashSet<>();

        for(MemberRequest r : requests) {
            requesters.add(r.getMember());
        }

        return requesters;
    }

    @RequestMapping(value = "/flocks/{gid}/requests/{uid}", method = {RequestMethod.POST, RequestMethod.DELETE})
    public Flock modifyMemberRequest(@PathVariable long uid, @PathVariable long gid,
                                     HttpServletRequest request, Principal principal) {
        Person user;
        if (uid == 0) {
            user = personRepository.findByUsername(principal.getName());
        } else {
            Optional<Person> userBox = personRepository.findById(uid);
            if (!userBox.isPresent()) return null;
            user = userBox.get();
        }
        Optional<Flock> flockBox = flockRepository.findById(gid);

        if (!flockBox.isPresent() || principal == null) return null;
        Flock flock = flockBox.get();

        if (request.getMethod().equals("POST")) {
            flock.addMemberRequest(user, personRepository.findByUsername(principal.getName()).getUid(),
                    memberRequestRepository);
        } else {
            flock.removeMemberRequest(user, memberRequestRepository);
        }

        return flock;
    }

    @RequestMapping("/flocks/name/{flockName}")
    public Flock getFlockByName(@PathVariable String flockName) {
        Flock f = flockRepository.findByNameIgnoreCase(flockName);
        return f;
    }

    @RequestMapping(value = "/flocks/name/{flockName}/interests", method = {RequestMethod.POST})
    public Flock addRelatedInterests(Principal principal, @PathVariable String flockName,
                                     @RequestBody List<String> interestNames) {
        Flock flock = flockRepository.findByNameIgnoreCase(flockName);
        if (!groupPermissionService.isGroupAdmin(principal.getName(), flock.getGid())) {
            return flock;
        }
        for(String interestName : interestNames) {
            Interest interest = interestRepository.findByName(interestName);
            if (interest == null) {
                interest = new Interest(interestName, false);
                interestRepository.save(interest);
            }
            flock.addRelatedInterest(interest);
        }
        flockRepository.save(flock);
        return flock;
    }

    @GetMapping("/search/flocks/{name}")
    public List<Flock> searchByName(@PathVariable String name) {
        List<Flock> f = flockRepository.findByNameContainingIgnoreCase(name);
        return f;
    }
}
