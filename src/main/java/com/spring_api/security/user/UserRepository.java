package com.spring_api.security.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  User findByResetPasswordToken(String token);

  @Transactional
  @Modifying
  @Query("update User u set u.resetPasswordToken = :token where u.email = :email")
  void updateResetPasswordToken(@Param("email") String email, @Param("token") String token);

}
