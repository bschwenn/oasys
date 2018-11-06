package com.oasys.repository;


import com.oasys.entities.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource()
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.gid IN (SELECT f.gid FROM Follows f WHERE f.uid = :uid)")
    List<Post> getUserFeed(@Param("uid") long uid);

    @Query("SELECT p FROM Post p WHERE p.gid = :gid")
    List<Post> getGroupFeed(@Param("gid") long gid);
}
