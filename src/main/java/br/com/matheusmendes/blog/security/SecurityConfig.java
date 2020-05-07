package br.com.matheusmendes.blog.security;

import br.com.matheusmendes.blog.service.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioServiceImpl service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/postagens", "/", "/buscarPostPorTitulo", "/detalhes/**", "/registrar", "/cadastre-se", "/comentar", "/logar")
                .permitAll()
                .antMatchers("/css/**", "/js/**", "/imagens/**")
                .permitAll()

                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/logar")
                    .defaultSuccessUrl("/dashboard")
                    .failureUrl("/login-error").permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/logar");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
    }
}
