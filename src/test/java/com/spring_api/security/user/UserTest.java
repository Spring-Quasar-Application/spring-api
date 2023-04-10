package com.spring_api.security.user;

import com.spring_api.security.token.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;
import java.util.List;

public class UserTest {

  private User user;

  @BeforeEach
  public void setUp() {
    user = User.builder()
            .id(1)
            .firstname("John")
            .lastname("Doe")
            .email("john.doe@example.com")
            .password("password")
            .role(Role.USER)
            .tokens(Collections.singletonList(new Token()))
            .build();
  }

  @Test
  public void testGetAuthorities() {
    List<SimpleGrantedAuthority> expectedAuthorities = Collections.singletonList(
            new SimpleGrantedAuthority(Role.USER.name())
    );
    Assertions.assertEquals(expectedAuthorities, user.getAuthorities());
  }

  @Test
  public void testGetUsername() {
    Assertions.assertEquals("john.doe@example.com", user.getUsername());
  }

  @Test
  public void testGetPassword() {
    Assertions.assertEquals("password", user.getPassword());
  }

  @Test
  public void testGetFirstname() {
    Assertions.assertEquals("John", user.getFirstname());
  }

  @Test
  public void testGetLastname() {
    Assertions.assertEquals("Doe", user.getLastname());
  }

  
  @Test
  public void testGetEmail() {
    Assertions.assertEquals("john.doe@example.com", user.getEmail());
  }

  @Test
  public void testIsAccountNonExpired() {
    Assertions.assertTrue(user.isAccountNonExpired());
  }

  @Test
  public void testIsAccountNonLocked() {
    Assertions.assertTrue(user.isAccountNonLocked());
  }

  @Test
  public void testIsCredentialsNonExpired() {
    Assertions.assertTrue(user.isCredentialsNonExpired());
  }

  @Test
  public void testIsEnabled() {
    Assertions.assertTrue(user.isEnabled());
  }

  @Test
  public void testUserDetails() {
    UserDetails userDetails = user;
    Assertions.assertEquals(user.getUsername(), userDetails.getUsername());
    Assertions.assertEquals(user.getPassword(), userDetails.getPassword());
    Assertions.assertEquals(user.isEnabled(), userDetails.isEnabled());
    Assertions.assertEquals(user.getAuthorities(), userDetails.getAuthorities());
  }
}