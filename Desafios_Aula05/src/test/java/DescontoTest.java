
import org.example.Desconto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DescontoTest {

    @ParameterizedTest
    @CsvSource({
            "100, 0, 100",
            "100, 10, 90",
            "100, 20, 80",
            "200, 10, 180",
            "500, 30, 350"
    })
    void deveCalcularDesconto(
            double preco,
            double percentual,
            double esperado) {

        double resultado = Desconto.calcular(preco, percentual);

        assertEquals(esperado, resultado);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, -10, -50, -100})
    void deveRejeitarPrecosNegativos(double preco) {

        assertThrows(
                IllegalArgumentException.class,
                () -> Desconto.calcular(preco, 10)
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, 31})
    void deveRejeitarPercentuaisForaDasFronteiras(
            double percentual) {

        assertThrows(
                IllegalArgumentException.class,
                () -> Desconto.calcular(100, percentual)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "0, 100",
            "30, 70"
    })
    void deveAceitarFronteirasValidas(
            double percentual,
            double esperado) {

        double resultado = Desconto.calcular(100, percentual);

        assertEquals(esperado, resultado);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 99",
            "29, 71"
    })
    void deveAceitarVizinhosInternos(
            double percentual,
            double esperado) {

        double resultado = Desconto.calcular(100, percentual);

        assertEquals(esperado, resultado);
    }

    @Test
    @Timeout(1)
    void deveCalcularDentroDoTempo() {

        Desconto.calcular(100, 10);
    }

    @Test
    void testeQueVaiFalhar() {

        double resultado = Desconto.calcular(100, 10);

        assertEquals(80, resultado);
    }
}