package com.icaro.taskmanager.security;

import com.icaro.taskmanager.model.UserEntity;
import com.icaro.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         UserEntity user = userRepository.findByEmail(username)
                 .orElseThrow(() -> new UsernameNotFoundException("The user is not in the database."));

         return User.builder()
                 .username(user.getEmail())
                 .password(user.getPassword())
                 .roles(user.getRole().name().replace("ROLE_", ""))
                 .build();
    }
}
