package com.kelwin.security.springsecurityauthserver.service;

import com.kelwin.security.springsecurityauthserver.model.MyUserDetails;
import com.kelwin.security.springsecurityauthserver.model.Users;
import com.kelwin.security.springsecurityauthserver.model.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Users> usersOptional = usersRepository.findByUsername(s);
        if (usersOptional == null || !usersOptional.isPresent()) {
            throw new UsernameNotFoundException("User "  + s + " not found");
        }

        Users u = usersOptional.get();
        return  new MyUserDetails(u.getUsername(), u.getPassword(), u.getEnabled());
    }
}
