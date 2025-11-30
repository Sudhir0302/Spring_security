package com.sudhir003.spring_security.config;

import com.sudhir003.spring_security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(csrf->csrf.disable());  //since this restapi is stateless,so there is no need to maintain session
        http.authorizeHttpRequests(request->request
                .requestMatchers("register","login","verify","enable2FA","disable2FA")
                .permitAll()
                .anyRequest().authenticated());  //here request means all request coming to the backend r
//        http.httpBasic(Customizer.withDefaults());  //user login using alert msg
        http.httpBasic().disable(); //disable default form login

//      if you disable form login, the default /login endpoint is gone,
//      and you need to implement your own login endpoint if you want one.
        http.formLogin().disable();
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        http.authenticationProvider(authenticationProvider());
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService()
//    {
//        UserDetails user= User
//                            .withDefaultPasswordEncoder()
//                            .username("sudhir")
//                            .password("sudhir")
//                            .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);  //since InMemoryUserDetailsManager is an implementation of UserDetailsservice we can return its object
//    }
}
