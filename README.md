# CryptoFolio

Aplicativo de portfolio de criptomoedas com DCA (Dollar Cost Averaging) tracking, construido com Kotlin Multiplatform e Compose Multiplatform.

## Arquitetura

O projeto utiliza uma **Arquitetura Modular** baseada em camadas (Clean Architecture) e o padrao **MVI (Model-View-Intent)** com **Compose Multiplatform**.

### Beneficios:
- **Multiplataforma**: Compartilha ~99% do codigo entre Android e iOS
- **Escalabilidade**: Adicionar novas funcionalidades e simples e nao interfere no codigo existente
- **Reutilizacao**: Modulos de `core` e `domain` sao compartilhados entre features
- **Builds Rapidos**: A modularizacao permite o build incremental, compilando apenas o que foi alterado
- **Manutenibilidade**: Separacao clara de responsabilidades facilita a localizacao de bugs e a evolucao do codigo
- **Testabilidade**: Logica de negocio isolada da UI permite testes unitarios mais eficazes

---

## Estrutura de Modulos

```
crypto-dca/
├── androidApp/              # Entry point Android (MainActivity, Application)
├── iosApp/                  # Entry point iOS (Swift wrapper)
├── composeApp/              # App compartilhado: navegacao, DI setup, App composable
├── build-logic/             # Convention Plugins para configuracao centralizada
├── feature/                 # Features independentes
│   ├── portfolio/           # Visao geral do portfolio e gestao de ativos
│   ├── transaction/         # Gerenciamento de transacoes (add, list, detail)
│   ├── asset-detail/        # Detalhes individuais de ativos
│   └── settings/            # Configuracoes do app
├── core/
│   ├── designsystem/        # Tema, componentes UI, cores, espacamentos
│   ├── ui/                  # BaseViewModel, formatters, Compose effects
│   ├── common/              # Utilitarios, extensions, DateTimeUtil, DispatcherProvider
│   ├── network/             # Ktor client, CoinGecko API
│   └── database/            # Room com suporte KMP (DAOs, entities)
├── domain/
│   ├── model/               # Classes de dominio compartilhadas
│   └── repository/          # Interfaces de repositorio (fronteira clean architecture)
└── gradle/
    └── libs.versions.toml   # Catalogo de versoes
```

---

## Convention Plugins (build-logic)

O projeto utiliza **Gradle Convention Plugins** para centralizar e padronizar as configuracoes de build. Isso elimina duplicacao de codigo nos arquivos `build.gradle.kts` e garante consistencia entre todos os modulos.

### Plugins Disponiveis

| Plugin | Descricao |
|--------|-----------|
| `cryptofolio.kmp.library` | Base para modulos KMP (Android + iOS targets) |
| `cryptofolio.kmp.feature` | Modulos de feature completos (library + Compose + Koin + Serialization) |
| `cryptofolio.android.application` | Modulo app Android principal |
| `cryptofolio.android.library` | Base para modulos de biblioteca Android |
| `cryptofolio.compose.multiplatform` | Habilita Compose Multiplatform e dependencias |
| `cryptofolio.koin` | Configura Koin para injecao de dependencia |

### Exemplo: Criando um Novo Modulo de Feature

```kotlin
// feature/minha-feature/build.gradle.kts
plugins {
    id("cryptofolio.kmp.feature")
}

android {
    namespace = "com.cryptofolio.feature.minhafeature"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Apenas dependencias especificas desta feature
            implementation(project(":core:network"))
            implementation(project(":domain:repository"))
        }
    }
}
```

O plugin `cryptofolio.kmp.feature` ja inclui automaticamente:
- Configuracao de targets KMP (Android + iOS)
- Compose Multiplatform habilitado com dependencias
- Koin configurado
- Kotlin Serialization
- Dependencias comuns: `:core:ui`, `:core:designsystem`, `:core:common`, `:domain:model`

### Estrutura do build-logic

```
build-logic/
├── settings.gradle.kts
└── convention/
    ├── build.gradle.kts
    └── src/main/kotlin/
        ├── KmpLibraryConventionPlugin.kt
        ├── KmpFeatureConventionPlugin.kt
        ├── AndroidApplicationConventionPlugin.kt
        ├── AndroidLibraryConventionPlugin.kt
        ├── ComposeMultiplatformConventionPlugin.kt
        └── KoinConventionPlugin.kt
```

---

## Padrao MVI (Model-View-Intent)

O projeto utiliza uma implementacao customizada de MVI construida sobre o `BaseViewModel`:

### BaseViewModel (core:ui)

```kotlin
abstract class BaseViewModel<State, Action, Event>(initialState: State) : ViewModel() {
    val state: StateFlow<State>        // Estado reativo imutavel
    val events: Flow<Event>            // Eventos one-shot via Channel

    abstract fun onAction(action: Action)
    protected fun updateState(reducer: State.() -> State)
    protected fun sendEvent(event: Event)
}
```

### Componentes MVI por Feature

Cada componente MVI fica em seu proprio arquivo separado:

```kotlin
// PortfolioUiState.kt - apenas a data class de estado
data class PortfolioUiState(
    val portfolio: Portfolio = Portfolio(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

// PortfolioAction.kt - sealed interface para intents do usuario
sealed interface PortfolioAction {
    data object LoadPortfolio : PortfolioAction
    data object RefreshPrices : PortfolioAction
    data class SelectAsset(val coinId: String) : PortfolioAction
}

// PortfolioEvent.kt - sealed interface para eventos one-shot
sealed interface PortfolioEvent {
    data class NavigateToAssetDetail(val coinId: String) : PortfolioEvent
    data class ShowError(val message: String) : PortfolioEvent
}
```

