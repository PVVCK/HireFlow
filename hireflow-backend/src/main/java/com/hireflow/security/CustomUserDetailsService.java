package com.hireflow.security;

import com.hireflow.common.entity.User;
import com.hireflow.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(
            String email
    ) throws UsernameNotFoundException {

        log.debug(
                "Attempting to load user details for email: {}",
                email
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {

                    log.error(
                            "User not found with email: {}",
                            email
                    );

                    return new UsernameNotFoundException(
                            "User not found"
                    );
                });

        log.info(
                "User found successfully: {} with role: {}",
                user.getEmail(),
                user.getRole()
        );

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        )
                )
        );
    }
}