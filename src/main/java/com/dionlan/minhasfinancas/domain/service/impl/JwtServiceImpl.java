package com.dionlan.minhasfinancas.domain.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.dionlan.minhasfinancas.domain.entity.dto.UsuarioDTO;
import com.dionlan.minhasfinancas.domain.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService{

	@Value("${jwt.expiracao}")
	private String expiracao;
	
	@Value("${jwt.chave-assinatura}")
	private String chaveAssinatura;
	
	SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	public static final String SECRET = Base64Utils.encodeToString("byBjYWbDqSDDqSBwcmV0bw==".getBytes());
	
	

	@Override
	public String gerarToken(UsuarioDTO usuario) {
		long expLong = Long.valueOf(expiracao);
		LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expLong);
		Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
		Date data = Date.from(instant);
		
		String horaExpiracaoToken = dataHoraExpiracao.toLocalTime()
				.format(DateTimeFormatter.ofPattern(("HH:mm")));
		//SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		
		String token = Jwts
					.builder()
					.setExpiration(data)
					.setSubject(usuario.getEmail())
					.claim("userId", usuario.getUserId())
					.claim("nome", usuario.getNome())
					.claim("horaExpiracao", horaExpiracaoToken)
					.signWith(key)
					.compact();
		return token;
	}

	@Override
	public Claims obterClaims(String token) throws ExpiredJwtException {
		//Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		return Jwts
				.parser()
				.setSigningKey(key)
				.parseClaimsJws(token)
				.getBody();
	}

	@Override
	public boolean isTokenValido(String token) {
		try {
			Claims claims = obterClaims(token);
			Date dataExp = claims.getExpiration();
			LocalDateTime dataExpiracao = dataExp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			
			boolean dataHoraAtualIsAfterDataExpiracao = LocalDateTime.now().isAfter(dataExpiracao);
			
			return !dataHoraAtualIsAfterDataExpiracao;
			
		}catch(ExpiredJwtException e) {
			return false;
		}
	}

	@Override
	public String obterLoginUsuario(String token) {
		Claims claims = obterClaims(token);
		return claims.getSubject();
	}
}
