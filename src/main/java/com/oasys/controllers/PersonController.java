package com.oasys.controllers;

import com.oasys.entities.Flock;
import com.oasys.entities.Interest;
import com.oasys.entities.Person;
import com.oasys.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FlockRepository flockRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private StudyRecordRepository studyRecordRepository;
    @Autowired
    private FollowRecordRepository followRecordRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/persons/{username}")
    public Person getPerson(@PathVariable String username) {
        return personRepository.findByUsername(username);
    }

    @RequestMapping("/current_user")
    @ResponseBody
    public Person getPerson(Principal principal) {
        String username = principal.getName();
        return personRepository.findByUsername(username);
    }

    @PostMapping("/persons")
    public Person createPerson(@RequestBody Person person) {
        String encryptedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encryptedPassword);
        personRepository.save(person);
        return person;
    }

    @RequestMapping("/persons/{username}/majors")
    public Set<Interest> getMajors(@PathVariable String username) {
        return personRepository.findByUsername(username).getMajors();
    }

    @RequestMapping("/current_user/majors")
    @ResponseBody
    public Set<Interest> getMajors(Principal principal) {
        String username = principal.getName();
        return personRepository.findByUsername(username).getMajors();
    }

    @RequestMapping("/persons/{username}/minors")
    public Set<Interest> getMinors(@PathVariable String username) {
        return personRepository.findByUsername(username).getMinors();
    }

    @RequestMapping("/current_user/minors")
    @ResponseBody
    public Set<Interest> getMinors(Principal principal) {
        String username = principal.getName();
        return personRepository.findByUsername(username).getMinors();
    }

    @RequestMapping("/persons/{username}/flocks")
    public Set<Flock> getPersonFlocks(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getFlocks();
    }

    @RequestMapping("/current_user/flocks")
    public Set<Flock> getPersonFlocks(Principal principal) {
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        return user.getFlocks();
    }

    @RequestMapping("/persons/{username}/follows")
    public Set<Flock> getPersonFollows(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getFollowedFlocks();
    }

    @RequestMapping("/current_user/follows")
    public Set<Flock> getPersonFollows(Principal principal) {
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        return user.getFollowedFlocks();
    }

    @RequestMapping(value = "/current_user/follows/{gid}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public Person followFlock(Principal principal, @PathVariable Long gid,
                             HttpServletRequest request) {
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        Optional<Flock> flockBox = flockRepository.findById(gid);
        if (!flockBox.isPresent()) {
            return user;
        }
        Flock flock = flockBox.get();
        if (request.getMethod().equals("POST")) {
            user.follow(flock, followRecordRepository);
        } else { // DELETE
            user.unfollow(flock, followRecordRepository);
        }
        personRepository.save(user);
        return user;
    }

    @RequestMapping("/persons/{username}/admin_roles")
    public Set<Flock> getAdministratedFlocks(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getAdminForFlocks();
    }

    @RequestMapping("/current_user/admin_roles")
    public Set<Flock> getAdministratedFlocks(Principal principal) {
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        return user.getAdminForFlocks();
    }

    @RequestMapping("/persons/{username}/interests")
    public Set<Interest> getPersonInterests(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getInterests();
    }

    @RequestMapping("/current_user/interests")
    public Set<Interest> getPersonInterests(Principal principal) {
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        return user.getInterests();
    }

    @RequestMapping(value = "/current_user/interests/{iid}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public Person addInterest(Principal principal, @PathVariable Long iid, String kind,
                              HttpServletRequest request) {
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        Optional<Interest> interestBox = interestRepository.findById(iid);
        if (!interestBox.isPresent()) {
            return user;
        }
        Interest interest = interestBox.get();
        if (request.getMethod().equals("POST")) {
            if (kind != null) {
                user.addStudy(interest, kind, studyRecordRepository);
            } else {
                user.addInterest(interest);
            }
        } else { // DELETE
            if (kind != null) {
                user.removeStudy(interest, studyRecordRepository);
            } else {
                user.removeInterest(interest);
            }
        }
        personRepository.save(user);
        return user;
    }

    @RequestMapping(value = "/current_user/interests", method = {RequestMethod.POST})
    public Person addInterests(Principal principal, @RequestBody List<String> interestNames) {
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        for(String interestName : interestNames) {
            Interest interest = interestRepository.findByName(interestName);
            if (interest == null) {
                interest = new Interest(interestName, false);
                interestRepository.save(interest);
            }
            user.addInterest(interest);
        }
        personRepository.save(user);
        return user;
    }

    @RequestMapping(value = "/current_user/majors", method = {RequestMethod.POST})
    public Person addMajors(Principal principal, @RequestBody List<String> majorNames) {
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        for(String majorName : majorNames) {
            Interest interest = interestRepository.findByName(majorName);
            if (interest == null) {
                interest = new Interest(majorName, true);
                interestRepository.save(interest);
            }
            user.addStudy(interest, "major", studyRecordRepository);
        }
        personRepository.save(user);
        return user;
    }

    @RequestMapping(value = "/current_user/minors", method = {RequestMethod.POST})
    public Person addMinors(Principal principal, @RequestBody List<String> minorNames) {
        String username = principal.getName();
        Person user = personRepository.findByUsername(username);
        for(String minorName : minorNames) {
            Interest interest = interestRepository.findByName(minorName);
            if (interest == null) {
                interest = new Interest(minorName, true);
                interestRepository.save(interest);
            }
            user.addStudy(interest, "minor", studyRecordRepository);
        }
        personRepository.save(user);
        return user;
    }

    @PostMapping(value = "/current_user/graduation_year/{graduationYear}")
    public Person updateGraduationYear(Principal principal, @PathVariable int graduationYear) {
        String username = principal.getName();
        Person person = personRepository.findByUsername(username);
        person.setGraduationYear(graduationYear);
        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/current_user/email/{email}")
    public Person updateEmail(Principal principal, @PathVariable String email) {
        String username = principal.getName();
        Person person = personRepository.findByUsername(username);
        person.setEmail(email);
        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/current_user/name/{name}")
    public Person updateName(Principal principal, @PathVariable String name) {
        String username = principal.getName();
        Person person = personRepository.findByUsername(username);
        person.setName(name);
        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/current_user/links")
    public Person updateLinks(Principal principal, @RequestBody String links) {
        String username = principal.getName();
        Person person = personRepository.findByUsername(username);
        person.setExternalLinks(links);
        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/current_user/photo_path/{photoPath}")
    public Person updatePhotoPath(Principal principal, @PathVariable String photoPath) {
        String username = principal.getName();
        Person person = personRepository.findByUsername(username);
        person.setPhotoPath(photoPath);
        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/current_user/password", consumes = "text/plain")
    public Person updatePassword(Principal principal, @RequestBody String password) {
        String username = principal.getName();
        Person person = personRepository.findByUsername(username);
        String encryptedPassword = passwordEncoder.encode(password);
        person.setPassword(encryptedPassword);
        personRepository.save(person);
        return person;
    }

    @GetMapping("/search/people/{name}")
    public List<Person> searchByName(@PathVariable String name) {
        List<Person> l = personRepository.findByNameContainingIgnoreCase(name);
        return l;
    }
}
