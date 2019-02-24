package hu.rb.cloud.account.controller;

import hu.rb.cloud.account.model.Account;
import hu.rb.cloud.account.service.AccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountServiceImpl accountService;

    @Test
    public void shouldReturnAccountByUsername(){
        Account account = new Account();
        account.setId(1L);
        account.setUsername("test");
        when(accountService.findByUsername("test")).thenReturn(account);
        Account resultAccount = accountController.getAccount("test",null);
        assertEquals(1, resultAccount.getId().longValue());
        assertEquals("test", resultAccount.getUsername());
    }

    @Test
    public void shouldReturnNullAccountByUsernameWhenNoAccountsExisted(){
        when(accountService.findByUsername(anyString())).thenReturn(null);
        Account resultAccount = accountController.getAccount("test",null);
        assertNull(resultAccount);
    }

    @Test
    public void shouldReturnAccountById(){
        Account account = new Account();
        account.setId(1L);
        account.setUsername("test");
        when(accountService.find(1L)).thenReturn(Optional.of(account));
        Account resultAccount = accountController.getAccount(null,1L);
        assertEquals(1, resultAccount.getId().longValue());
        assertEquals("test", resultAccount.getUsername());
    }

    @Test
    public void shouldReturnNullAccountByIdWithNull(){
        when(accountService.find(null)).thenReturn(Optional.empty());
        Account resultAccount = accountController.getAccount(null,null);
        assertNull(resultAccount);
    }

    @Test
    public void shouldReturnNullAccountByIdWhenNoAccountsExisted(){
        when(accountService.find(anyLong())).thenReturn(Optional.empty());
        Account resultAccount = accountController.getAccount(null,1L);
        assertNull(resultAccount);
    }

}