---

## Padroes de Codigo

**Camada de Apresentacao (por feature):**
- `*ViewModel.kt` - Extende `BaseViewModel<State, Action, Event>`
- `*UiState.kt` - Data class imutavel representando o estado da UI
- `*Action.kt` - Sealed interface para acoes do usuario
- `*Event.kt` - Sealed interface para eventos one-shot
- `*Screen.kt` - Composables da tela

**Camada de Dominio:**
- UseCases em `domain/usecase/` com funcao `execute()` ou `operator fun invoke()`
- Interfaces de repositorio em `domain/repository/`
- Models em `domain/model/`

**Camada de Data:**
- Implementacoes de repositorio em `feature/*/data/repository/`
- APIs em `core/network/`
- DAOs em `core/database/`

---

## Navegacao

- Rotas definidas como `@Serializable object/data class`
- Navegacao type-safe com Navigation Compose + Kotlinx Serialization
- Bottom navigation: Portfolio, Transactions, Settings
- Telas de detalhe: Asset Detail, Transaction Detail, Add Transaction

---

## Injecao de Dependencia (Koin)

- **Koin 4.0.2** com suporte KMP completo
- Cada feature tem seu proprio modulo Koin
- ViewModels registrados com `viewModelOf(::ViewModel)`
- Repositorios registrados com `singleOf(::RepositoryImpl) bind Repository::class`
- UseCases registrados com `factoryOf(::UseCase)`
- `AppModule.kt` em `composeApp` agrega todos os modulos

### Exemplo de Modulo Koin

```kotlin
val portfolioModule = module {
    singleOf(::AssetRepositoryImpl) bind AssetRepository::class
    singleOf(::PortfolioRepositoryImpl) bind PortfolioRepository::class
    factoryOf(::GetPortfolioUseCase)
    viewModelOf(::PortfolioViewModel)
}
```

---

## Tecnologias Utilizadas

| Categoria | Tecnologia | Versao |
|-----------|------------|--------|
| Linguagem | Kotlin | 2.1 |
| UI | Compose Multiplatform | 1.8 |
| Design | Material 3 | - |
| DI | Koin | 4.0.2 |
| Navegacao | Navigation Compose | 2.9 |
| Network | Ktor Client | 3.0.3 |
| Serializacao | Kotlinx Serialization | 1.8 |
| Database | Room (KMP) | 2.7.2 |
| Date/Time | Kotlinx DateTime | 0.6.1 |
| Coroutines | Kotlinx Coroutines | 1.10.1 |
| Build | Gradle Convention Plugins | - |

---

## Testes Unitarios

### Estrutura de Testes

Testes ficam em `commonTest` para serem multiplatform:

```
feature/<feature>/src/commonTest/kotlin/com/cryptofolio/feature/<feature>/
├── <Feature>ViewModelTest.kt
├── usecase/
│   └── <UseCase>Test.kt
└── data/
    └── <Repository>Test.kt
```

### Nomenclatura de Testes

Padrao **Given-When-Then** em backticks (usar ` - ` como separador, **NAO usar virgulas** pois Kotlin/Native nao suporta):

```kotlin
@Test
fun `given <condicao> - when <acao> - then <resultado esperado>`() = runTest {
    // Given
    val portfolio = Portfolio.mock()

    // When
    viewModel.onAction(PortfolioAction.LoadPortfolio)
    advanceUntilIdle()

    // Then
    assertEquals(expected, viewModel.state.value)
}
```

**Exemplos de nomenclatura:**

| Camada | Exemplo |
|--------|---------|
| ViewModel | `given portfolio loaded - when RefreshPrices action - then update prices` |
| UseCase | `given repository returns transactions - when execute - then return Success` |
| Repository | `given assets exist - when getPortfolio - then return portfolio with correct assets` |

### Bibliotecas de Teste

| Biblioteca | Uso |
|------------|-----|
| kotlin-test | Framework de testes multiplatform |
| Coroutines Test | Testes de coroutines com `runTest` |
| Turbine | Testes de Flow |

### Boas Praticas

1. **Testes em commonTest** - Testes ficam em `commonTest` para rodar em todas as plataformas
2. **Assertions com data class inteira** - Usar `assertEquals(expected, state)` com `.copy()` ao inves de campo-a-campo
3. **Funcoes mock()** - Classes de dominio devem ter `companion object` com funcoes `mock()`:
   ```kotlin
   data class Transaction(...) {
       companion object {
           fun mock() = Transaction(id = 1, coinId = "bitcoin", ...)
       }
   }
   ```
4. **Fakes sobre Mocks** - Preferir fakes (implementacoes de teste) sobre mocking libraries em KMP
5. **Focar em transformacoes** - Testar logica de mapeamento e transformacao de dados
6. **StandardTestDispatcher** - Usar com `advanceUntilIdle()` para controlar execucao de coroutines
7. **Turbine para Flows** - Usar Turbine para testar emissoes de StateFlow e eventos

### Comandos de Teste

```bash
# Todos os testes
./gradlew allTests

# Testes de um modulo especifico
./gradlew :feature:portfolio:allTests

# Testes com relatorio detalhado
./gradlew :feature:portfolio:allTests --info
```

---

## Como Buildar

### Debug
```bash
./gradlew assembleDebug
```

### Release
```bash
./gradlew assembleRelease
```

### iOS
O app iOS e buildado via Xcode a partir do projeto em `iosApp/`.

### Lint
```bash
./gradlew lint
```
