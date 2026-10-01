# Simulador de Tráfego 2D com Múltiplas Threads

## Visão Geral do Projeto

Este projeto consiste em um simulador de tráfego de uma cidade em duas dimensões. O principal objetivo é utilizar programação concorrente (múltiplas threads) para simular o comportamento de veículos em um cruzamento, garantindo a exclusão mútua através de mecanismos como semáforos e mutexes.

A biblioteca gráfica escolhida para a renderização do feedback visual em tempo real é a **libGDX**, utilizando Java.

## Como foi planejado e estruturado

O projeto é dividido em duas grandes partes: a **Lógica de Simulação Concorrente** e a **Renderização Gráfica**.

### 1. Renderização Gráfica (libGDX)
- A libGDX possui um loop principal (`render()`) que roda continuamente (geralmente a 60 frames por segundo).
- A responsabilidade da thread principal da libGDX (a thread de renderização) é apenas **ler o estado atual** do mundo (posição dos carros, cor dos semáforos) e desenhar as texturas correspondentes na tela.
- Não é recomendado fazer pausas (`Thread.sleep`) ou lógica pesada de bloqueio na thread de renderização, pois isso "congela" a tela.

### 2. Lógica de Simulação Concorrente (Threads)
De acordo com os requisitos:
- **Veículos como Threads**: Cada veículo instanciado na simulação roda em sua própria `Thread`. O loop interno da thread do veículo calcula a sua nova posição com base em sua velocidade, verifica se pode avançar (olhando os semáforos e outros carros à frente) e atualiza suas coordenadas.
- **Sincronização (Semáforos/Mutex)**: O cruzamento é a região crítica do projeto. Para evitar batidas, o espaço do cruzamento (ou até mesmo pequenos "blocos" ao longo da pista) pode ser protegido por instâncias de `Semaphore` ou `ReentrantLock` (Mutex).
  - Um veículo, ao tentar entrar em um bloco do cruzamento, tenta adquirir o *lock/permit* correspondente.
  - Se o espaço estiver ocupado ou o semáforo de trânsito estiver vermelho, o veículo (sua thread) ficará bloqueado aguardando liberação.
  - Quando avança, o veículo libera o espaço anterior.
- **Gerador de Tráfego**: Uma thread separada (ou a própria classe de controle de tráfego) pode ser responsável por instanciar novos veículos periodicamente nas bordas do mapa.

### 3. Estrutura de Classes Sugerida
- `TrafficSimulatorGame`: Classe principal do libGDX (herda de `ApplicationAdapter` ou `Game`). Gerencia o carregamento de assets e o método `render()`.
- `Vehicle` (implementa `Runnable` ou herda de `Thread`): Contém `x`, `y`, `velocidade`, `direção` e a referência para a malha de controle de tráfego. O método `run()` atualiza `x` e `y` continuamente e lida com as paradas.
- `TrafficLight` (Semáforo): Controla o estado (Verde, Amarelo, Vermelho) e alterna de forma assíncrona (usando sua própria thread ou um temporizador).
- `TrafficController`: Mantém as estruturas de dados de sincronização (ex: uma matriz de `Semaphore` representando espaços na via e no cruzamento) que os `Vehicles` consultam antes de se moverem.

### 4. Integração Threads vs Renderização
Como o libGDX desenha a tela enquanto as threads atualizam as variáveis `x` e `y` dos carros, as variáveis de posição dos veículos que são lidas pelo libGDX devem ser seguras para acesso concorrente ou declaradas como `volatile` (caso seja apenas atualização atômica de valores primitivos). Em jogos simples, o acesso direto (apenas leitura pela thread gráfica e escrita pela thread do veículo) costuma ser visualmente aceitável se garantida a exclusão mútua das posições lógicas.
