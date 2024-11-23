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
---
# Fluxo de Teste do App

O fluxo de funcionamento do aplicativo segue uma sequência de etapas para garantir o correto gerenciamento de ordens de serviço, desde o cadastro até a finalização da entrega. Abaixo, explicamos cada uma das etapas:

## 1. Cadastro de Cliente
Para iniciar o processo, é necessário cadastrar pelo menos um cliente. Este cliente será vinculado à ordem de serviço, um dos requisitos de avaliação é um campo de busca que foi implementado da lista de clientes.

## 2. Cadastro de Funcionário
Em seguida, é necessário cadastrar pelo menos um funcionário. Os funcionários serão responsáveis pela execução das etapas da ordem de serviço e devem ser vinculados ao processo para controle de atividades.

## 3. Cadastro de Acabamento
Também é obrigatório cadastrar ao menos um tipo de acabamento. Esse acabamento será utilizado no produto.

## 4. Cadastro de Equipamento
Para cadastrar o produto, é necessário cadastrar ao menos um equipamento.

## 5. Cadastro de Produto
Após cadastrar o acabamento e o equipamento, você poderá cadastrar o produto. Este produto será utilizado na ordem de serviço.

## 6. Cadastro da Ordem de Serviço
Com todos os dados acima cadastrados (cliente, funcionário, acabamento, equipamento e produto), você estará pronto para cadastrar a ordem de serviço. Este cadastro é o ponto de partida para o gerenciamento da produção.

## 7. Gerenciamento do Status da Ordem de Serviço
Depois de cadastrar a ordem de serviço, será possível alterar seu status conforme o andamento do processo. Os status disponíveis são:

- *Em Produção*: O produto está sendo fabricado.
- *Em Acabamento*: O produto está na fase de acabamento.
- *Pronto para Entrega*: O produto está pronto para ser entregue ao cliente.

## 8. Solicitação de Baixa
Quando a ordem de serviço alcançar o status *"Pronto para Entrega", você poderá solicitar a baixa da entrega. Ao fazer isso, será aberta uma tela (compose) com as informações da ordem de serviço, onde você poderá incluir um desconto, visualizar o valor a pagar e, ao confirmar, o status da ordem de serviço será alterado para *"Solicitado Baixa"**.

## 9. Visualização e Confirmação da Baixa
As solicitações de baixa realizadas estarão listadas no botão *"Solicitações de Baixa". Ao clicar sobre uma ordem de serviço, você visualizará todos os detalhes relacionados à entrega e terá a opção de confirmar a baixa. Ao confirmar, o status da ordem de serviço será alterado para **Baixada*, finalizando o processo.

---

Esse fluxo garante uma *teste eficaz e detalhado* de cada etapa da produção e entrega, desde o cadastro inicial até a finalização da ordem de serviço.
