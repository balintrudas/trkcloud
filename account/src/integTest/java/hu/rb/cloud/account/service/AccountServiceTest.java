package hu.rb.cloud.account.service;

import hu.rb.cloud.account.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldReturnAccountByUsername(){
        Account account = new Account();
        account.setUsername("test-service-username");
        account.setFirstName("test firstname");
        account.setLastName("test lastname");
        entityManager.persist(account);
        Account resultAccount = accountService.findByUsername("test-service-username");
        assertEquals("test-service-username", resultAccount.getUsername());
    }

    @Test
    public void shouldReturnNullAccountByUsername(){
        Account account = new Account();
        account.setUsername("test-service-username-other");
        account.setFirstName("test firstname");
        account.setLastName("test lastname");
        entityManager.persist(account);
        Account resultAccount = accountService.findByUsername("test-service-username");
        assertNull(resultAccount);
    }

    @Test
    public void shouldReturnAccountById(){
        Account account = new Account();
        account.setUsername("test-service-id");
        account.setFirstName("test firstname");
        account.setLastName("test lastname");
        entityManager.persist(account);
        Optional<Account> resultAccount = accountService.find(account.getId());
        assertEquals("test-service-id", resultAccount.get().getUsername());
    }

    @Test
    public void shouldReturnOpNullAccountById(){
        Account account = new Account();
        account.setUsername("test-service-id-other");
        account.setFirstName("test firstname");
        account.setLastName("test lastname");
        entityManager.persist(account);
        Optional<Account> resultAccount = accountService.find(account.getId()+1);
        assertFalse(resultAccount.isPresent());
    }
}
