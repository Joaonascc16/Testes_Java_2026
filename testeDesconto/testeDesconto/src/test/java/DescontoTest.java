
import org.example.Desconto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class DescontoTest {
    // Os métodos de teste serão adicionados aqui.

    @ParameterizedTest(name = "R$ {0} com {1}% deve resultar em R$ {2}")
    @CsvSource({
            "100.00,  10,  90.00",
            "200.00,  20, 160.00",
            " 80.00,   0,  80.00",
            " 50.00, 100,   0.00"
    })
    void calcularDeveAplicarPercentual(
            double preco,
            int percentual,
            double esperado) {

        // Act: calcula o resultado para a linha atual.
        double obtido = Desconto.calcular(preco, percentual);

        // Assert: compara doubles usando uma pequena tolerância.
        assertEquals(esperado, obtido, 0.001);
    }

    @ParameterizedTest(name = "preço inválido: {0}")
    @ValueSource(doubles = {-0.01, -1.0, -100.0})
    void precoNegativoDeveLancarExcecao(double preco) {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> Desconto.calcular(preco, 10)
        );

        assertEquals(
                "O preço não pode ser negativo.",
                excecao.getMessage()
        );
    }

    @ParameterizedTest
    @CsvSource({
            "-1,  O percentual deve estar entre 0 e 100.",
            "101, O percentual deve estar entre 0 e 100."
    })
    void percentualForaDoIntervaloDeveFalhar(
            int percentual,
            String mensagemEsperada) {

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> Desconto.calcular(100.0, percentual)
        );

        assertEquals(mensagemEsperada, excecao.getMessage());

    }
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void calcularDeveTerminarRapidamente() {
        Desconto.calcular(250.0, 15);
    }

}