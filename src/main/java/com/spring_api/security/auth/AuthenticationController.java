package com.spring_api.security.auth;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring_api.entity.EmailDetails;
import com.spring_api.security.user.User;
import com.spring_api.security.user.UserRepository;
import com.spring_api.service.EmailService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final UserRepository userRepository;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;

  @CrossOrigin(origins = "http://localhost:8090")
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(service.register(request));
  }

  @CrossOrigin(origins = "http://localhost:8090")
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/forgot-password")
  public void forgotPassword(@RequestParam String email) {
    User user = null;
    if (userRepository.findByEmail(email).isPresent()) {
      user = userRepository.findByEmail(email).get();
    }

    if (user != null) {
      String resetPasswordToken = UUID.randomUUID().toString();
      userRepository.updateResetPasswordToken(email, resetPasswordToken);
      String resetPasswordLink = "http://localhost:8080/reset-password?token=" + resetPasswordToken;
      EmailDetails emailDetails = new EmailDetails("curimanalexandru@gmail.com",
          "Click on the link to reset your password: " + resetPasswordLink, "Reset Password");
      emailService.sendSimpleMail(emailDetails);

    }
  }

  @GetMapping("/reset-password")
  public void showResetPasswordForm(@RequestParam String token) {
    // Check if the token is valid
    User user = userRepository.findByResetPasswordToken(token);
    if (user != null) {
      // Show the reset password form
    } else {
      // Redirect to an error page
    }
  }

  @PostMapping("/reset-password")
  public void resetPassword(@RequestParam String token, @RequestParam String password) {
    // Check if the token is valid
    User user = userRepository.findByResetPasswordToken(token);
    if (user != null) {
      user.setPassword(passwordEncoder.encode(password));
      user.setResetPasswordToken(null);
      userRepository.save(user);
    } else {
      // Redirect to an error page
    }
  }

}