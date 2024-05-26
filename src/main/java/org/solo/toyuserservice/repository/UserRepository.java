package org.solo.toyuserservice.repository;

import org.solo.toyuserservice.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByUsername(String username);
    UserEntity findByUsername(String username);
    void deleteByUsername(String username);

    @Modifying
    @Query("update UserEntity set name = :name, email = :email where username = :username")
    void updateByUsername(@Param(value = "username") String username,
                          @Param(value = "name") String name,
                          @Param(value = "email") String email);
}
