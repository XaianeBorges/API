</head>
<body>

  <h1>🌿 API Flor da Cidade</h1>
  <p>API REST construída em <strong>Java</strong> com <strong>Spring Boot</strong> para o projeto <em>Flor da Cidade</em>.</p>

  <div class="section">
    <h2>📌 Sobre</h2>
    <p>Esta API expõe endpoints para gerenciar recursos relacionados ao sistema de hortas comunitárias e cursos do projeto Flor da Cidade, utilizando o framework Spring Boot.</p>
    <p>O código fonte fica disponível nesta branch: <strong>XaianeBorges/API</strong> no GitHub. https://github.com/XaianeBorges/API/new/XaianeBorges/API </p>
  </div>

  <div class="section">
    <h2>🚀 Tecnologias Utilizadas</h2>
    <ul>
      <li>Java 17+</li>
      <li>Spring Boot (Web, Data JPA)</li>
      <li>MySQL</li>
      <li>Maven</li>
    </ul>
  </div>

  <div class="section">
    <h2>📥 Pré-requisitos</h2>
    <ul>
      <li>Java JDK 17 </li>
      <li>Spring boot 3.3.5 </li>
      <li>Maven 3.8</li>
      <li>Banco de dados (ex.: MySQL)</li>
      <li>Git</li>
    </ul>
  </div>

  <div class="section">
    <h2>📦 Instalação</h2>
    <p>Clone o repositório e acesse a pasta do projeto:</p>
    <pre><code>git clone https://github.com/XaianeBorges/API.git
cd API</code></pre>
    <p>Escolha a branch correta (se necessário):</p>
    <pre><code>git checkout XaianeBorges/API</code></pre>
  </div>

  <div class="section">
    <h2>⚙️ Configuração</h2>
    <p>Antes de rodar, configure as variáveis de ambiente ou o arquivo <code>application.properties</code> com os dados do seu banco de dados:</p>
    <pre><code>spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
spring.datasource.username=usuario
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
server.port=8080</code></pre>
    <p>Você pode usar o MySQL Workbench ou outra ferramenta para criar o banco antes de rodar a aplicação.</p>
  </div>

  <div class="section">
    <h2>📌 Executando a API</h2>
    <p>Compile e execute com Maven:</p>
    <pre><code>mvn clean install
mvn spring-boot:run</code></pre>
    <p>A API estará disponível por padrão em: <code>http://localhost:8082</code></p>
  </div>

  <div class="section">
    <h2>🔍 Endpoints (Exemplos)</h2>
    <p>Exemplo de endpoints disponíveis na API:</p>
    <ul>
      <li><code>GET /api/pessoas</code> – lista todas as pessoas</li>
      <li><code>POST /api/usuarios</code> – cria um novo usuário</li>
      <li><code>GET /api/hortas</code> – lista todas as hortas</li>
      <li><code>POST /api/cursos</code> – cria um novo curso</li>
    </ul>
    <p>A documentação visual está disponível em:</p>
    <pre><code>http://localhost:8082/swagger-ui.html</code></pre>
  </div>

  <div class="section">
    <h2>📁 Estrutura de Diretórios</h2>
    <ul>
      <li><code>src/main/java/</code> – código fonte Java</li>
      <li><code>src/main/resources/</code> – configs e propriedades</li>
      <li><code>pom.xml</code> – Gerenciador do Maven</li>
      <li><code>Dockerfile</code> – arquivo de containerização</li>
    </ul>
  </div>

  <div class="section">
    <h2>📜 Licença</h2>
    <p>Este projeto está licenciado sob a licença <strong>Apache-2.0</strong>.</p>
  </div>

  <div class="section">
    <h2>👥 Contribuidores</h2>
    <p>Xaiane Borges</p>

  </div>

</body>
</html>
