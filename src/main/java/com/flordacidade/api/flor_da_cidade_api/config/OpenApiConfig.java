package com.flordacidade.api.flor_da_cidade_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Projeto Flor da Cidade", version = "10.0", description = "Documentação da API para o sistema de gerenciamento de hortas e cursos da SEAU.", contact = @Contact(name = "Equipe Dev Nassau", email = "testapi.projeto@gmail.com"), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
public class OpenApiConfig {

}
