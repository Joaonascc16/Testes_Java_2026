# Aula 05 — Testes Parametrizados com JUnit 5

**Unidade curricular:** Teste de Sistemas  
**Carga horária:** 4 horas  
**Tema central:** executar a mesma regra de teste com diferentes conjuntos de dados

---

## 1. Objetivos de aprendizagem

Ao final da aula, o estudante deverá ser capaz de:

- reconhecer testes repetidos que podem ser parametrizados;
- usar `@ParameterizedTest` no JUnit 5;
- escolher entre `@ValueSource`, `@CsvSource` e `@MethodSource`;
- relacionar as colunas da fonte aos parâmetros do método de teste;
- criar nomes legíveis para cada execução;
- testar casos comuns, fronteiras e valores inválidos;
- usar `@Timeout` com cautela;
- interpretar qual conjunto de dados provocou uma falha.

## 2. Organização sugerida das 4 horas

| Etapa | Tempo | Estratégia |
|---|---:|---|
| Retomada da Aula 4 | 15 min | Revisão de AAA, `assertEquals` e `assertThrows` |
| Exposição dialogada | 55 min | Slides, comparação de códigos e quiz |
| Demonstração ao vivo | 35 min | Construção da suíte `DescontoTest` |
| Intervalo | 10 min | — |
| Prática guiada | 70 min | Implementação em sete etapas |
| Desafio autônomo | 30 min | Testes da `CalculadoraFrete` |
| Socialização e feedback | 25 min | Leitura de falhas e rubrica formativa |

---

## 3. Ideia central: mesma pergunta, dados diferentes

Um teste parametrizado separa duas coisas:

1. **a regra de verificação**, escrita uma única vez no corpo do método;
2. **os dados dos cenários**, fornecidos por uma anotação ou por um método.

Se quatro casos usam a mesma ação e a mesma asserção, mas mudam apenas a entrada e o resultado esperado, provavelmente existe uma oportunidade de parametrização.

### Antes: repetição de código

```java
@Test
void descontoDe10PorCentoEm100() {
    assertEquals(90.0, Desconto.calcular(100.0, 10), 0.001);
}

@Test
void descontoDe20PorCentoEm200() {
    assertEquals(160.0, Desconto.calcular(200.0, 20), 0.001);
}

@Test
void descontoZeroMantemPreco() {
    assertEquals(80.0, Desconto.calcular(80.0, 0), 0.001);
}
```

### Depois: regra única com vários conjuntos

```java
@ParameterizedTest
@CsvSource({
    "100.0, 10,  90.0",
    "200.0, 20, 160.0",
    " 80.0,  0,  80.0"
})
void calcularDeveAplicarPercentual(
        double preco,
        int percentual,
        double esperado) {

    // Act: executa a regra com os dados da linha atual.
    double obtido = Desconto.calcular(preco, percentual);

    // Assert: compara valores double usando tolerância.
    assertEquals(esperado, obtido, 0.001);
}
```

Três linhas no `@CsvSource` produzem três execuções independentes.

---

## 4. Classe de produção usada na aula

Crie em `src/main/java/org/example/Desconto.java`:

```java
package org.example;

/**
 * Classe utilitária responsável pelo cálculo de descontos.
 */
public final class Desconto {

    // Evita que alguém crie objetos de uma classe que só possui método estático.
    private Desconto() {
    }

    /**
     * Calcula o preço após aplicar um percentual de desconto.
     *
     * @param preco preço original, maior ou igual a zero
     * @param percentual percentual entre 0 e 100
     * @return preço final depois do desconto
     */
    public static double calcular(double preco, int percentual) {
        // A validação ocorre antes de qualquer cálculo.
        if (preco < 0) {
            throw new IllegalArgumentException(
                    "O preço não pode ser negativo."
            );
        }

        // Zero e cem são valores válidos e representam as fronteiras.
        if (percentual < 0 || percentual > 100) {
            throw new IllegalArgumentException(
                    "O percentual deve estar entre 0 e 100."
            );
        }

        // A divisão por 100.0 mantém o cálculo em ponto flutuante.
        double valorDoDesconto = preco * percentual / 100.0;

        return preco - valorDoDesconto;
    }
}
```

