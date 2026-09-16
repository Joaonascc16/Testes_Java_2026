
import org.example.ReservaHotel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservaHotelTest {

    @Test
    void reservaRecemCriadaDeveTerEstadoInicialCompleto() {

        // Arrange + Act
        ReservaHotel reserva = new ReservaHotel(
                "João",
                3,
                200.00
        );

        // Assert
        assertAll(
                () -> assertEquals("João", reserva.getHospede()),
                () -> assertEquals(3, reserva.getQuantidadeDiarias()),
                () -> assertEquals(200.00, reserva.getValorDiaria(), 0.001),
                () -> assertFalse(reserva.isConfirmada()),
                () -> assertNull(reserva.getCodigoConfirmacao())
        );
    }

    @Test
    void codigoInicialDeveSerNulo() {

        // Arrange
        ReservaHotel reserva = new ReservaHotel(
                "João",
                3,
                200.00
        );

        // Act
        String codigo = reserva.getCodigoConfirmacao();

        // Assert
        assertNull(codigo);
    }

    @Test
    void calcularTotalDeveMultiplicarDiariasPeloValorDaDiaria() {

        // Arrange
        ReservaHotel reserva = new ReservaHotel(
                "João",
                3,
                200.00
        );

        // Act
        double total = reserva.calcularTotal();

        // Assert
        assertEquals(600.00, total, 0.001);
    }

    @Test
    void confirmarDeveAlterarEstadoEArmazenarCodigo() {

        // Arrange
        ReservaHotel reserva = new ReservaHotel(
                "João",
                3,
                200.00
        );

        // Act
        reserva.confirmar("ABC123");

        // Assert
        assertAll(
                () -> assertTrue(reserva.isConfirmada()),
                () -> assertNotNull(reserva.getCodigoConfirmacao()),
                () -> assertEquals(
                        "ABC123",
                        reserva.getCodigoConfirmacao()
                )
        );
    }

    @Test
    void hospedeNuloDeveLancarExcecao() {

        // Arrange + Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> new ReservaHotel(null, 2, 100)
        );

        // Assert
        assertEquals(
                "O hóspede é obrigatório.",
                excecao.getMessage()
        );
    }

    @Test
    void hospedeEmBrancoDeveLancarExcecao() {

        // Arrange + Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> new ReservaHotel("   ", 2, 100)
        );

        // Assert
        assertEquals(
                "O hóspede é obrigatório.",
                excecao.getMessage()
        );
    }

    @Test
    void quantidadeDeDiariasZeroDeveLancarExcecao() {

        // Arrange + Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> new ReservaHotel("João", 0, 100)
        );

        // Assert
        assertEquals(
                "A quantidade de diárias deve ser maior que zero.",
                excecao.getMessage()
        );
    }

    @Test
    void quantidadeDeDiariasNegativaDeveLancarExcecao() {

        // Arrange + Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> new ReservaHotel("João", -1, 100)
        );

        // Assert
        assertEquals(
                "A quantidade de diárias deve ser maior que zero.",
                excecao.getMessage()
        );
    }

    @Test
    void valorDaDiariaZeroDeveLancarExcecao() {

        // Arrange + Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> new ReservaHotel("João", 2, 0)
        );

        // Assert
        assertEquals(
                "O valor da diária deve ser maior que zero.",
                excecao.getMessage()
        );
    }

    @Test
    void valorDaDiariaNegativoDeveLancarExcecao() {

        // Arrange + Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> new ReservaHotel("João", 2, -100)
        );

        // Assert
        assertEquals(
                "O valor da diária deve ser maior que zero.",
                excecao.getMessage()
        );
    }

    @Test
    void codigoNuloDeveLancarExcecao() {

        // Arrange
        ReservaHotel reserva = new ReservaHotel(
                "João",
                2,
                100
        );

        // Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> reserva.confirmar(null)
        );

        // Assert
        assertEquals(
                "O código de confirmação é obrigatório.",
                excecao.getMessage()
        );
    }

    @Test
    void codigoEmBrancoDeveLancarExcecao() {

        // Arrange
        ReservaHotel reserva = new ReservaHotel(
                "João",
                2,
                100
        );

        // Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> reserva.confirmar("   ")
        );

        // Assert
        assertEquals(
                "O código de confirmação é obrigatório.",
                excecao.getMessage()
        );
    }

    @Test
    void segundaConfirmacaoDeveLancarExcecao() {

        // Arrange
        ReservaHotel reserva = new ReservaHotel(
                "João",
                2,
                100
        );

        reserva.confirmar("ABC123");

        // Act
        IllegalStateException excecao = assertThrows(
                IllegalStateException.class,
                () -> reserva.confirmar("XYZ789")
        );

        // Assert
        assertEquals(
                "A reserva já está confirmada.",
                excecao.getMessage()
        );

        assertTrue(reserva.isConfirmada());
        assertEquals(
                "ABC123",
                reserva.getCodigoConfirmacao()
        );
    }
}