package com.example.msa.backend.usersvc.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.msa.backend.usersvc.domain.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.credentialsByUserId where u.userId = :userId")
  User findByUserId(@Param("userId") long userId);

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.credentialsByUserId where u.loginId = :loginId")
  User findByLoginId(@Param("loginId") String loginId);
}
