package com.dionlan.minhasfinancas;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A partir de out/nov de 2019 a versão 2.2 do Spring Boot implementa o JUnit 5 com as novas anotações
 * @author Dionlan
 * 
 * Susbstituir @RunWith(SpringRunner.class) para @ExtendWith(SpringExtension.class)
 * 
 * Para rodar os testes de integração, retirar o void contextLoads() padrão e deixar o método main.
 * 
 * Caso não rode ir em> botão direito no projeto, run as, java application e selecionar a classe main de testes
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class MinhasfinancasApplicationTests {

		public static void main(String[] args) {
				SpringApplication.run(MinhasfinancasApplicationTests.class, args);
	}
}
