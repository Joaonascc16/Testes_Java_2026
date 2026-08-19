import org.example.Circulo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CirculoTest {

    @Test
    void deveCalcularAreaCorretamente() {
        // Arrange (preparar)
        Circulo circulo = new Circulo(5);

        //Act (executar)
        double area = circulo.calcularArea();

        //Assert (verificar)
        assertEquals(Math.PI * 25, area);
    }
    @Test
    void deveIdentificarCirculoGrande() {

        // Arrange (preparar)
        Circulo circulo = new Circulo(3);

        //Act (executar)
        boolean resultado = circulo.circuloGrande();

        //Assert (verificar)
        assertFalse(resultado);

    }
    @Test
    void deveIdentificarCirculoNaoGrande() {
        // Arrange
        Circulo circulo = new Circulo(3);

        // Act
        boolean resultado = circulo.circuloGrande();

        // Assert
        assertFalse(resultado);
    }

    @Test
    void deveLancarExcecaoQuandoRaioForZero() {
        // Arrange, Act e Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Circulo(0)
        );
    }

    @Test
    void deveVerificarTodasAsInformacoesDoCirculo() {
        // Arrange
        Circulo circulo = new Circulo(6);

        // Act e Assert
        assertAll(
                () -> assertEquals(6, circulo.getRaio()),
                () -> assertEquals(Math.PI * 36, circulo.calcularArea()),
                () -> assertTrue(circulo.circuloGrande())
        );
    }
}
