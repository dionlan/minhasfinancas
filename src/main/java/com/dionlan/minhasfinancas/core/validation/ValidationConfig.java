package com.dionlan.minhasfinancas.core.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Recomendação de uso: resources bundle do Spring (messeages.properties) no lugar dos arquivos de properties do BeanValidation da especificação do hibernate
 * classe para configurar, unificar/integrar da classe BeanValidation do Spring. Integra o ValidationMessages.properties da depedencia 
 * do hibernate-validator com o arquivo messages.properties dentro dos resourcers (para não ter redundância entre as configurações)
 * @author Dionlan
 *
 */
@Configuration
public class ValidationConfig {

	@Bean
	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}
}
