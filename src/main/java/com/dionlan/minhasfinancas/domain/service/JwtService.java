package com.dionlan.minhasfinancas.domain.service;

import com.dionlan.minhasfinancas.domain.entity.dto.UsuarioDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JwtService {
	
	String gerarToken(UsuarioDTO usuario);
	//Claims obterClaims(String token) throws ExpiredJwtException; //payload 
	Jws<Claims> parseJwt(String jwtString);
	boolean isTokenValido(String token);
	String obterLoginUsuario(String token);

}
