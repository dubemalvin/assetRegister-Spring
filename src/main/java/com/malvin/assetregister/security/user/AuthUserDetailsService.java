package com.malvin.assetregister.security.user;

import com.malvin.assetregister.entity.User;
import com.malvin.assetregister.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            User user =  Optional.ofNullable(userRepository.findByEmail(email))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new AuthUserDetails(user);
        }

}
