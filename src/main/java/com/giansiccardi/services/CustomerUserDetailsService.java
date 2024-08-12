package com.giansiccardi.services;

import com.giansiccardi.models.Customer;
import com.giansiccardi.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer user= customerRepository.findByEmail(username);

        if(user==null){
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        List<GrantedAuthority>authorityList= new ArrayList<>();

        User userDetails= new User(user.getEmail(),user.getPassword(),authorityList);

        return userDetails;
    }
}
