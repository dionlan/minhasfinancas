package com.dionlan.minhasfinancas.domain.service;

import com.dionlan.minhasfinancas.domain.entity.dto.UsuarioDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {
	
	String gerarToken(UsuarioDTO usuario);
	Claims obterClaims(String token) throws ExpiredJwtException; //payload 
	boolean isTokenValido(String token);
	String obterLoginUsuario(String token);

}
