package com.travelagency.destinations.services.user;

import com.travelagency.destinations.dtos.user.UserRequestDTO;
import com.travelagency.destinations.entities.user.UserEntity;
import com.travelagency.destinations.repositories.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole())
                .build();
    }

    @Transactional
    public void registerUser(UserRequestDTO requestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(requestDTO.getUsername());
        userEntity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        userEntity.setRole(requestDTO.getRole());
        userRepository.save(userEntity);
    }
}
