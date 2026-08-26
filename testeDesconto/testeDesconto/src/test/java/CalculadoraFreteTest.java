import org.example.CalculadoraFrete;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraFreteTest {

    @ParameterizedTest(name = "peso {0} kg, expresso {1} deve resultar em R$ {2}")
    @CsvSource({
            "1.0,  false, 10.00",
            "1.0,  true,  15.00",
            "5.0,  false, 18.00",
            "5.0,  true,  27.00",
            "10.0, false, 28.00",
            "10.0, true,  42.00"
    })
    void calcularDeveCalcularFrete(
            double pesoKg,
            boolean entregaExpressa,
            double esperado) {

        double obtido = CalculadoraFrete.calcular(pesoKg, entregaExpressa);

        assertEquals(esperado, obtido, 0.001);
    }


    @Test
    void freteComumEExpressoDevTerValoresDiferentes() {

        double freteComum = CalculadoraFrete.calcular(5.0, false);
        double freteExpresso = CalculadoraFrete.calcular(5.0, true);

        assertEquals(18.00, freteComum, 0.001);
        assertEquals(27.00, freteExpresso, 0.001);
    }


    @ParameterizedTest(name = "peso inválido: {0}")
    @ValueSource(doubles = {0.0, -0.01, -1.0, -10.0})
    void pesoInvalidoDeveLancarExcecao(double pesoKg) {

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> CalculadoraFrete.calcular(pesoKg, false)
        );

        assertEquals(
                "O peso deve ser maior que zero.",
                excecao.getMessage()
        );
    }


    @Test
    void pesoMinimoValidoDeveSerAceito() {

        double obtido = CalculadoraFrete.calcular(0.01, false);

        assertEquals(8.02, obtido, 0.001);
    }
}