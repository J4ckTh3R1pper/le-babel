package dayp308.lebabel.configuration;

import dayp308.lebabel.repository.RedisCaptchaRepository;
import dayp308.lebabel.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserDetailsPasswordServiceImpl userDetailsPasswordService;

    private final UserDetailsServiceImpl userDetailsService;

    private final RedisCaptchaRepository redisCaptchaRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

    @Autowired
    public SecurityConfiguration(UserDetailsPasswordServiceImpl userDetailsPasswordService, UserDetailsServiceImpl userDetailsService, RedisCaptchaRepository redisCaptchaRepository) {
        this.userDetailsPasswordService = userDetailsPasswordService;
        this.userDetailsService = userDetailsService;
        this.redisCaptchaRepository = redisCaptchaRepository;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring().requestMatchers("/api/images/**", "/error", "/upload/**", "/docs.html", "/webjars/**");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(formLogin -> formLogin
                        .usernameParameter("loginName")
                        .passwordParameter("password")
                        .loginProcessingUrl("/api/login")
                        .permitAll()
                )
                .authorizeHttpRequests(auth -> auth
                        // .requestMatchers("/api/images/**").permitAll()
                        .requestMatchers("/api/no_auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling( e -> {
                    e.disable();
                    // e.accessDeniedPage(null)
                    //         .authenticationEntryPoint(authenticationEntryPoint());
                })
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterAt(captchaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        provider.setUserDetailsPasswordService(userDetailsPasswordService);

        ProviderManager providerManager = new ProviderManager(provider);

        return providerManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.eraseCredentials(false);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Access to Login page or include correct token in request");
        return entryPoint;
    }

    @Bean
    public OncePerRequestFilter jwtAuthenticationFilter() {
        return new JwtAuthFilter(userDetailsService);
    }

    @Bean
    public CaptchaUsernamePasswordAuthenticationFilter captchaAuthenticationFilter() {
        CaptchaUsernamePasswordAuthenticationFilter filter =
                new CaptchaUsernamePasswordAuthenticationFilter(redisCaptchaRepository, authenticationManager());
        filter.setUsernameParameter("loginName");
        filter.setPasswordParameter("password");
        filter.setFilterProcessesUrl("/api/login");
        return filter;
    }
}
