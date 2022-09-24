package br.com.fabioestevam.sgm.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfig {
	
	@Autowired
    private LoggingAccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	UserDetailServiveImpl userDetailServiveImpl;
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		  http
          .authorizeRequests()
		  .antMatchers(
                  "/",
                  "/js/**",
                  "/css/**",
                  "/img/**",
                  "/webjars/**",
                  "/recovery/**",
                  "/reset/**",
                  "/usuario/cadastro/**").permitAll()
          .anyRequest().authenticated()
      .and()
      .formLogin()
          .loginPage("/login")
          .permitAll()
      .and()
      .logout()
          .invalidateHttpSession(true)
          .clearAuthentication(true)
          .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
          .logoutSuccessUrl("/login?logout")
          .permitAll()
      .and()
      .exceptionHandling()
          .accessDeniedHandler(accessDeniedHandler);
		  
		  return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
