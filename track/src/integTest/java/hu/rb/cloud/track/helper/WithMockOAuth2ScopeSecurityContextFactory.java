package hu.rb.cloud.track.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashSet;
import java.util.Set;

public class WithMockOAuth2ScopeSecurityContextFactory implements WithSecurityContextFactory<WithScope> {

    @Override
    public SecurityContext createSecurityContext(WithScope withScope) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Set<String> scope = new HashSet<>();
        scope.add(withScope.scope());
        OAuth2Request request = new OAuth2Request(null, null, null, true, scope, null, null, null, null);
        Authentication auth = new OAuth2Authentication(request, null);
        context.setAuthentication(auth);
        return context;
    }
}