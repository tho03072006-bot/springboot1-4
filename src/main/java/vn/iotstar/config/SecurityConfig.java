package vn.iotstar.config;
import vn.iotstar.repository.UserRepository;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authorization.AuthorizationDecision;
import jakarta.servlet.DispatcherType;
import vn.iotstar.entity.AppUser.Role;
@Configuration
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean UserDetailsService userDetailsService(UserRepository users) {
        return username -> users.findByUsernameIgnoreCase(username)
            .map(u -> User.withUsername(u.getUsername()).password(u.getPasswordHash()).roles(u.getRole().name()).disabled(!u.isEnabled()).build())
            .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại."));
    }
    @Bean SecurityFilterChain security(HttpSecurity http,UserRepository users) throws Exception {
        return http.authorizeHttpRequests(a -> a
            .dispatcherTypeMatchers(DispatcherType.FORWARD,DispatcherType.INCLUDE,DispatcherType.ERROR).permitAll()
            .requestMatchers("/login","/assets/**","/error").permitAll()
            .requestMatchers("/admin/**").access((auth,ctx) -> new AuthorizationDecision(users.findByUsernameIgnoreCase(auth.get().getName()).map(u -> u.isEnabled() && u.getRole()==Role.ADMIN).orElse(false)))
            .anyRequest().authenticated())
            .formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/",true).failureUrl("/login?error"))
            .logout(l -> l.logoutSuccessUrl("/login?logout"))
            .exceptionHandling(e -> e.accessDeniedPage("/denied"))
            .build();
    }
}
