package com.oasys.repository;

import com.oasys.entities.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource()
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
    Person findByUsername(@Param("username") String username);
}
