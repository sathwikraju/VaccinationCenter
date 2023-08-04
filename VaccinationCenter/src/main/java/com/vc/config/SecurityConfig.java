package com.vc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.vc.handler.*;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Autowired
	private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				requests -> requests.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						.antMatchers("/dashboard").authenticated() // Restrict access to /dashboard
						.anyRequest().permitAll())
				.formLogin(formLogin -> formLogin.loginPage("/login.html") // Customize the login page URL
						.loginProcessingUrl("/login") // URL to submit the login form
						.successHandler(customAuthenticationSuccessHandler) // Custom authentication success handler
						.failureHandler(customAuthenticationFailureHandler) // Custom authentication failure handler
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout") // URL to submit the logout request
						.logoutSuccessHandler(customLogoutSuccessHandler) // Custom logout success handler
						.invalidateHttpSession(true).deleteCookies("JSESSIONID"))
				.csrf().disable(); // Disable CSRF for simplicity (enable it for production)
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
