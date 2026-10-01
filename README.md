# 🚦 Simulador de Tráfego

Uma aplicação interativa de simulação de tráfego urbano multithread desenvolvida em **Java** e **libGDX**, demonstrando conceitos avançados de programação concorrente, sincronização com semáforos/mutexes, gerenciamento de filas e prevenção de deadlocks.

---

## Funcionalidades Principais

- **Controle de Semáforos Inteligente**: Semáforos animados com contadores de tempo indicando a transição de fases.
- **Faixas Múltiplas e Manobras Complexas**:
  - 4 faixas de tráfego bidirecional simultâneo.
  - Veículos podem seguir **em linha reta**, **virar à esquerda** ou **virar à direita** nos cruzamentos de acordo com a faixa ocupada.
- **Gerenciamento de Fila de Espera**:
  - Evita que veículos se sobreponham enquanto aguardam a liberação do sinal.
  - Cálculo de desaceleração e parada progressiva de acordo com os veículos à frente.
- **Detecção de Colisões & Explosões**:
  - Detecção de colisão via bounding box (AABB) com rotação ajustada.
  - Efeito visual de explosão ao colidir com encerramento gracioso da thread do veículo.
- **HUD em Tempo Real**:
  - Contador dinâmico de veículos ativos.
  - Contador de acidentes e colisões ocorridas.
  - Indicador de multiplicador de velocidade da simulação.
- **Controle de Velocidade Interativo**:
  - Pressione `1`: Reduz a velocidade da simulação.
  - Pressione `2`: Acelera a velocidade da simulação.

---

## 🏗️ Arquitetura Concorrente

| Componente | Papel Concorrente |
| :--- | :--- |
| **`Vehicle`** | Executado em sua própria `Thread`. Gerencia movimentação, decisão de curva e estado de parada. |
| **`IntersectionController`** | Thread responsável pelo ciclo dos semáforos, controle de filas por faixa (`ConcurrentLinkedQueue`) e sincronização de travessia do cruzamento através de `Semaphore` (Mutex). |
| **`VehicleGenerator`** | Thread produtora que realiza o spawn randômico de novos veículos até atingir o limite configurado (`MAX_VEHICLES`). |

---

## Como Executar

### Pré-requisitos

- **JDK 17+** instalado.
- **Gradle** (incluso via wrapper `./gradlew`).

### Comandos de Execução

No terminal, execute o comando correspondente ao seu sistema:

**Linux / macOS:**
```bash
./gradlew lwjgl3:run
```

**Windows:**
```cmd
gradlew.bat lwjgl3:run
```

---

## 🎮 Controles

| Tecla | Ação |
| :---: | :--- |
| <kbd>1</kbd> | Desacelerar simulação (0.5x / 0.25x) |
| <kbd>2</kbd> | Acelerar simulação (2.0x / 3.0x) |

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 17+
- **Framework Gráfico**: [libGDX](https://libgdx.com/) (LWJGL3 Backend)
- **Sincronização**: `java.util.concurrent` (`Semaphore`, `ConcurrentLinkedQueue`, `CopyOnWriteArrayList`)
- **Gerenciador de Dependências**: Gradle

