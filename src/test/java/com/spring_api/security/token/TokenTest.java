package com.spring_api.security.token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.spring_api.security.user.Role;
import com.spring_api.security.user.User;

public class TokenTest {

  private Token token;
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
            .build();

    token = Token.builder()
            .id(1)
            .token("abc123")
            .tokenType(TokenType.BEARER)
            .revoked(false)
            .expired(false)
            .user(user)
            .build();
  }

  @Test
  public void testGetId() {
    Assertions.assertEquals(1, token.getId());
  }

  @Test
  public void testGetToken() {
    Assertions.assertEquals("abc123", token.getToken());
  }

  @Test
  public void testGetTokenType() {
    Assertions.assertEquals(TokenType.BEARER, token.getTokenType());
  }

  @Test
  public void testIsRevoked() {
    Assertions.assertFalse(token.isRevoked());
  }

  @Test
  public void testIsExpired() {
    Assertions.assertFalse(token.isExpired());
  }

  @Test
  public void testGetUser() {
    Assertions.assertEquals(user, token.getUser());
  }

}