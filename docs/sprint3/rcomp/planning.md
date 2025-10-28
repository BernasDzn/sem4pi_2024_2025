# Sprint 3 Planning

## **Equipa**

| Número  | Nome              |
| ------- | ----------------- |
| 1221402 | Rui Santiago      |
| 1230896 | Helena Mouro      |
| 1231090 | Bernardo Cardoso  |
| 1231092 | Francisco Lousada |

### **Sprint backlog e distribuição de tarefas**
Neste projeto, a nossa equipa irá desenvolver uma arquitetura ***client-server*** utilizando **Java e Berkeley Sockets API** e utilizando sockets **TCP (Transmission Control Protocol)** para a comunicação entre as aplicações.
Como protocolo de aplicação sobre **TCP**, optámos por utilizar o **HTTP (HyperText Transfer Protocol)**, uma solução já estruturada e amplamente suportada.
Para além disso, as seguintes funcionalidades deverão ser implementadas:

| Membro responsável             | User story                     |
| ------------------------------ | ------------------------------ |
| Rui Santiago **(1221402)**     | US370 - Analyse a proposal     |
| Helena Mouro **(1230896)**     | US371 - Accept/reject proposal |
| Francico Lousada **(1231092)** | US372 - Check shows dates      |
| Bernardo Cardoso **(1231090)** | US373 - Get show info          |
---

## **Servidor e Cliente**

Como dito anteriormente serão usados Java e Berkeley Sockets API, sockets TCP e protocolo de aplicação HTTP, desta forma permitimos a **comunicação em rede entre uma aplicação central (servidor) e várias aplicações cliente**. O objetivo é implementar um sistema distribuído onde os clientes interagem com o servidor e executam as funcionalidades das *user stories* (ver Sprint backlog).

### **Servidor**

A aplicação do servidor, desenvolvida em Java, será executada no **servidor do DEI**. Será desenvolvida de forma a suportar **vários clientes em simultâneo**, recorrendo a **multithreading**. Para cada ligação de cliente, o servidor irá criar uma **thread dedicada** que tratará exclusivamente da comunicação com esse cliente, permitindo assim o atendimento de múltiplos utilizadores sem bloqueios.

#### **O servidor irá:**

- Estar em execução **contínua**, pronto a receber novas ligações.
- Utilizar **sockets Java** (API Berkeley Sockets) para estabelecer e gerir ligações de rede.
- Processar pedidos dos clientes com base no **protocolo HTTP**.
- Autenticar utilizadores e recuperar os dados associados a cada sessão autenticada.
- Suportar as funcionalidades descritas nas *user stories* (ver Sprint backlog).

### **Cliente**

A aplicação cliente será também desenvolvida em Java. Poderá ser executada por vários utilizadores, cada um iniciando a sua própria instância da aplicação no seu computador.

#### **Quando um cliente é iniciado:**

- Cria uma ligação **TCP (socket)** para o servidor.
- Envia **pedidos HTTP** formatados manualmente (por exemplo, "POST /login", "GET /dados").
- Recebe e interpreta as **respostas HTTP** do servidor.
- A aplicação cliente, após login, apresenta então um menu interativo, onde o utilizador pode aceder às funcionalidades disponibilizadas pelas *user stories* (ver Sprint backlog).
- Cada ação do utilizador gera um novo **pedido HTTP por socket TCP**.

A comunicação entre cliente e servidor seguirá o **protocolo HTTP**, garantindo um comportamento consistente em operações como autenticação, consulta e manipulação de dados.

---
### **Explicação em diagrama**

                            +-----------------------------+
                            |        Servidor DEI         |
                            |   (Aplicação Java - Multi   |
                            |        Threads, Sockets)    |
                            +-------------+---------------+
                                          ^
                                          | (socket TCP)
                                          v
    +---------------------+     +---------------------+     +---------------------+
    |       Cliente 1     |     |       Cliente 2     | ... |       Cliente N     |
    | Autentica utilizador|     | Autentica utilizador|     | Autentica utilizador|
    | Apresenta Menu UI   |     | Apresenta Menu UI   |     | Apresenta Menu UI   |
    +---------------------+     +---------------------+     +---------------------+
#### **Resumindo:**
- O servidor está em execução contínua no servidor do DEI, e está preparado para aceitar várias ligações simultâneas através de sockets TCP.
- Cada cliente corre localmente no seu computador, e estabelece ligação ao servidor.
- O servidor cria uma thread para cada cliente, mantendo a comunicação isolada e simultânea com cada utilizador.
- Após a autenticação, o cliente recebe os dados relevantes e apresenta um menu com as funcionalidades definidas nas *user stories* (ver Sprint backlog).

---

## **US370 - Analyse a proposal**

## **US371 - Accept/reject proposal**

## **US372 - Check shows dates**

## **US373 - Get show info**