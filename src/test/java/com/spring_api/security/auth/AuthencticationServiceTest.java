package com.spring_api.security.auth;

import com.spring_api.security.config.JwtService;
import com.spring_api.security.token.TokenRepository;
import com.spring_api.security.user.Role;
import com.spring_api.security.user.User;
import com.spring_api.security.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthencticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setEmail("johndoe@example.com");
        request.setPassword("password");

        User user = User.builder()
                .id(1)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password("encodedpassword")
                .role(Role.USER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        // When
        AuthenticationResponse response = authenticationService.register(request);

        // Then
        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    public void testAuthenticate() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("johndoe@example.com");
        request.setPassword("password");

        User user = User.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .email(request.getEmail())
                .password("encodedpassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        // When
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Then
        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(tokenRepository, times(1)).save(any());
    }
}
