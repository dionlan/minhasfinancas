package com.dionlan.minhasfinancas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@SpringBootApplication
public class MinhasfinancasApplication implements WebMvcConfigurer {
	

	/**
	 * addCorsMappings adicionado para não dar o erro de CORS na página web.
	 * Obs.: Quando for realizar os teste de integração, deixar apenas o método main.
	 */
	/**
	 * para gerar o build do projeto, comenta-se o addCorsMappings e habilita WebConfiguration.
	 */
	/*
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// TODO Auto-generated method stub
		registry.addMapping("/**").allowedMethods("GET","POST", "PUT", "DELETE", "OPTIONS", "PATCH");
	}
	*/
	
	public static void main(String[] args) {
		SpringApplication.run(MinhasfinancasApplication.class, args);
	}

}
