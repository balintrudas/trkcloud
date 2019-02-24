package hu.rb.cloud.account.controller;

import hu.rb.cloud.account.model.Account;
import hu.rb.cloud.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    @PreAuthorize("#oauth2.hasScope('server')")
    public Account getAccount(@RequestParam(value = "username", required = false) String username, @RequestParam(value = "id", required = false) Long id) {
        if(username!=null){
            return accountService.findByUsername(username);
        }
        return accountService.find(id).orElse(null);
    }

}
