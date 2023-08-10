package com.example.oauth2proxy.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T1 extends Serializable, T2 extends Serializable>
        extends JpaRepository<T1, T2>, JpaSpecificationExecutor<T1> {

    default Optional<T1> findOne(T2 id) {
        return findById(id);
    }

    default void deleteOne(T2 id) {
        deleteById(id);
    }
}