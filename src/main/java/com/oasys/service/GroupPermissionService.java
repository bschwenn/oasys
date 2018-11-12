package com.oasys.service;

import com.oasys.entities.Flock;
import com.oasys.entities.Person;
import com.oasys.repository.FlockRepository;
import com.oasys.repository.PersonRepository;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Optional;

@Service(value="groupPermissionEvaluator")
public class GroupPermissionService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FlockRepository flockRepository;

    public boolean isInGroup(String username, Long gid) {
        Person person = personRepository.findByUsername(username);
        Optional<Flock> flockBox = flockRepository.findById(gid);
        if (flockBox.isPresent() && person != null && person.getFlocks() != null) {
            return person.getFlocks().contains(flockBox.get());
        } else {
            return false;
        }
    }

    public boolean isGroupAdmin(String username, Long gid) {
        Person user = personRepository.findByUsername(username);
        Optional<Flock> flockBox = flockRepository.findById(gid);
        if (flockBox.isPresent()) {
            return flockBox.get().getAdmins().contains(user);
        } else {
            return false;
        }
    }
}