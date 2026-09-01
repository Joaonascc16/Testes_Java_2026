import org.example.Lampada;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LampadaTest {

    @Test
    void lampadaRecemCriadaDeveEstarDesligada() {
        // Arrange
        Lampada lampada = new Lampada("Quarto");

        // Act
        // Não precisamos fazer nada

        // Assert
        assertFalse(lampada.isLigada());
    }

    @Test
    void lampadaRecemCriadaDeveTerIntensidadeZero() {
        // Arrange
        Lampada lampada = new Lampada("Quarto");

        // Act
        // Não precisamos fazer nada

        // Assert
        assertEquals(0, lampada.getIntensidade());
    }

    @Test
    void ligarDeveAlterarEstadoDaLampada() {
        // Arrange
        Lampada lampada = new Lampada("Quarto");

        // Act
        lampada.ligar();

        // Assert
        assertTrue(lampada.isLigada());
    }

    @Test
    void ligarDeveAlterarIntensidadePara100() {
        // Arrange
        Lampada lampada = new Lampada("Quarto");

        // Act
        lampada.ligar();

        // Assert
        assertEquals(100, lampada.getIntensidade());
    }

    @Test
    void desligarDeveRestaurarEstadoInicial() {
        // Arrange
        Lampada lampada = new Lampada("Quarto");
        lampada.ligar();

        // Act
        lampada.desligar();

        // Assert
        assertFalse(lampada.isLigada());
        assertEquals(0, lampada.getIntensidade());
    }
}
