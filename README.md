</head>
<body>

<h1>🌿 API Flor da Cidade</h1>

<p>
  API REST desenvolvida em <strong>Java</strong> utilizando <strong>Spring Boot</strong>,
  com o objetivo de gerenciar informações do projeto
  <strong>Flor da Cidade</strong>, incluindo usuários, pessoas,
  cursos e hortas comunitárias.
</p>

<div class="section">
  <h2>📌 Descrição</h2>
  <p>
    Esta API foi criada como parte de um projeto acadêmico, seguindo boas práticas
    de desenvolvimento backend, arquitetura em camadas e padrão REST.
  </p>
  <p>Principais funcionalidades:</p>
  <ul>
    <li>Gerenciamento de <strong>Usuários</strong></li>
    <li>Cadastro de <strong>Pessoas</strong></li>
    <li>Controle de <strong>Cursos</strong></li>
    <li>Gestão de <strong>Hortas Comunitárias</strong></li>
  </ul>
</div>

<div class="section">
  <h2>🚀 Tecnologias Utilizadas</h2>
  <ul>
    <li>Java 17+</li>
    <li>Spring Boot 3.3.5</li>
    <li>Spring Web</li>
    <li>Spring Data JPA</li>
    <li>MySQL</li>
    <li>Maven</li>
    <li>Swagger</li>
  </ul>
</div>

<div class="section">
  <h2>📦 Pré-requisitos</h2>
  <ul>
    <li>Java JDK 17 ou superior</li>
    <li>Maven 3.8+</li>
    <li>MySQL</li>
    <li>Git</li>
  </ul>
</div>

<div class="section">
  <h2>📥 Instalação</h2>
  <pre><code>git clone https://github.com/XaianeBorges/API.git
cd API
git checkout XaianeBorges/API</code></pre>
</div>

<div class="section">
  <h2>⚙️ Configuração</h2>
  <p>Edite o arquivo <code>application.properties</code>:</p>
  <pre><code>spring.datasource.url=jdbc:mysql://localhost:3306/flor_da_cidade
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8082</code></pre>
</div>

<div class="section">
  <h2>▶️ Executando a Aplicação</h2>
  <pre><code>mvn clean install
mvn spring-boot:run</code></pre>
  <p>A API estará disponível em <code>http://localhost:8082</code></p>
</div>

<div class="section">
  <h2>🔗 Endpoints da API</h2>

  <h3>👥 Usuários</h3>
  <table>
    <tr><th>Método</th><th>Rota</th><th>Descrição</th></tr>
    <tr><td>GET</td><td>/usuarios</td><td>Lista todos os usuários</td></tr>
    <tr><td>GET</td><td>/usuarios/{id}</td><td>Busca usuário por ID</td></tr>
    <tr><td>POST</td><td>/usuarios</td><td>Cadastra novo usuário</td></tr>
    <tr><td>PUT</td><td>/usuarios/{id}</td><td>Atualiza usuário</td></tr>
    <tr><td>DELETE</td><td>/usuarios/{id}</td><td>Remove usuário</td></tr>
  </table>

  <h3>🧍 Pessoas</h3>
  <table>
    <tr><th>Método</th><th>Rota</th><th>Descrição</th></tr>
    <tr><td>GET</td><td>/pessoas</td><td>Lista pessoas</td></tr>
    <tr><td>GET</td><td>/pessoas/{id}</td><td>Busca pessoa por ID</td></tr>
    <tr><td>POST</td><td>/pessoas</td><td>Cadastra pessoa</td></tr>
    <tr><td>PUT</td><td>/pessoas/{id}</td><td>Atualiza pessoa</td></tr>
    <tr><td>DELETE</td><td>/pessoas/{id}</td><td>Remove pessoa</td></tr>
  </table>

  <h3>🎓 Cursos</h3>
  <table>
    <tr><th>Método</th><th>Rota</th><th>Descrição</th></tr>
    <tr><td>GET</td><td>/cursos</td><td>Lista cursos</td></tr>
    <tr><td>GET</td><td>/cursos/{id}</td><td>Busca curso por ID</td></tr>
    <tr><td>POST</td><td>/cursos</td><td>Cadastra curso</td></tr>
    <tr><td>PUT</td><td>/cursos/{id}</td><td>Atualiza curso</td></tr>
    <tr><td>DELETE</td><td>/cursos/{id}</td><td>Remove curso</td></tr>
  </table>

  <h3>🌱 Hortas</h3>
  <table>
    <tr><th>Método</th><th>Rota</th><th>Descrição</th></tr>
    <tr><td>GET</td><td>/hortas</td><td>Lista hortas</td></tr>
    <tr><td>GET</td><td>/hortas/{id}</td><td>Busca horta por ID</td></tr>
    <tr><td>POST</td><td>/hortas</td><td>Cadastra horta</td></tr>
    <tr><td>PUT</td><td>/hortas/{id}</td><td>Atualiza horta</td></tr>
    <tr><td>DELETE</td><td>/hortas/{id}</td><td>Remove horta</td></tr>
  </table>
</div>

<div class="section">
  <h2>📖 Documentação Swagger</h2>
  <p>
    A documentação interativa
    pode ser acessada em:
  </p>
  <pre><code>http://localhost:8082/swagger-ui.html</code></pre>
</div>

<div class="section">
  <h2>📁 Estrutura do Projeto</h2>
  <pre><code>src/
 └── main/
     ├── java/
     │   └── com/flordacidade/api
     │       ├── controller
     │       ├── service
     │       ├── repository
     │       └── model
     └── resources/
         └── application.properties</code></pre>
</div>

<footer>
  <p>
    📜 Licença Apache 2.0<br>
    👩‍💻 Desenvolvido por <strong>Xaiane Borges</strong><br>
    Projeto acadêmico – Engenharia da Computação
  </p>
</footer>

</body>
</html>
