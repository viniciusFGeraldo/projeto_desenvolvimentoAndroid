# Projeto sistema SGOS

O Sistema de Gerenciamento de Ordem de Serviço (SGOS) foi desenvolvido
para otimizar a gestão de serviços dentro de uma empresa, permitindo um controle
detalhado e eficiente de todos os aspectos envolvidos na produção. Este sistema
visa facilitar a criação e o acompanhamento de ordens de serviço, integrando
informações sobre clientes, Funcionários, produtos, acabamentos e equipamentos, além de permitir um controle de status em tempo real.


## Integrantes  
- **André Marcassi Satler**: Responsável pela parte visual, incluindo o design das interfaces e a experiência do usuário.  
- **Thiago Schwantes de Moura**: Responsável pelas regras de negócio, implementação da lógica do sistema e integração de funcionalidades.  
- **Vinicius Ferreira Geraldo**: Criador do projeto e responsável pelos commits e pela gestão do controle de versão.  

## Funcionalidades Principais  
- **Cadastro**: Permite registrar novos itens no sistema, como Acabamentos, Equipamentos, Produtos, Clientes e Funcionários.
- **Edição**: Possibilita a modificação de informações previamente cadastradas. 
- **Remoção**: Permite excluir itens do sistema.  
- **Atualização em Tempo Real**: Reflete mudanças no sistema de forma instantânea, mantendo os dados sincronizados.  
- **Consulta**: Permite buscar e visualizar informações específicas.
- **Gestão de Ordens de Serviço**: Permite criar novas ordens de serviço com detalhes importantes, 
além de acompanhar e atualizar o status de cada ordem ao longo do processo
(ex.: "Em Produção", "Em Acabamento", "Pronto para Entrega", "Solicitado Baixa", "Baixada"), 
além disso é possivel solicitar baixa da ordem de serviço 
e após isso tem a autorização dessa solicitação que encerra o processo da ordem de serviço.

- **Fluxo da ordem de serviço: **
Criação da O.S --> Solicitação de baixa --> Autorização da solicitação.

## Instruções de Execução  

### Pré-requisitos  
- **Android Studio** (versão mínima recomendada: API 30 ("R"; Android 11.0) ou superior) koala
- **Java Development Kit (JDK)** versão 17 ou superior
- **Emulador** configurado no Android Studio (Pixel 3 API 30)

### Passos  
1. Clone o repositório:  
   ```bash
   git clone https://github.com/viniciusFGeraldo/projeto_desenvolvimentoAndroid.git