## 5. Preparação do projeto

Os testes parametrizados pertencem ao módulo de parâmetros do JUnit 5. Em projetos Maven, a dependência agregadora é suficiente:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.11.4</version>
    <scope>test</scope>
</dependency>
```

Imports mais usados:

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
```

> Todos os imports desta aula começam com `org.junit.jupiter`. Se aparecer `org.junit.Test`, o projeto está misturando JUnit 4 e JUnit 5.

---

## 6. `@ValueSource`: um argumento simples

Use quando apenas um valor varia entre as execuções.

```java
// Indica que este é um teste parametrizado.
// Isso significa que o mesmo teste será executado várias vezes,
// recebendo um valor diferente em cada execução.
//
// {0} representa o primeiro argumento recebido pelo método.
// Exemplos de nomes exibidos no relatório:
// "preço inválido: -0.01"
// "preço inválido: -1.0"
// "preço inválido: -100.0"
@ParameterizedTest(name = "preço inválido: {0}")

// Fornece os valores que serão enviados ao parâmetro "preco".
// Como o parâmetro é double, usamos doubles.
//
// O teste será executado três vezes:
// 1ª execução: preco = -0.01
// 2ª execução: preco = -1.0
// 3ª execução: preco = -100.0
@ValueSource(doubles = {-0.01, -1.0, -100.0})
void precoNegativoDeveLancarExcecao(double preco) {

    // ARRANGE — Preparação
    //
    // O preço não precisa ser criado aqui, pois é recebido
    // como parâmetro por meio do @ValueSource.
    //
    // O percentual de desconto será o mesmo nas três execuções.
    int percentual = 10;

    // ACT — Ação
    //
    // O assertThrows verifica se o código executado dentro da
    // expressão lambda lança a exceção esperada.
    //
    // IllegalArgumentException.class:
    // informa qual tipo de exceção esperamos.
    //
    // () -> Desconto.calcular(preco, percentual):
    // é uma expressão lambda que representa o código que será executado.
    //
    // Se Desconto.calcular() não lançar a exceção, o teste falha.
    // Se lançar outra exceção, o teste também falha.
    //
    // A exceção lançada é capturada e armazenada na variável "excecao".
    IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> Desconto.calcular(preco, percentual)
    );

    // ASSERT — Verificação
    //
    // Além de verificar o tipo da exceção, também conferimos
    // se a mensagem está correta.
    //
    // excecao.getMessage() recupera a mensagem da exceção lançada.
    assertEquals(
            "O preço não pode ser negativo.", // Resultado esperado
            excecao.getMessage()              // Resultado obtido
    );
}
```

Cada valor chega separadamente ao parâmetro `preco`.

Tipos suportados incluem `strings`, `ints`, `longs`, `doubles`, `floats`, `shorts`, `bytes`, `chars`, `booleans` e `classes`.

##    6.1. O que é um teste parametrizado?

Um teste parametrizado recebe diferentes dados sem que seja necessário repetir o código:
```java
@ValueSource(doubles = {-0.01, -1.0, -100.0})
```

É equivalente a escrever três testes separados:
```java
precoNegativoDeveLancarExcecao(-0.01);
precoNegativoDeveLancarExcecao(-1.0);
precoNegativoDeveLancarExcecao(-100.0);
```
A vantagem é evitar repetição e facilitar a inclusão de novos casos.

##    6.2. O padrão AAA

O teste está organizado segundo o padrão Arrange–Act–Assert:

|Etapa	|Significado	|Neste teste|
|:-------|:----------:|---------------:|
|Arrange|	Preparar os dados	|Define percentual = 10|
|Act	|Executar o comportamento	|Chama Desconto.calcular()|
|Assert	|Conferir o resultado	|Verifica a mensagem da exceção|
---------------------------------------------------------------

##    6.3. O que assertThrows verifica?

```java
IllegalArgumentException excecao = assertThrows(
    IllegalArgumentException.class,
    () -> Desconto.calcular(preco, percentual)
);
```

Ele verifica duas coisas:

   -    1 - O método realmente lançou uma exceção.
   -    2 - A exceção é do tipo IllegalArgumentException.

Além disso, devolve a exceção capturada, permitindo verificar sua mensagem.

