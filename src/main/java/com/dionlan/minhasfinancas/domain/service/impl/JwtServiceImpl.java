package com.dionlan.minhasfinancas.domain.service.impl;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.dionlan.minhasfinancas.domain.entity.dto.UsuarioDTO;
import com.dionlan.minhasfinancas.domain.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;

@Service
public class JwtServiceImpl implements JwtService{

	@Value("${jwt.expiracao}")
	private String expiracao;
	
	@Value("${jwt.chave-assinatura}")
	private static String chaveAssinatura;
	
	String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

	@Override
	public String gerarToken(UsuarioDTO usuario) {
		long expLong = Long.valueOf(expiracao);
		LocalDateTime now = LocalDateTime.now();
		Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
		Date dateHourNow = Date.from(instant);
		
		Date dateHourExpiration = Date.from(instant.plus(expLong, ChronoUnit.MINUTES));

		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
		
		Map<String, Object> claim = new HashMap<>();
        claim.put("alg", "HS256");
        claim.put("typ", "JWT");
	        
		String token = null;
		try {
			token = Jwts
						.builder()
						.signWith(hmacKey)
						.setClaims(claim)
						.setSubject(usuario.getEmail())
						.setId(UUID.randomUUID().toString())
						.setIssuedAt(dateHourNow)
						.setExpiration(dateHourExpiration)
						.claim("userId", usuario.getUserId())
						.claim("nome", usuario.getNome())
						.compact();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return token;	
	}
	
	@Override
	public Jws<Claims> parseJwt(String jwtString) {
	    Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
	                                    SignatureAlgorithm.HS256.getJcaName());

	    Jws<Claims> jwt = Jwts.parserBuilder()
	            .setSigningKey(hmacKey)
	            .build()
	            .parseClaimsJws(jwtString);

	    return jwt;
	}

	@Override
	public boolean isTokenValido(String token) {
		try {
			
			Jws<Claims> claims = parseJwt(token);
			Date dataExp = claims.getBody().getExpiration();
			LocalDateTime dataExpiracao = dataExp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			boolean dataHoraAtualIsAfterDataExpiracao = LocalDateTime.now().isAfter(dataExpiracao);
			
			return !dataHoraAtualIsAfterDataExpiracao;
			
		}catch(ExpiredJwtException e) {
			return false;
		}
	}

	@Override
	public String obterLoginUsuario(String token) {
		Jws<Claims> claims = parseJwt(token);
		Claims bodyClaims = claims.getBody();
		return bodyClaims.getSubject();
	}
}
