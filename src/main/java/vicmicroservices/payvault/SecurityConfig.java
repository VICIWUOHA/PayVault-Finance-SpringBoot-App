package vicmicroservices.payvault;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration // used to tell Spring to use this class to configure Spring & Spring Boot
class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        for every request to our base url, we want to apply some security config settings to ensure that
//        a user is authenticated.
        http.authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/paycards/**")
                        .hasRole("PAYCARD-OWNER"))
                        .csrf(csrf -> csrf.disable())
                        .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    UserDetailsService testUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder();
        UserDetails victor = users
                .username("VictorI")
                .password(passwordEncoder.encode("123abcxyz"))
                .roles("PAYCARD-OWNER") // Owner Role
                .build();
        UserDetails ben = users
                .username("BigBen")
                .password(passwordEncoder.encode("big@ben10"))
                .roles("PAYCARD-OWNER")
                .build();
        UserDetails stanleyNoCards = users
                .username("stanley-owns-no-cards")
                .password(passwordEncoder.encode("qrs456"))
                .roles("NON-OWNER") // new role
                .build();
        return new InMemoryUserDetailsManager(victor,stanleyNoCards,ben);
    }
}
