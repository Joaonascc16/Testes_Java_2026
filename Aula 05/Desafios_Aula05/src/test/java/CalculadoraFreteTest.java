import org.example.CalculadoraFrete;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculadoraFreteTest {

    @ParameterizedTest
    @CsvSource({
            "1, false, 10",
            "2, false, 12",
            "5, false, 18",
            "10, false, 28"
    })
    void deveCalcularFreteComum(double pesoKg, boolean entregaExpressa, double valorEsperado) {

        double resultado = CalculadoraFrete.calcular(pesoKg, entregaExpressa);

        assertEquals(valorEsperado, resultado);
    }

    @ParameterizedTest
    @CsvSource({
            "1, true, 15",
            "2, true, 18",
            "5, true, 27",
            "10, true, 42"
    })
    void deveCalcularFreteExpresso(double pesoKg, boolean entregaExpressa, double valorEsperado) {

        double resultado = CalculadoraFrete.calcular(pesoKg, entregaExpressa);

        assertEquals(valorEsperado, resultado);
    }

    @ParameterizedTest
    @CsvSource({
            "0",
            "-1",
            "-5",
            "-10"
    })
    void deveLancarExcecaoQuandoPesoForInvalido(double pesoKg) {

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> CalculadoraFrete.calcular(pesoKg, false)
        );

        assertEquals(
                "O peso deve ser maior que zero.",
                excecao.getMessage()
        );
    }
}