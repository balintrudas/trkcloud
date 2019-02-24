package hu.rb.cloud.account.controller;

import hu.rb.cloud.account.helper.WithScope;
import hu.rb.cloud.account.model.Account;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
public class AccountControllerTest {

    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountController accountController;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    @WithAnonymousUser
    public void shouldFailAccountByUsernameWhenAnonym() throws Exception {
        Account account = new Account();
        account.setUsername("test-controller-username");
        account.setFirstName("test firstname");
        account.setLastName("test lastname");
        entityManager.persist(account);

        thrown.expectCause(is(instanceOf(AccessDeniedException.class)));

        mvc.perform(get("/")
                .param("username", "test-controller-username")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithScope(scope = "server")
    public void shouldReturnAccountByUsernameWhenServer() throws Exception {
        Account account = new Account();
        account.setUsername("test-controller-username");
        account.setFirstName("test firstname");
        account.setLastName("test lastname");
        entityManager.persist(account);
        mvc.perform(get("/")
                .param("username", "test-controller-username")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("test-controller-username")))
                .andExpect(jsonPath("$.firstName", is("test firstname")));
    }

    @Test
    @WithScope(scope = "server")
    public void shouldReturnAccountByIdWhenServer() throws Exception {
        Account account = new Account();
        account.setUsername("test-controller-id");
        account.setFirstName("test firstname");
        account.setLastName("test lastname");
        entityManager.persist(account);
        mvc.perform(get("/")
                .param("id", account.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("test-controller-id")))
                .andExpect(jsonPath("$.firstName", is("test firstname")));
    }

    @Test
    @WithScope(scope = "server")
    public void shouldReturnNullAccountByIdWhenServer() throws Exception {
        mvc.perform(get("/")
                .param("id", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

}
