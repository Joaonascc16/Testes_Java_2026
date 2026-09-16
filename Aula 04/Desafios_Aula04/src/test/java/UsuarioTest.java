import org.example.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void usuarioRecemCriadoDeveTerEstadoInicialCorreto() {

        // Arrange + Act
        Usuario usuario = new Usuario(
                "João",
                "joao@email.com"
        );

        // Assert
        assertAll(
                () -> assertEquals("João", usuario.getNome()),
                () -> assertEquals("joao@email.com", usuario.getEmail()),
                () -> assertNull(usuario.getTelefone()),
                () -> assertTrue(usuario.isAtivo())
        );
    }

    @Test
    void telefoneInicialDeveSerNulo() {

        // Arrange
        Usuario usuario = new Usuario("João", "joao@email.com");

        // Act
        String telefone = usuario.getTelefone();

        // Assert
        assertNull(telefone);
    }

    @Test
    void usuarioRecemCriadoDeveEstarAtivo() {

        // Arrange
        Usuario usuario = new Usuario("João", "joao@email.com");

        // Act
        boolean ativo = usuario.isAtivo();

        // Assert
        assertTrue(ativo);
    }

    @Test
    void definirTelefoneDeveArmazenarTelefoneInformado() {

        // Arrange
        Usuario usuario = new Usuario("João", "joao@email.com");

        // Act
        usuario.definirTelefone("47999999999");

        // Assert
        assertNotNull(usuario.getTelefone());
        assertEquals("47999999999", usuario.getTelefone());
    }

    @Test
    void telefoneNuloDeveLancarExcecao() {

        // Arrange
        Usuario usuario = new Usuario("João", "joao@email.com");

        // Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> usuario.definirTelefone(null)
        );

        // Assert
        assertEquals(
                "O telefone é obrigatório.",
                excecao.getMessage()
        );
    }

    @Test
    void telefoneEmBrancoDeveLancarExcecao() {

        // Arrange
        Usuario usuario = new Usuario("João", "joao@email.com");

        // Act
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> usuario.definirTelefone("   ")
        );

        // Assert
        assertEquals(
                "O telefone é obrigatório.",
                excecao.getMessage()
        );
    }

    @Test
    void desativarDeveAlterarEstadoParaInativo() {

        // Arrange
        Usuario usuario = new Usuario("João", "joao@email.com");

        // Act
        usuario.desativar();

        // Assert
        assertFalse(usuario.isAtivo());
    }
}