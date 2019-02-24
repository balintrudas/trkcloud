package hu.rb.cloud.account.service;

import hu.rb.cloud.account.model.Account;
import hu.rb.cloud.account.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void shouldFindAccountByName() {
        Account account = new Account();
        account.setId(1L);
        account.setUsername("test-service");
        account.setFirstName("test firstname");
        account.setLastName("test lastname");
        when(accountRepository.findByUsername("test-service")).thenReturn(account);
        Account resultAccount = accountService.findByUsername("test-service");
        assertEquals(1, resultAccount.getId().longValue());
        assertEquals("test-service", resultAccount.getUsername());
    }

    @Test
    public void shouldReturnNullAccountByNameWhenNoAccountsExisted() {
        when(accountRepository.findByUsername(anyString())).thenReturn(null);
        Account resultAccount = accountService.findByUsername("test-service");
        assertNull(resultAccount);
    }
}
