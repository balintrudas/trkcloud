package hu.rb.cloud.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@Order(1)
public class ActuatorSecurityConfig {

//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        return new InMemoryUserDetailsManager(
//                User.withDefaultPasswordEncoder().username("actuator").password("password")
//                        .authorities("ROLE_ACTUATOR", "ROLE_USER").build());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.antMatcher("/actuator/**")
//                .httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/actuator/**").hasRole("ACTUATOR");
//    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("actuator")
                .password("password")
                .roles("ACTUATOR")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.securityMatcher(ServerWebExchangeMatchers.pathMatchers("/actuator/**"))
                .httpBasic()
                .and()
                .authorizeExchange()
                .pathMatchers("/actuator/**")
                .hasRole("ACTUATOR");

        http.authorizeExchange().pathMatchers("/**").permitAll().and().csrf().disable()
                .logout().disable()
                .formLogin().disable().headers().frameOptions().disable();

        return http.build();

    }

}