package com.oasys.repository;

import com.oasys.entities.Person;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GroupRepository extends PagingAndSortingRepository<Person, Long> {
}
