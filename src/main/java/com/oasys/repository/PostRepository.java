package com.oasys.repository;


import com.oasys.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RepositoryRestResource()
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.gid = :gid AND p.kind = 'public' ORDER BY p.timestamp DESC")
    Page<Post> getPublicFeed(@Param("gid") long gid, Pageable pageRequest);
}
