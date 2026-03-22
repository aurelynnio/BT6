package com.example.demo.repository;

import com.example.demo.model.Account;
import com.example.demo.model.AccountRole;
import com.example.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    boolean existsByAccountAndRole(Account account, Role role);
}
