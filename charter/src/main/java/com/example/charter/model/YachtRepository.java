package com.example.charter.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;


//  Application will not start without connection with database.
//  If mysql service is not running, prompt will throw error about that
//  hibernate connection has be configured (when it actually is)

@Repository
public interface YachtRepository extends JpaRepository<Yacht, Integer> {




}
