package com.tean.supergame.service;

import com.tean.supergame.model.entity.UserModel;
import com.tean.supergame.repository.UserRepository;
import com.tean.supergame.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user = userRepository.findByUsername(username);

        return new UserDetailsImpl(user.orElse(null));
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        Optional<UserModel> user = userRepository.findById(id);

        return new UserDetailsImpl(user.orElse(null));
    }
}