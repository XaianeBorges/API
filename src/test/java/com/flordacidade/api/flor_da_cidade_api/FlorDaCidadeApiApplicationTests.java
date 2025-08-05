package com.flordacidade.api.flor_da_cidade_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Classe de teste principal que verifica se o contexto da aplicação Spring
 * pode ser carregado com sucesso.
 *
 * A anotação @SpringBootTest(classes = ...) é crucial para dizer ao Spring qual
 * é a
 * classe de configuração principal da sua aplicação, resolvendo o erro "Unable
 * to find a @SpringBootConfiguration".
 *
 * A anotação @ActiveProfiles("test") instrui o Spring a usar o arquivo de
 * configuração 'application-test.properties' durante a execução dos testes.
 */
// A CORREÇÃO PRINCIPAL ESTÁ NESTA LINHA:
@SpringBootTest(classes = FlorDaCidadeApiApplication.class)
@ActiveProfiles("test") // Garante que o arquivo 'application-test.properties' seja lido
class FlorDaCidadeApiApplicationTests {

	/**
	 * Este teste simples apenas tenta carregar o contexto da aplicação.
	 * Se este teste passar, significa que todas as suas configurações,
	 * injeções de dependência e beans estão corretos e a aplicação pode iniciar.
	 */
	@Test
	void contextLoads() {
		// O corpo do método pode ficar vazio. O sucesso está no carregamento do
		// contexto.
	}

}