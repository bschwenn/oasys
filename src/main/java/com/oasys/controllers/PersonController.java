package com.oasys.controllers;

import com.oasys.entities.Flock;
import com.oasys.entities.Interest;
import com.oasys.entities.Person;
import com.oasys.repository.FlockRepository;
import com.oasys.repository.InterestRepository;
import com.oasys.repository.PersonRepository;
import com.oasys.repository.StudyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/persons/{username}")
    public Person getPerson(@PathVariable String username) {
        return personRepository.findByUsername(username);
    }

    @PostMapping("/persons")
    public Person createPerson(@RequestBody Person person) {
        personRepository.save(person);
        String encryptedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encryptedPassword);
        return person;
    }

    @RequestMapping("/persons/{username}/majors")
    public Set<Interest> getMajors(@PathVariable String username) {
        return personRepository.findByUsername(username).getMajors();
    }

    @RequestMapping("/persons/{username}/minors")
    public Set<Interest> getMinors(@PathVariable String username) {
        return personRepository.findByUsername(username).getMinors();
    }

    @RequestMapping("/persons/{username}/flocks")
    public Set<Flock> getPersonFlocks(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getFlocks();
    }

    @RequestMapping("/persons/{username}/follows")
    public Set<Flock> getPersonFollows(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getFollowedFlocks();
    }

    @PostMapping("/persons/{username}/follows/{fid}")
    @PreAuthorize("#username.equals(authentication.principal)")
    public Flock followFlock(@PathVariable String username, @PathVariable Long fid) {
        Person user = personRepository.findByUsername(username);
        Optional<Flock> flockBox = flockRepository.findById(fid);
        if (flockBox.isPresent()) {
            Flock flock = flockBox.get();
            user.getFollowedFlocks().add(flock);
            personRepository.save(user);
            return flock;
        } else {
            return null; // TODO (BEN): error handling :)
        }
    }

    @RequestMapping("/persons/{username}/interests")
    public Set<Interest> getPersonInterests(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getInterests();
    }

    @RequestMapping(value = "/persons/{username}/interests/{iid}", method = {RequestMethod.DELETE, RequestMethod.POST})
    @PreAuthorize("#username.equals(authentication.principal)")
    public Person addInterest(@PathVariable String username, @PathVariable Long iid, String kind,
                              HttpServletRequest request) {
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
                user.removeStudy(interest);
            } else {
                user.removeInterest(interest);
            }
        }
        personRepository.save(user);
        return user;
    }

    @PostMapping(value = "/persons/{username}/graduation_year/{graduationYear}")
    @PreAuthorize("#username.equals(authentication.principal)")
    public Person updateGraduationYear(@PathVariable String username, @PathVariable int graduationYear) {
        Person person = personRepository.findByUsername(username);
        person.setGraduationYear(graduationYear);
        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/persons/{username}/email/{email}")
    @PreAuthorize("#username.equals(authentication.principal)")
    public Person updateEmail(@PathVariable String username, @PathVariable String email) {
        Person person = personRepository.findByUsername(username);
        person.setEmail(email);
        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/persons/{username}/name/{name}")
    @PreAuthorize("#username.equals(authentication.principal)")
    public Person updateName(@PathVariable String username, @PathVariable String name) {
        Person person = personRepository.findByUsername(username);
        person.setName(name);
        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/persons/{username}/links")
    @PreAuthorize("#username.equals(authentication.principal)")
    public Person updateLinks(@PathVariable String username, @RequestBody String links) {
        Person person = personRepository.findByUsername(username);
        person.setExternalLinks(links);
        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/persons/{username}/photo_path/{photoPath}")
    @PreAuthorize("#username.equals(authentication.principal)")
    public Person updatePhotoPath(@PathVariable String username, @PathVariable String photoPath) {
        Person person = personRepository.findByUsername(username);
        person.setPhotoPath(photoPath);
        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/persons/{username}/password", consumes = "text/plain")
    @PreAuthorize("#username.equals(authentication.principal)")
    public Person updatePassword(@PathVariable String username, @RequestBody String password) {
        Person person = personRepository.findByUsername(username);
        String encryptedPassword = passwordEncoder.encode(password);
        person.setPassword(encryptedPassword);
        personRepository.save(person);
        return person;
    }
}
