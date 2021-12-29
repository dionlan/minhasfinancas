package com.dionlan.minhasfinancas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * A partir de out/nov de 2019 a versão 2.2 do Spring Boot implementa o JUnit 5 com as novas anotações
 * @author Dionlan
 * 
 * Susbstituir @RunWith(SpringRunner.class) para @ExtendWith(SpringExtension.class)
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class MinhasfinancasApplicationTests {

	@Test
	void contextLoads() {
	}

}
