package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select a from Account a left join fetch a.accountRoles ar left join fetch ar.role where a.loginName = :loginName")
    Optional<Account> findByLoginNameWithRoles(@Param("loginName") String loginName);

    Optional<Account> findByLoginName(String loginName);
}
