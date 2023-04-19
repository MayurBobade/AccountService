package com.mayurbobde.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mayurbobde.model.Account;


@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {

    Page<Account> findByBalanceGreaterThan(double accountBalance, Pageable page);

    Page<Account> findByBalanceBetween(double accountBalanceGT, double accountBalanceLt, Pageable page);

}