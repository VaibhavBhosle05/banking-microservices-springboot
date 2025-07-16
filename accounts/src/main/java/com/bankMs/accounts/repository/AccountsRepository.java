package com.eazyBytes.accounts.repository;

import com.eazyBytes.accounts.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    Accounts findByCustomerId(Long customerId);

    void deleteByCustomerId(Long customerId);
}
