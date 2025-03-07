package dayp308.chatroom.configuration;

import dayp308.chatroom.security.*;
import dayp308.chatroom.repository.RedisPersistentTokenRepository;
import dayp308.chatroom.service.RememberServices;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = false)
public class SecurityConfiguration {
    @Autowired
    private final UserDetailsPasswordServiceImpl userDetailsPasswordService;
    @Autowired
    private final UserDetailsServiceImpl userDetailsService;
    @Autowired
    private RedisPersistentTokenRepository redisPersistentTokenRepository;

    @Autowired
    public SecurityConfiguration(UserDetailsPasswordServiceImpl userDetailsPasswordService, UserDetailsServiceImpl userDetailsService) {
        this.userDetailsPasswordService = userDetailsPasswordService;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(formLogin -> formLogin
                        .usernameParameter("loginName")
                        .passwordParameter("password")
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/register").permitAll()
                        .requestMatchers("/api/no_auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling( e -> {
                    e.accessDeniedPage(null)
                            .authenticationEntryPoint(authenticationEntryPoint());
                })
                .rememberMe(remember -> remember
                        .rememberMeServices(rememberMeServices(userDetailsService, redisPersistentTokenRepository))
                        .userDetailsService(userDetailsService)
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(new BCryptPasswordEncoder(8));
        provider.setUserDetailsService(userDetailsService);
        provider.setUserDetailsPasswordService(userDetailsPasswordService);

        ProviderManager providerManager = new ProviderManager(provider);

        return providerManager;
    }

    @Bean
    public Advisor preAuthorize(CustomAuthorizationManager manager) {
        return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(manager);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public UserDetailsPasswordService userDetailsPasswordService() {
        return userDetailsPasswordService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.eraseCredentials(false);
    }

    @Autowired
    public AbstractRememberMeServices rememberMeServices(UserDetailsServiceImpl userDetailsService, RedisPersistentTokenRepository tokenRepository) {
        return new RememberServices(RememberServices.DEFAULT_PARAMETER, userDetailsService, tokenRepository);
    }

    @Autowired
    public void setRedisPersistentTokenRepository(RedisPersistentTokenRepository redisPersistentTokenRepository) {
        this.redisPersistentTokenRepository = redisPersistentTokenRepository;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new FormLoginAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new LoginFailedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Access to Login page");
        return entryPoint;
    }
}