##    6.4. O que é a expressão lambda?
```java
() -> Desconto.calcular(preco, percentual)
```
Essa expressão significa:

**“Quando o assertThrows solicitar, execute o método Desconto.calcular.”**

Ela poderia ser representada de maneira mais extensa:
```java
() -> {
    Desconto.calcular(preco, percentual);
}
```

A chamada não pode ser feita diretamente assim:

```java
// Incorreto
assertThrows(
    IllegalArgumentException.class,
    Desconto.calcular(preco, percentual)
);
```
O assertThrows precisa receber uma ação para executar e monitorar, não o resultado imediato do método.


##    6.5. Por que conferir a mensagem?

Verificar apenas a classe da exceção confirma que ocorreu um erro, mas não garante que ele aconteceu pelo motivo correto.
```java
assertEquals(
    "O preço não pode ser negativo.",
    excecao.getMessage()
);
```
Esse assertEquals compara:
```java
assertEquals(resultadoEsperado, resultadoObtido);
```
Assim, o teste documenta precisamente a regra de negócio.

Imports necessários
```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
```
**Pequeno desafio:**

Por que esse teste não utiliza 0.0 entre os valores inválidos? 

Isso depende de outra regra: **preço zero é permitido ou também deve lançar exceção?**


## 7. `@CsvSource`: vários argumentos simples

Cada String representa uma execução. As colunas são enviadas aos parâmetros na mesma ordem.

```java
@ParameterizedTest(
        name = "caso {index}: R$ {0} - {1}% deve resultar em R$ {2}"
)
@CsvSource({
    "100.00,  10,  90.00",
    "200.00,  25, 150.00",
    " 80.00,   0,  80.00",
    " 50.00, 100,   0.00"
})
void calcularDeveAplicarPercentual(
        double preco,
        int percentual,
        double esperado) {

    // Act
    double obtido = Desconto.calcular(preco, percentual);

    // Assert: esperado, obtido e delta.
    assertEquals(esperado, obtido, 0.001);
}
```


## Objetivo do teste

Esse código verifica se o método `Desconto.calcular()` aplica corretamente diferentes percentuais de desconto.

Em vez de criar quatro métodos de teste, usamos um **teste parametrizado**. O JUnit executa o mesmo método quatro vezes, usando uma linha diferente do `@CsvSource` em cada execução.

Pense no `@CsvSource` como uma pequena tabela:

|     Preço | Desconto | Resultado esperado |
| --------: | -------: | -----------------: |
| R$ 100,00 |      10% |           R$ 90,00 |
| R$ 200,00 |      25% |          R$ 150,00 |
|  R$ 80,00 |       0% |           R$ 80,00 |
|  R$ 50,00 |     100% |            R$ 0,00 |

## Código completamente comentado

```java
// Indica que este método é um teste parametrizado.
// Isso permite executar o mesmo teste várias vezes,
// utilizando diferentes conjuntos de valores.
@ParameterizedTest(

        // Define como cada execução aparecerá no relatório do JUnit.
        //
        // {index} = número da execução do teste.
        // {0}     = primeiro valor recebido: preço.
        // {1}     = segundo valor recebido: percentual.
        // {2}     = terceiro valor recebido: resultado esperado.
        //
        // Exemplo exibido no relatório:
        // caso 1: R$ 100.00 - 10% deve resultar em R$ 90.00
        name = "caso {index}: R$ {0} - {1}% deve resultar em R$ {2}"
)

// Fornece os dados que serão usados pelo teste.
//
// Cada String representa uma execução.
// Os valores são separados por vírgulas e enviados,
// na mesma ordem, aos parâmetros do método de teste.
@CsvSource({

    // preco = 100.00
    // percentual = 10
    // esperado = 90.00
    "100.00,  10,  90.00",

    // preco = 200.00
    // percentual = 25
    // esperado = 150.00
    "200.00,  25, 150.00",

    // Caso de fronteira: desconto de 0%.
    // O preço deve permanecer igual.
    " 80.00,   0,  80.00",

    // Caso de fronteira: desconto de 100%.
    // O resultado deve ser zero.
    " 50.00, 100,   0.00"
})

// Nome descritivo do comportamento esperado.
//
// O método será executado quatro vezes.
// Em cada execução, os parâmetros receberão
// os valores de uma linha do @CsvSource.
void calcularDeveAplicarPercentual(

        // Recebe o primeiro valor de cada linha.
        double preco,

        // Recebe o segundo valor de cada linha.
        int percentual,

        // Recebe o terceiro valor de cada linha.
        double esperado) {

    // ACT — Ação
    //
    // Executa o comportamento que está sendo testado.
    // O método calcular() recebe o preço e o percentual.
    //
    // O resultado devolvido é armazenado na variável "obtido".
    double obtido = Desconto.calcular(preco, percentual);

    // ASSERT — Verificação
    //
    // Compara o resultado esperado com o resultado obtido.
    //
    // 1º argumento: valor esperado.
    // 2º argumento: valor obtido pelo método.
    // 3º argumento: delta ou margem de tolerância.
    //
    // O teste será aprovado se a diferença entre os valores
    // for menor ou igual a 0.001.
    assertEquals(esperado, obtido, 0.001);
}
```

