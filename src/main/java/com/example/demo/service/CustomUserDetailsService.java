package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginNameWithRoles(username)
            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));

        Set<GrantedAuthority> authorities = account.getAccountRoles().stream()
            .filter(accountRole -> accountRole.isEnabled())
            .map(accountRole -> new SimpleGrantedAuthority(accountRole.getRole().getName()))
            .collect(Collectors.toSet());

        return User.withUsername(account.getLoginName())
            .password(account.getPassword())
            .authorities(authorities)
            .disabled(!account.isEnabled())
            .build();
    }
}
