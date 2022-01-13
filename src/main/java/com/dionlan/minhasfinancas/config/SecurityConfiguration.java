package com.dionlan.minhasfinancas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dionlan.minhasfinancas.api.JwtTokenFilter;
import com.dionlan.minhasfinancas.domain.service.JwtService;
import com.dionlan.minhasfinancas.domain.service.impl.SecurityUserDetailsService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private SecurityUserDetailsService userDetailsService;
	
	@Autowired
	private JwtService jwtService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter(jwtService, userDetailsService);
	}
	
	/**
	 * Deinição de permissões para executar determinado endpoint hasRole("ADMIN")
	 * Role (grupo de usuários): PERFIL(admin, rh, contabilidade, usuario final, planejador)
	 * hasAuthority("CADASTRAR_USUARIOS) (PERMISSÕES de cada perfil) 
	 * CADA perfil tem suas authorities específicas. Ex.: ADMIN pode tudo
	 * 
	 * disable().authorizeRequests()
			.antMatchers(HttpMethod.POST, "/api/usuarios/autenticar").permitAll()
			Não exige autenticação para esse método em específico, e para todos os outros exige
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/usuarios/autenticar").permitAll()
				.antMatchers(HttpMethod.POST, "/api/usuarios/salvar").permitAll()
				.anyRequest().authenticated()
		.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
