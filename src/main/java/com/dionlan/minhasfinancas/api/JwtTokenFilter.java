package com.dionlan.minhasfinancas.api;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.dionlan.minhasfinancas.domain.service.JwtService;
import com.dionlan.minhasfinancas.domain.service.impl.SecurityUserDetailsService;

public class JwtTokenFilter extends OncePerRequestFilter{

	@Autowired
	private JwtService jwtService;
	 
	@Autowired
	private SecurityUserDetailsService userDetailsService;
	
	public JwtTokenFilter(JwtService jwtService, SecurityUserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorization = request.getHeader("Authorization");
		
		if(authorization != null && authorization.startsWith("Bearer")) {
			String token = authorization.split(" ")[1];
			boolean isTokenValid = jwtService.isTokenValido(token);
			
			if(isTokenValid) {
				String login = jwtService.obterLoginUsuario(token);
				UserDetails ausuarioAutenticado = userDetailsService.loadUserByUsername(login);
				UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
																ausuarioAutenticado, null, ausuarioAutenticado.getAuthorities());
				user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(user);
			}
		}
		filterChain.doFilter(request, response);
	}
}
