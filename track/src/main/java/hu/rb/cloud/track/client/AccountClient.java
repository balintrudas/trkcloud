package hu.rb.cloud.track.client;

import hu.rb.cloud.track.model.dto.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping(value = "/")
    Account getAccount(@RequestParam(value = "username", required = false) String username, @RequestParam(value = "id", required = false) Long id);

}