package antifraud.config.security;

import antifraud.model.enumerator.Role;
import antifraud.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserManagementService userManagementService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .mvcMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/api/auth/user/*").hasRole(Role.ADMINISTRATOR.name())
                .mvcMatchers(HttpMethod.GET, "/api/auth/list").hasAnyRole(Role.ADMINISTRATOR.name(), Role.SUPPORT.name())
                .mvcMatchers(HttpMethod.PUT, "/api/auth/role").hasRole(Role.ADMINISTRATOR.name())
                .mvcMatchers(HttpMethod.PUT, "/api/auth/access").hasRole(Role.ADMINISTRATOR.name())

                .mvcMatchers(HttpMethod.POST, "/api/antifraud/suspicious-ip").hasRole(Role.SUPPORT.name())
                .mvcMatchers(HttpMethod.POST, "/api/antifraud/stolencard").hasRole(Role.SUPPORT.name())
                .mvcMatchers(HttpMethod.DELETE, "/api/antifraud/suspicious-ip/*").hasRole(Role.SUPPORT.name())
                .mvcMatchers(HttpMethod.DELETE, "/api/antifraud/stolencard/*").hasRole(Role.SUPPORT.name())
                .mvcMatchers(HttpMethod.GET, "/api/antifraud/suspicious-ip").hasRole(Role.SUPPORT.name())
                .mvcMatchers(HttpMethod.GET, "/api/antifraud/stolencard").hasRole(Role.SUPPORT.name())
                .mvcMatchers(HttpMethod.GET, "/api/antifraud/history").hasRole(Role.SUPPORT.name())
                .mvcMatchers(HttpMethod.GET, "/api/antifraud/history/*").hasRole(Role.SUPPORT.name())
                .mvcMatchers(HttpMethod.PUT, "/api/antifraud/transaction").hasRole(Role.SUPPORT.name())

                .mvcMatchers(HttpMethod.POST, "/api/antifraud/transaction").hasRole(Role.MERCHANT.name())

                .mvcMatchers("/actuator/shutdown").permitAll() // needs to run test
                // other matchers
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userManagementService).passwordEncoder(passwordEncoder)
                .and()
                .jdbcAuthentication().getUserDetailsService();
    }

}
