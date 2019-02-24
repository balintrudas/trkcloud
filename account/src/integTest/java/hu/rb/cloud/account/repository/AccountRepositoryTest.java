package hu.rb.cloud.account.repository;

import hu.rb.cloud.account.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest()
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void shouldFindByName() {
        Account account = new Account();
        account.setUsername("test");
        account.setFirstName("test firstname");
        account.setLastName("test lastname");
        entityManager.persist(account);
        Account resultAccount = accountRepository.findByUsername("test");
        assertEquals(account.getId().longValue(), resultAccount.getId().longValue());
        assertEquals("test", resultAccount.getUsername());
    }

}
