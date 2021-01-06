# Aplicação Conta e autorização

### Objetivo
Objetivo desse aplicativo é exibir a capacidade técnica/conhecimento no desenvolvimento de  software utilizando a linguagem Java, Spring, paradigma de programação funcional, programação reativa, Imutabilidade e  testes unitários. 

### 1- Tecnologias 

* Spring Boot 2.4.1
* Spring Webflux
* Banco de dados h2
* Java 11
* JUnit
* Maven
* Postmon
* Servidor Netty
* Docker


### 2 Observações Gerais

* Para simplificar, assumirá que o aplicativo lidará com apenas uma conta.
* Assuma que não ocorrerão erros de análise de entrada.
* Os dados são armazenados em memória, não sendo necessário a utilização de banco de dados.

##Docker
O projeto está configurado para ser executado  em ambiente docker, foi adicionado o arquivo Dockerfile e plugin dockerfile-maven-plugin, também em anexo segue umaimagem docker com o nome challenge-authorizer.tar. Para importar imagem para seu ambiente docker execute o comando a seguir:
* docker load --input "challenge-authorizer.tar"

##Construindo aplicativo:

No caminha raiz do aplicativo e com ambiente docker iniciado execute o seguinte comando:
* mvn clean install
Iniciando o aplicativo
* docker run -p 8080:8080 challenge-authorizer:0.0.1-SNAPSHOT

## 3 Testes Unitarios

Testes unitários e testes de integração foram criados nas classes AccountTest, TransactionTest , AccountTestIntegration eTransactionTestIntegration, os mesmo estão cobrindo todas as regras de negócio impostas no anunciado do desafio . Para executar os teste deve-se seguir as seguintes instruções.
*	Caminha para diretório raiz do projeto
*	Execute o seguinte comando: mvn clean test.
O aplicativo foi desenvolvido utilizado paradigma Programação funcional , imutabilidade e código limpo. 
   
   
###  4 Funcionalidades

* Criação de conta
* Autorização de transação

### 4 Regras de negócios

* Depois de criada, a conta não deve ser atualizada ou recriada: conta já inicializada.
* Nenhuma transação deve ser aceita sem uma conta devidamente inicializada: conta não inicializada
* Nenhuma transação deve ser aceita quando o cartão não está ativo: cartão não ativo
* O valor da transação não deve exceder o limite disponível: limite insuficiente
* Não deve haver mais de 3 transações em um intervalo de 2 minutos:
alta frequência-pequeno-intervalo
* Não deve haver mais de 1 transação semelhante (mesmo valor e
comerciante) em um intervalo de 2 minutos: transação dupla