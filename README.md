# MobileDU

**MobileDU** é uma biblioteca para Android desenvolvida com o objetivo de apoiar a implementação de diretrizes de **Desenho Universal (DU)** e acessibilidade em aplicativos móveis.

A biblioteca busca reduzir a distância entre diretrizes de acessibilidade, frequentemente apresentadas de forma conceitual ou normativa, e sua operacionalização no desenvolvimento de aplicações Android.

A MobileDU foi desenvolvida originalmente no contexto de uma tese de Doutorado em Ciência da Computação da Universidade Federal do ABC (UFABC).

---

## Objetivo

A MobileDU tem como objetivo oferecer mecanismos reutilizáveis que auxiliem desenvolvedores na implementação de recursos relacionados ao Desenho Universal e à acessibilidade em aplicações Android.

A proposta busca favorecer:

- reutilização de soluções de acessibilidade;
- centralização das configurações;
- personalização da interface;
- redução da necessidade de implementar repetidamente os mesmos recursos;
- integração de recursos de acessibilidade às aplicações consumidoras.

A biblioteca não substitui as APIs de acessibilidade da plataforma Android nem dispensa a aplicação das boas práticas e diretrizes de acessibilidade durante o desenvolvimento do aplicativo consumidor.

---

## Origem acadêmica

A MobileDU foi desenvolvida no contexto da tese:

**Diretrizes para o Desenvolvimento de Aplicações Móveis Voltadas para o Desenho Universal: A Biblioteca MobileDU**

**Autor:** Carlos Jair Coletto  
**Orientadora:** Profa. Dra. Juliana Cristina Braga  
**Programa:** Doutorado em Ciência da Computação  
**Instituição:** Universidade Federal do ABC (UFABC)  
**Ano:** 2026

O trabalho envolveu a identificação, consolidação e operacionalização de diretrizes provenientes de diferentes referências relacionadas à acessibilidade e ao Desenho Universal.

Entre as principais referências utilizadas estão:

- Web Content Accessibility Guidelines (WCAG);
- Guia de Diretrizes de Acessibilidade para Aplicações Móveis (GDAMA);
- ABNT NBR 17060:2022;
- diretrizes e estudos relacionados ao desenvolvimento móvel acessível.

---

## Categorias consideradas

No processo de desenvolvimento da MobileDU, as diretrizes foram organizadas nas seguintes categorias:

- Ambiente;
- Texto;
- Som;
- Vídeo;
- Zoom;
- Entrada;
- Notificações e Alertas;
- Gestos.

A implementação da biblioteca contempla mecanismos associados a parte dessas diretrizes, constituindo uma **implementação de referência** para a operacionalização da proposta.

---

## Principais recursos

### Texto

A MobileDU disponibiliza mecanismos relacionados à personalização da apresentação textual, incluindo:

- alteração do tamanho do texto;
- alteração da cor do texto;
- seleção de fonte;
- aplicação das configurações aos componentes de texto da interface.

### Ambiente

A biblioteca disponibiliza configurações relacionadas ao ambiente de apresentação da aplicação, incluindo:

- configuração da cor do ambiente;
- inversão de cores;
- controle de intensidade;
- ajuste de brilho;
- configuração da orientação da tela.

### Som

A MobileDU possui mecanismos relacionados às configurações sonoras, incluindo:

- controle de volume;
- ativação e desativação de som;
- configurações relacionadas aos recursos sonoros.

### Contraste

A biblioteca disponibiliza configurações relacionadas ao contraste e a combinações de cores destinadas a melhorar a percepção visual da interface.

### LIBRAS e vídeo

A MobileDU possui mecanismos de apoio à apresentação de conteúdo em LIBRAS associado a vídeos, permitindo a utilização de uma janela de vídeo complementar sobre a interface da aplicação.

### Formas alternativas de acesso às configurações

A biblioteca também possui mecanismos destinados a facilitar o acesso às suas configurações, incluindo:

- ícone flutuante de acesso;
- detecção de movimento do dispositivo;
- utilização das teclas de volume como mecanismo adicional de acionamento.

---

## Arquitetura

A implementação utiliza componentes responsáveis pela aplicação das configurações da MobileDU às aplicações consumidoras.

Entre os componentes existentes estão:

- `DUSettingsApplier`;
- `EnvironmentApplier`;
- `SoundApplier`;
- `DUInitializer`;
- `DUVolumeInterceptor`;
- `LibrasSupportManager`;
- `ShakeDetector`.

As preferências da MobileDU são centralizadas e persistidas para que possam ser reutilizadas durante a execução da aplicação.

---

## Instalação

A MobileDU pode ser utilizada como dependência em projetos Android.

### JitPack

Adicione o repositório JitPack ao arquivo `settings.gradle.kts` do projeto:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Em seguida, no arquivo `build.gradle.kts` do módulo da aplicação, adicione a dependência correspondente à versão desejada:

