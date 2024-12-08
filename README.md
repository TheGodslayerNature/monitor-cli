# Monitor de Saúde do Sistema

Este é um pequeno projeto feito por diversão e apenas para **estudar** a tecnologia shell do Spring, O **Monitor de Saúde do Sistema** desenvolvido em **Java** utilizando o framework **Spring Boot** e a biblioteca **Oshi** para coleta de informações de hardware e software. Ele exibe informações em tempo real sobre o uso da CPU, memória e discos, com uma interface interativa em linha de comando (CLI) criada com o **Spring Shell**.

## Funcionalidades

- **Monitoramento Dinâmico**: Exibição contínua das métricas do sistema em tempo real, incluindo:
    - Uso da CPU (%)
    - Uso de Memória (usado/total em GB)
    - Uso de Disco (nome, modelo, espaço usado/total em GB)
- **Atualização Periódica**: Os dados são atualizados dinamicamente a cada intervalo.
- **Interface CLI**: Comandos intuitivos para interação direta com o monitor.

---

## Tecnologias Utilizadas

- **Java 21*: Linguagem de programação principal.
- **Spring Boot**: Framework para facilitar a configuração e o gerenciamento do projeto.
- **Spring Shell**: Biblioteca para criar uma interface CLI interativa.
- **Oshi (Operating System and Hardware Information)**: Biblioteca para coletar métricas do hardware e do sistema operacional.
- **JUnit e Mockito**: Ferramentas de teste para garantir a confiabilidade e a precisão do software.

---

## Como Executar o Projeto

### Pré-requisitos
- **JDK 21** ou superior instalado.
- Gerenciador de dependências **Maven** (ou embutido no IDE).