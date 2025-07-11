package br.com.iftm.monitoria.config;


import br.com.iftm.monitoria.model.UsuarioLogado;
import br.com.iftm.monitoria.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .requiresChannel(channel -> channel
                        .anyRequest().requiresSecure()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/index", "/usuarios/cadastrar", "/css/**", "/js/**", "/images/**", "/swagger-ui").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
                        .requestMatchers("/usuarios/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers("/monitorias/**").hasAnyRole("ADMIN", "PROFESSOR", "MONITOR")
                        .requestMatchers("/disciplinas/**").hasAnyRole("ADMIN", "PROFESSOR", "MONITOR")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/perfil/**").authenticated()
                        .requestMatchers("/relatorios/**").hasAnyRole("ADMIN", "PROFESSOR", "MONITOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.ALWAYS)
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository repository) {
        return username -> repository.findByEmail(username)
                .map(UsuarioLogado::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
