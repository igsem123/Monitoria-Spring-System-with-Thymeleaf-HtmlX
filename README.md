# 📚 Monitoria - Sistema de Controle de Monitorias com Spring Boot, Thymeleaf e HTMX

Sistema web para gerenciamento de monitorias acadêmicas, com autenticação, controle de acesso por papéis (roles), geração de relatórios e interações dinâmicas com HTMX.

---

## 🚀 Tecnologias Utilizadas

- ✅ **Java 17**
- ✅ **Spring Boot 3**
- ✅ **Spring Security**
- ✅ **Spring Data JPA**
- ✅ **Thymeleaf**
- ✅ **HTMX**
- ✅ **TailwindCSS**
- ✅ **PostgreSQL**
- ✅ **Relatórios PDF**
- ✅ **HTTPS com certificado autoassinado (ambiente local)**

---

## 📦 Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL em execução
- Node.js (para build do TailwindCSS)
- Navegador moderno (Chrome, Firefox, etc.)

---

## 🧪 Configuração do Banco de Dados

Edite o `application.properties` com as credenciais do seu PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/monitoria
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

---

## 🛡️ Segurança e Autenticação

Este sistema implementa autenticação baseada em papéis de usuário:

Papel	Permissões principais
- ADMIN: Acesso total, gerenciamento de usuários e dados
- PROFESSOR: Gerencia disciplinas e monitorias
- MONITOR: Registra presenças e interage com seu perfil

---

## 🔐 HTTPS no Desenvolvimento Local

O sistema é servido via HTTPS (porta 8443) com um certificado autoassinado.

### Gerar o keystore.p12
Execute no terminal:

```
keytool -genkeypair -alias monitoria-ssl -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650 -storepass senha123 -keypass senha123 -dname "CN=localhost, OU=Monitoria, O=IFTMonline, L=Localhost, ST=MG, C=BR"

```

Mova o keystore.p12 para a pasta:

```
src/main/resources/seu-keystore-aqui
```

E adicione isso ao .gitignore:

```
/src/main/resources/keystore.p12
```

Configurações no application.properties

```
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=senha123
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=monitoria-ssl
```

Redirecionamento de HTTP → HTTPS

A aplicação redireciona automaticamente as requisições da porta 8080 para 8443 (HTTPS).

---

## 🧰 Como Rodar o Projeto

Clone o repositório:
- git clone https://github.com/igsem123/Monitoria-Spring-System-with-Thymeleaf-HtmlX.git
- cd Monitoria-Spring-System-with-Thymeleaf-HtmlX

Gere o certificado (ver seção HTTPS acima)
Compile o CSS com Tailwind:
- npm install
- npm run tailwind:build

Execute o projeto:
- ./mvnw spring-boot:run

Acesse no navegador:
- https://localhost:8443

---

## 👨‍🏫 Telas e Funcionalidades

- 📌 Login com controle de acesso por papel
- 📌 Cadastro e edição de usuários
- 📌 Listagem, cadastro e edição de monitorias
- 📌 Listage, cadastro e edição de disciplinas
- 📌 Registro de presenças
- 📌 Geração de relatórios em PDF com JasperSoft
- 📌 Perfil do usuário com avatar e informações dinâmicas (HTMX)

---

## 📄 Licença

Este projeto está sob a licença MIT.

---

## ✉️ Contato

Caso tenha dúvidas ou sugestões:

Criador: @igsem123 @Larissa
