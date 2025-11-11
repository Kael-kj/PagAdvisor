# 🤖 PagAdvisor: O Assessor de Negócios por IA (PagSeguro)

Este projeto é uma demonstração de produto (`proof-of-concept`) desenvolvida como parte de um processo seletivo para a vaga de **Engenheiro Mobile Júnior na PagSeguro**.

O objetivo não era apenas construir um app, mas sim **projetar uma solução completa** que demonstra proficiência na stack principal da PagBank (**Kotlin**, **Jetpack Compose**, **MVVM**, **Retrofit**) e vai além, integrando um **Agente de IA Interativo** (`n8n` + `Gemini`) para resolver um problema de negócio real dos vendedores PagSeguro.

---

### 🎯 A Visão do Produto

O PagAdvisor não é um simples app de dashboard. Ele atua como um **consultor de negócios pessoal e proativo**. Ele entende o perfil do vendedor (ex: "Restaurante de Marmitas") e cruza seus dados de vendas com suas metas (diárias, semanais, anuais) para fornecer planos de ação hiper-personalizados e acionáveis.

---

### ✨ Funcionalidades Principais (O "Showcase")

Este app foi construído em 4 fases, evoluindo de um MVP técnico para uma solução de produto robusta:

* **UI Profissional (Estilo PagBank):** O app replica a identidade visual do PagBank, com uma TopAppBar flutuante (ícones de perfil e notificação) e um design de dashboard baseado em cards, como na referência de design.

* **Fluxo de Autenticação Completo:** Fluxo `v2` completo de autenticação, incluindo Login, Cadastro, Múltiplas telas de Recuperação de Senha e validação de formulários.

* **Setup de Perfil Inteligente (Pós-Cadastro):**
    * O usuário não é jogado no app. Ele passa por um fluxo de *onboarding* de perfil.
    * Ele define seu **Ramo de Negócio** (ex: "Restaurante", "Loja de Varejo", "Serviços").
    * Ele especifica suas **Categorias de Produto** (ex: "Alimentos", "Baixo Ticket", "Roupas").
    * *Lógica de "Outro":* O usuário pode digitar seu próprio ramo ou produto se não estiver listado.

* **Sistema de Metas Flexível (v2):**
    * A tela de Metas permite ao usuário alternar entre metas `Semanal`, `Mensal` e `Anual`.
    * **Sugestão de Meta (IA):** Se o usuário não tem meta, o app analisa suas vendas passadas e sugere uma meta (ex: "Sugerimos uma meta de R$ 2.750,00...").
    * **Meta Diária Automática:** Ao salvar uma meta semanal, o app calcula e salva automaticamente uma meta diária para o dashboard.

* **Agente de IA Interativo (O Cérebro 🧠):**
    * **Personalização Real:** O chat não é genérico. O app envia o **perfil completo** (tipo de negócio, produtos, metas, vendas) para o `n8n` (via Retrofit).
    * **Renderização de Markdown:** O app formata as respostas da IA, transformando `**negrito**` e `### Títulos` em texto formatado.
    * **Conversa com Contexto (Interativa):**
        1.  A IA (Gemini) analisa os dados e, em vez de uma resposta final, sugere **planos de ação** (ex: `[PLANO: Cartão Fidelidade]`).
        2.  O app exibe esses planos como `SuggestionChip` (chips clicáveis).
        3.  Quando o usuário clica em um chip (ou digita "sim"), o app reenvia a *escolha* E o *contexto* da conversa anterior.
        4.  A IA (FLUXO 2) entende a escolha e detalha o passo a passo daquele plano específico.

---


### 🏛️ Arquitetura

O projeto utiliza uma arquitetura limpa baseada em **MVVM** e princípios de **Clean Architecture**, separando o projeto em 3 camadas principais:

* **📁 `data` (Camada de Dados):**
    * **`remote`:** Contém `RetrofitClient` (com timeout de 60s), `OkHttp`, DTOs (`AnalysisRequest`/`Response`) e a interface `N8nApiService`.
    * **`local`:** Contém `UserPreferencesRepository` (usando **DataStore**) para persistir todas as metas e dados de perfil do usuário.
    * **`repository`:** Implementações (`SalesRepositoryImpl`, `PagAdvisorRepositoryImpl`) que fornecem uma fonte única de verdade para os UseCases.

* **📁 `domain` (Camada de Domínio):**
    * Contém a lógica de negócio pura, isolada de Android e APIs.
    * **`usecase`:** Classes para cada ação (ex: `GetAiAnalysisUseCase`, `SaveUserProfileUseCase`, `GetWeeklyGoalUseCase`).
    * **`repository` (Interfaces):** Contratos que a camada `data` deve implementar.