## Entendendo cada parte

### 1. `@ParameterizedTest`

```java
@ParameterizedTest
```

Essa anotação informa ao JUnit:

> “Este método não será executado apenas uma vez. Ele receberá diferentes conjuntos de dados.”

Sem teste parametrizado, seria necessário escrever algo parecido com:

```java
@Test
void deveAplicarDezPorCento() {
    double obtido = Desconto.calcular(100.00, 10);
    assertEquals(90.00, obtido, 0.001);
}

@Test
void deveAplicarVinteECincoPorCento() {
    double obtido = Desconto.calcular(200.00, 25);
    assertEquals(150.00, obtido, 0.001);
}
```

O teste parametrizado reduz repetição e facilita a inclusão de novos casos.

---

### 2. Nome de cada execução

```java
name = "caso {index}: R$ {0} - {1}% deve resultar em R$ {2}"
```

Os marcadores são substituídos pelos valores de cada execução:

| Marcador  | Representa                      |
| --------- | ------------------------------- |
| `{index}` | Número da execução              |
| `{0}`     | Primeiro argumento: `preco`     |
| `{1}`     | Segundo argumento: `percentual` |
| `{2}`     | Terceiro argumento: `esperado`  |

O relatório ficará semelhante a:

```text
caso 1: R$ 100.00 - 10% deve resultar em R$ 90.00
caso 2: R$ 200.00 - 25% deve resultar em R$ 150.00
caso 3: R$ 80.00 - 0% deve resultar em R$ 80.00
caso 4: R$ 50.00 - 100% deve resultar em R$ 0.00
```

Isso ajuda a identificar rapidamente qual conjunto de dados falhou.

---

### 3. `@CsvSource`

```java
@CsvSource({
    "100.00, 10, 90.00",
    "200.00, 25, 150.00"
})
```

CSV significa **Comma-Separated Values**, ou valores separados por vírgulas.

Cada linha representa um cenário:

```text
preço, percentual, resultado esperado
```

O JUnit converte automaticamente os textos para os tipos declarados no método:

```java
void calcularDeveAplicarPercentual(
    double preco,
    int percentual,
    double esperado
)
```

A relação acontece pela posição:

```text
"100.00, 10, 90.00"
     ↓     ↓     ↓
  preco percentual esperado
```

Portanto, a ordem dos valores precisa corresponder à ordem dos parâmetros.

---

### 4. Onde está o Arrange?

O teste segue o padrão AAA:

* **Arrange:** preparação;
* **Act:** ação;
* **Assert:** verificação.

Neste caso, o `Arrange` está implicitamente no `@CsvSource`:

```java
@CsvSource({
    "100.00, 10, 90.00"
})
```

Os valores já chegam preparados ao método:

```java
double preco,
int percentual,
double esperado
```

Assim, o corpo do teste precisa apenas executar e verificar.

---

### 5. Cálculo esperado

Para calcular o desconto, podemos usar:

```text
desconto = preço × percentual ÷ 100
```

Depois:

```text
valor final = preço − desconto
```

No primeiro cenário:

```text
desconto = 100 × 10 ÷ 100
desconto = 10

valor final = 100 − 10
valor final = 90
```

