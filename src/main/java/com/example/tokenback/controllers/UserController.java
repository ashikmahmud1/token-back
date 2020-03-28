package com.example.tokenback.controllers;

import com.example.tokenback.models.ERole;
import com.example.tokenback.models.Role;
import com.example.tokenback.models.User;
import com.example.tokenback.payload.response.JwtResponse;
import com.example.tokenback.payload.response.MessageResponse;
import com.example.tokenback.repository.RoleRepository;
import com.example.tokenback.repository.UserRepository;
import com.example.tokenback.schema.CustomUser;
import com.example.tokenback.schema.ResetPassword;
import com.example.tokenback.security.jwt.JwtUtils;
import com.example.tokenback.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    // Get All Users
    @GetMapping("/")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getSingleUser(@PathVariable(value = "id") Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
    }

    // only admin can access this functionality
    // reset the user password
    @PutMapping("/reset-password/{id}")
    public User resetPassword(@PathVariable(value = "id") Long userId, @Valid @RequestBody ResetPassword resetPassword) {
        // check if the resetPassword.getPassword and resetPassword.getConfirmPassword is same
        // if not same raise error. password and confirmPassword should be same
        if (!resetPassword.getPassword().equals(resetPassword.getConfirmPassword()))
            throw new RuntimeException("Error: password and confirm password should be same.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        // encode the password
        // set the password to the user
        user.setPassword(encoder.encode(resetPassword.getPassword()));
        // save the user
        return userRepository.save(user);
    }

    // only admin can access this functionality
    // edit the user
    @PutMapping("/edit/{id}")
    public User editUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody CustomUser customUser) {
        // don't give them to change the email and username
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        user.setName(customUser.getName() != null ? customUser.getName() : user.getName());
        Set<String> strRoles = customUser.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_TOKENIST)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("staff".equals(role)) {
                    Role modRole = roleRepository.findByName(ERole.ROLE_STAFF)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_TOKENIST)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    // logged in user can access this functionality
    // reset currently logged in user password
    // after resetting password send the user including token
    @GetMapping("/me/reset-password/{id}")
    public ResponseEntity<?> resetOwnPassword(@PathVariable(value = "id") Long userId, @Valid @RequestBody ResetPassword resetPassword) {
        if (!resetPassword.getPassword().equals(resetPassword.getConfirmPassword()))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: password and confirm password should be same."));

        // get the user by id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        // compare the user password with resetPassword.getOldPassword()
        if (encoder.matches(resetPassword.getOldPassword(), user.getPassword())){
            user.setPassword(encoder.encode(resetPassword.getPassword()));
            // save the user with the new password
            userRepository.save(user);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), resetPassword.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getName(),
                    roles));
        }
        // if value is not same then throw runtime exception
        // else save the new password
        // return the jwtResponse
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Old password is not correct."));
    }

}