* **📁 `ui` (Camada de UI):**
    * **Jetpack Compose** e **Material 3**.
    * **`screens`:** Cada tela (ex: `DashboardScreen`) e seu respectivo `ViewModel` (ex: `DashboardViewModel`).
    * **`components`:** Componentes reutilizáveis criados para a identidade visual (ex: `PagPrimaryButton`, `PagOutlinedTextField`, `MarkdownText`).
    * **`navigation`:** Gerenciamento de navegação com `NavHost` e um gráfico aninhado (`authGraph`) para compartilhar ViewModels.
    * **Injeção de Dependência (Manual):** O projeto utiliza uma `PagAdvisorApp` (Application class) para inicializar os repositórios e uma `HomeViewModelFactory` para injetar os UseCases corretos nos ViewModels.

---

### 🛠️ Stack de Tecnologias

| Área | Tecnologia | Propósito |
| :--- | :--- | :--- |
| **Mobile (App)** | **Kotlin** | Linguagem principal (100% Kotlin). |
| | **Jetpack Compose** | UI Declarativa e moderna (requisito da vaga). |
| | **Arquitetura MVVM** | Separação de responsabilidades (View, ViewModel, Model/Domain). |
| | **Material 3** | Componentes de UI modernos (Chips, TopAppBar, etc). |
| | **Retrofit & OkHttp** | Comunicação HTTP com o `n8n` (com timeout de 60s). |
| | **Coroutines** | Gerenciamento de threads e assincronia (`viewModelScope`). |
| | **DataStore** | Persistência local (perfil do usuário, metas). |
| | **Navigation Compose** | Navegação single-activity e gráficos aninhados. |
| **Backend (BFF)** | **n8n.io** | Orquestração do workflow, atuando como Backend-for-Frontend. |
| **Inteligência** | **Google Gemini** | Geração das análises e planos de ação. |
| **DevOps** | **GitHub Actions** | Integração Contínua (CI) para builds e testes unitários. |
| **Testes** | **JUnit4** | Testes unitários (ex: `AuthViewModelTest`). |
| | **`kotlinx-coroutines-test`** | Teste de Coroutines e `viewModelScope`. |

---

### 🧠 O Cérebro: Fluxo do `n8n` + Gemini

O fluxo de dados da IA é o coração do projeto:

1.  **App (Compose)**: Usuário clica em "Enviar" no chat.
2.  **App (`ChatViewModel`)**: Monta o `AnalysisRequest.kt` com **todos os dados** (vendas, perfil, metas, contexto).
3.  **App (Retrofit)**: Envia o `POST` para o `n8n` (local ou nuvem).
4.  **`n8n` (Webhook)**: Recebe a chamada.
5.  **`n8n` (Google Gemini)**: Envia os dados para a API do Gemini com o **Prompt V3** (que entende `FLUXO 1` vs `FLUXO 2`).
6.  **`n8n` (Edit Fields)**: Formata a resposta de texto do Gemini em um JSON `{ "aiReply": "..." }`.
7.  **`n8n` (Respond to Webhook)**: Envia o JSON de volta para o app.
8.  **App (`ChatViewModel`)**: Recebe o `AnalysisResponse.kt`, analisa por `[PLANO: ...]`, e atualiza o `UiState`.
9.  **App (Compose)**: Renderiza o `MarkdownText` e os `SuggestionChip`s.

---

### 🚀 Como Executar o Projeto

1.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/Kael-kj/PagAdvisor.git](https://github.com/Kael-kj/PagAdvisor.git)
    ```

2.  **Configure o Backend `n8n`:**
    * Crie um workflow no `n8n` (local ou nuvem) com 4 nós: `Webhook` -> `Google Gemini` -> `Edit Fields` -> `Respond to Webhook`.
    * **`Webhook`:** Mude `Respond` para **`Using Respond to Webhook Node`**.
    * **`Google Gemini`:** Cole o **Prompt da Fase 3** (o último que fizemos) e adicione sua Chave de API do Google.
    * **`Edit Fields`:** Configure o `Mode` para **`JSON`** e use o JSON `{ "aiReply": "{{ ... }}" }` (conforme nossa conversa).
    * Ative o workflow (clique em **"Active"**).

3.  **Configure o App Android:**
    * Abra o projeto no Android Studio.
    * Copie a **"Production URL"** do seu nó `Webhook` no `n8n`.
    * Cole a URL no arquivo `util/Constants.kt` (ex: `const val BASE_URL = "http://localhost:5678/webhook/..."`).
    * **Se estiver usando `n8n` local (http):** Verifique se o `res/xml/network_security_config.xml` está permitindo o tráfego "cleartext" para o seu IP.

4.  **Execute o App!**
    * Crie um novo usuário.
    * Configure o perfil (ex: Restaurante -> Marmitas).
    * Defina uma meta (ex: 5000).
    * Vá ao chat e pergunte: "Como posso melhorar minhas vendas esta semana?"