O teste espera:

```java
esperado = 90.00;
```

---

## Por que utilizar o delta?

O teste usa:

```java
assertEquals(esperado, obtido, 0.001);
```

Para números inteiros, normalmente fazemos uma comparação direta:

```java
assertEquals(10, resultado);
```

Entretanto, valores `double` podem apresentar pequenas imprecisões porque são armazenados em representação binária.

Um cálculo que deveria produzir:

```text
90.00
```

poderia, em determinadas operações, resultar internamente em algo como:

```text
89.999999999
```

O `delta` define uma margem aceitável:

```java
0.001
```

O JUnit verifica:

```text
|esperado − obtido| ≤ delta
```

Por exemplo:

```text
esperado = 90.000
obtido   = 89.9995

diferença = 0.0005
```

Como `0.0005` é menor que `0.001`, o teste passa.

O terceiro argumento não é uma quantidade de casas decimais. Ele é uma **tolerância numérica**.

## Casos de fronteira presentes

O teste não verifica apenas valores comuns. Ele também cobre os limites válidos:

```java
"80.00, 0, 80.00"
```

Com desconto de `0%`, o preço deve permanecer igual.

```java
"50.00, 100, 0.00"
```

Com desconto de `100%`, o resultado deve ser zero.

Esses testes são importantes porque erros costumam aparecer nos limites das regras.

## Imports necessários

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
```

Pergunta de verificação: se acrescentarmos a linha abaixo, quais valores serão recebidos por `preco`, `percentual` e `esperado`?

```java
"300.00, 50, 150.00"
```








### Textos que contêm vírgulas

Use outro delimitador:

```java
@ParameterizedTest
@CsvSource(
    value = {
        "Blumenau, SC;47",
        "Florianópolis, SC;48"
    },
    delimiter = ';'
)
void cidadeDevePossuirDdd(String cidade, int ddd) {
    // teste ilustrativo
}
```

## 8. `@MethodSource`: objetos e dados construídos

Use quando a anotação ficaria difícil de ler ou quando os casos incluem objetos.

```java
@ParameterizedTest(name = "{0}")
@MethodSource("cenariosDeDesconto")
void calcularDeveAtenderCenarios(
        String descricao,
        double preco,
        int percentual,
        double esperado) {

    double obtido = Desconto.calcular(preco, percentual);

    // A descrição também funciona como mensagem em caso de falha.
    assertEquals(esperado, obtido, 0.001, descricao);
}

