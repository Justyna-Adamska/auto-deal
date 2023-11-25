package com.example.autodeal.config;

import com.example.autodeal.model.AutoDealUserDetails;
import com.example.autodeal.model.UserModel;
import com.example.autodeal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
    @RequiredArgsConstructor
    public class AutoDealUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Optional<UserModel> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
            UserModel user = userOptional.get();
            return new AutoDealUserDetails(user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }
}