```kotlin
dependencies {
    implementation("com.github.CarlosColetto:MobileDU:v1.2.0")
}
```

> **Importante:** recomenda-se verificar as tags ou releases disponíveis neste repositório para identificar a versão mais recente antes de definir a dependência utilizada no projeto.

---

## Versionamento

As versões disponibilizadas da MobileDU são identificadas por meio de **tags/releases** deste repositório.

Ao utilizar a biblioteca em projetos acadêmicos ou profissionais, recomenda-se registrar explicitamente a versão utilizada.

Por exemplo:

```text
MobileDU v1.2.0
```

A identificação da versão facilita a reprodução de experimentos, a comparação entre implementações e a rastreabilidade dos resultados obtidos.

---

## Repositório oficial de desenvolvimento

Este é o **repositório oficial de desenvolvimento, manutenção e distribuição da MobileDU**:

**https://github.com/CarlosColetto/MobileDU**

As versões mais recentes e as futuras evoluções da biblioteca serão mantidas neste repositório.

---

## Repositório acadêmico

A versão da MobileDU associada ao contexto da tese de doutorado é preservada em um repositório acadêmico separado:

**https://github.com/carloscoletto-academic/MobileDU**

Esse repositório tem a finalidade de preservar a referência acadêmica e o código disponibilizado no contexto da pesquisa.

O desenvolvimento, a manutenção e as novas versões da MobileDU são realizados neste repositório oficial (`CarlosColetto/MobileDU`).

---

## Uso em pesquisas

Pesquisadores que utilizarem a MobileDU em experimentos, Trabalhos de Conclusão de Curso (TCC), dissertações, teses, artigos ou outros trabalhos acadêmicos são incentivados a registrar:

- versão da MobileDU utilizada;
- tag ou release correspondente;
- data de obtenção;
- ambiente Android utilizado;
- versão do Android utilizada nos testes;
- adaptações realizadas no aplicativo consumidor.

Essas informações são importantes para garantir a rastreabilidade e a reprodutibilidade dos resultados.

---

## Limitações

A MobileDU deve ser entendida como uma biblioteca de apoio à operacionalização de diretrizes de Desenho Universal e acessibilidade.

A utilização da biblioteca, isoladamente, não garante que uma aplicação seja plenamente acessível.

A acessibilidade final depende também:

- da arquitetura do aplicativo consumidor;
- dos componentes de interface utilizados;
- da organização e da semântica do conteúdo;
- das APIs e dos recursos de acessibilidade disponibilizados pelo Android;
- da correta integração da biblioteca;
- da aplicação de boas práticas de desenvolvimento acessível;
- da realização de testes de acessibilidade;
- da avaliação com usuários.

---

## Desenvolvimento e evolução

A MobileDU permanece aberta a aprimoramentos e extensões.

Entre as possibilidades de evolução estão:

- ampliação das diretrizes operacionalizadas;
- aprimoramento da integração com diferentes arquiteturas Android;
- evolução dos mecanismos de personalização;
- integração com recursos nativos de acessibilidade;
- ampliação dos mecanismos relacionados a conteúdo multimídia;
- aprimoramento da documentação;
- desenvolvimento de novos exemplos de utilização.

---

## Autoria

### Carlos Jair Coletto

Desenvolvedor da MobileDU e autor da pesquisa de doutorado na qual a biblioteca foi desenvolvida.


### Juliana Cristina Braga

Orientadora da pesquisa de doutorado.

Universidade Federal do ABC (UFABC).

---

## Referência acadêmica

A MobileDU foi desenvolvida no contexto do trabalho:

**COLETTO, Carlos Jair. Diretrizes para o Desenvolvimento de Aplicações Móveis Voltadas para o Desenho Universal: A Biblioteca MobileDU. Tese (Doutorado em Ciência da Computação) — Universidade Federal do ABC, Santo André, 2026. Orientação: Juliana Cristina Braga.**

---

## Licença

A MobileDU é disponibilizada sob a licença **MIT**.

É permitida a utilização, modificação e distribuição da biblioteca nos
termos estabelecidos pela licença.

Consulte o arquivo [LICENSE](LICENSE) para mais informações.

## Contato e contribuições

Sugestões, relatos de problemas e propostas de melhoria podem ser registrados por meio das **Issues** deste repositório.

Contribuições para o aprimoramento e a evolução da MobileDU são bem-vindas.

---

## Sobre este repositório

Este repositório (`CarlosColetto/MobileDU`) deve ser considerado a referência para o **desenvolvimento e para as versões atuais da MobileDU**.

Para fins de rastreabilidade acadêmica, a versão associada à tese de doutorado permanece preservada separadamente em:

**https://github.com/carloscoletto-academic/MobileDU**