static Stream<Arguments> cenariosDeDesconto() {
    return Stream.of(
        Arguments.of("sem desconto", 80.0, 0, 80.0),
        Arguments.of("desconto parcial", 200.0, 25, 150.0),
        Arguments.of("desconto total", 50.0, 100, 0.0)
    );
}
```

O método fornecedor é `static` por padrão e retorna um fluxo de argumentos.

## 9. Nulos e vazios

Para parâmetros que aceitam String, coleções ou arrays:

```java
@ParameterizedTest
@NullAndEmptySource
@ValueSource(strings = {"   ", "\t"})
void nomeAusenteDeveSerRejeitado(String nome) {
    assertThrows(
            IllegalArgumentException.class,
            () -> Cadastro.validarNome(nome)
    );
}
```

Anotações úteis:

- `@NullSource`: uma execução com `null`;
- `@EmptySource`: uma execução com valor vazio;
- `@NullAndEmptySource`: combina as duas.

## 10. Casos de fronteira

Para uma regra que aceita percentuais entre 0 e 100:

- `0` e `100` são fronteiras válidas;
- `-1` e `101` estão imediatamente fora da faixa;
- `1` e `99` estão imediatamente dentro da faixa.

Um conjunto forte de testes exercita os dois lados da transição.

```java
@ParameterizedTest
@ValueSource(ints = {-1, 101})
void percentualForaDoIntervaloDeveFalhar(int percentual) {
    IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> Desconto.calcular(100.0, percentual)
    );

    assertEquals(
            "O percentual deve estar entre 0 e 100.",
            excecao.getMessage()
    );
}
```

## 11. `@Timeout`

```java
@Test
@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
void calcularDeveTerminarRapidamente() {
    Desconto.calcular(250.0, 15);
}
```

O teste falha se a chamada ultrapassar o limite. Entretanto:

- `@Timeout` não substitui benchmark;
- limites muito curtos podem produzir falhas instáveis;
- use-o para proteger contra travamentos ou lentidão evidente.

---

## 12. Erros frequentes

| Erro | Causa provável | Correção |
|---|---|---|
| `No ParameterResolver` | O método possui parâmetro, mas não recebeu fonte adequada | Use `@ParameterizedTest` e uma fonte |
| `ArgumentConversionException` | O texto não pode ser convertido para o tipo do parâmetro | Corrija o valor ou o tipo Java |
| `PreconditionViolationException` | O método indicado em `@MethodSource` não existe ou não fornece dados | Confira nome, retorno e `static` |
| `Expected ... Actual ...` | A execução ocorreu, mas o resultado divergiu | Leia os argumentos do caso e revise regra ou expectativa |
| `params cannot be resolved` | Dependência ou import ausente | Confira `junit-jupiter` e os imports |

## 13. Perguntas de fixação

1. Qual fonte usar quando apenas uma String varia?
2. Qual fonte é adequada para preço, percentual e resultado esperado?
3. Quatro linhas em `@CsvSource` produzem quantas execuções?
4. Por que comparar `double` com delta?
5. Quais valores devem ser testados ao redor da faixa 0–100?

### Respostas

1. `@ValueSource(strings = {...})`.
2. `@CsvSource`.
3. Quatro execuções.
4. Porque números de ponto flutuante podem apresentar pequenas diferenças de representação.
5. Pelo menos `0`, `100`, `-1` e `101`; `1` e `99` também fortalecem a cobertura.

---

## 14. Prática guiada

Implemente a suíte da classe `Desconto`:

1. crie a classe de produção;
2. prepare os imports;
3. teste resultados com `@CsvSource`;
4. teste preços negativos com `@ValueSource`;
5. teste percentuais imediatamente fora das fronteiras;
6. adicione um teste de timeout;
7. execute, provoque uma falha e interprete o relatório.

## 15. Desafio autônomo — CalculadoraFrete

Regra:

- frete comum = `8 + pesoKg * 2`;
- entrega expressa = frete comum acrescido de 50%;
- peso deve ser maior que zero;
- mensagem inválida: `O peso deve ser maior que zero.`

### Classe de produção — gabarito

```java
package org.example;

public final class CalculadoraFrete {

    private CalculadoraFrete() {
    }

    public static double calcular(double pesoKg, boolean entregaExpressa) {
        if (pesoKg <= 0) {
            throw new IllegalArgumentException(
                    "O peso deve ser maior que zero."
            );
        }

        double freteComum = 8.0 + pesoKg * 2.0;

        if (entregaExpressa) {
            return freteComum * 1.5;
        }

        return freteComum;
    }
}
```

### Testes — gabarito

```java
package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculadoraFreteTest {

    @ParameterizedTest(
            name = "peso={0}, expressa={1}, esperado={2}"
    )
    @CsvSource({
        "0.01, false,  8.02",
        "1.00, false, 10.00",
        "5.00, false, 18.00",
        "1.00, true,  15.00",
        "5.00, true,  27.00"
    })
    void calcularDeveRetornarFreteCorreto(
            double peso,
            boolean expressa,
            double esperado) {

        double obtido = CalculadoraFrete.calcular(peso, expressa);

        assertEquals(esperado, obtido, 0.001);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.01, -10.0})
    void pesoInvalidoDeveLancarExcecao(double peso) {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> CalculadoraFrete.calcular(peso, false)
        );

        assertEquals(
                "O peso deve ser maior que zero.",
                excecao.getMessage()
        );
    }
}
```

## 16. Critérios de avaliação

- fonte de dados adequada ao cenário;
- correspondência correta entre dados e parâmetros;
- cobertura de casos válidos, inválidos e fronteiras;
- asserções corretas, incluindo delta e mensagem;
- nomes descritivos e comentários que explicam decisões;
- execução integral da suíte sem falhas inesperadas.

---

**Próxima aula:** organização e leitura de resultados de teste, mantendo o foco na qualidade das evidências produzidas pela suíte.
