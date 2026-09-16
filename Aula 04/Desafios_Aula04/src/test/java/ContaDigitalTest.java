
import org.example.ContaDigital;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContaDigitalTest {

    @Test
    void saldoInicialDeveSerZero() {

        // Arrange + Act
        ContaDigital conta = new ContaDigital("João");

        // Assert
        assertEquals(0, conta.getSaldo(), 0.001);
    }

    @Test
    void depositoDeveAumentarSaldo() {

        // Arrange
        ContaDigital conta = new ContaDigital("João");

        // Act
        conta.depositar(500);

        // Assert
        assertEquals(500, conta.getSaldo(), 0.001);
    }

    @Test
    void saqueValidoDeveReduzirSaldo() {

        // Arrange
        ContaDigital conta = new ContaDigital("João");
        conta.depositar(500);

        // Act
        conta.sacar(200);

        // Assert
        assertEquals(300, conta.getSaldo(), 0.001);
    }

    @Test
    void depositoZeroDeveLancarExcecao() {

        // Arrange
        ContaDigital conta = new ContaDigital("João");

        // Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> conta.depositar(0)
        );

        // Assert
        assertEquals(
                "O depósito deve ser maior que zero.",
                excecao.getMessage()
        );

        assertEquals(0, conta.getSaldo(), 0.001);
    }

    @Test
    void depositoNegativoDeveLancarExcecao() {

        // Arrange
        ContaDigital conta = new ContaDigital("João");

        // Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> conta.depositar(-100)
        );

        // Assert
        assertEquals(
                "O depósito deve ser maior que zero.",
                excecao.getMessage()
        );

        assertEquals(0, conta.getSaldo(), 0.001);
    }

    @Test
    void saqueZeroDeveLancarExcecao() {

        // Arrange
        ContaDigital conta = new ContaDigital("João");
        conta.depositar(500);

        // Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> conta.sacar(0)
        );

        // Assert
        assertEquals(
                "O saque deve ser maior que zero.",
                excecao.getMessage()
        );

        assertEquals(500, conta.getSaldo(), 0.001);
    }

    @Test
    void saqueNegativoDeveLancarExcecao() {

        // Arrange
        ContaDigital conta = new ContaDigital("João");
        conta.depositar(500);

        // Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> conta.sacar(-100)
        );

        // Assert
        assertEquals(
                "O saque deve ser maior que zero.",
                excecao.getMessage()
        );

        assertEquals(500, conta.getSaldo(), 0.001);
    }

    @Test
    void saqueMaiorQueSaldoDeveLancarExcecao() {

        // Arrange
        ContaDigital conta = new ContaDigital("João");
        conta.depositar(500);

        double saldoAntes = conta.getSaldo();

        // Act
        IllegalStateException excecao = assertThrows(
                IllegalStateException.class,
                () -> conta.sacar(600)
        );

        // Assert
        assertEquals(
                "Saldo insuficiente.",
                excecao.getMessage()
        );

        assertEquals(saldoAntes, conta.getSaldo(), 0.001);
    }
}