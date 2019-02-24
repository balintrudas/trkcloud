package hu.rb.cloud.account.service;

import hu.rb.cloud.account.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Account save(Account account);

    Optional<Account> find(Long id);

    Account findByUsername(String username);

    List<Account> findAll();

    List<Account> findAll(Sort sort);

    Page<Account> findAll(Pageable pageable);

    void delete(Account account);

    void deleteAll();

    long count();

}