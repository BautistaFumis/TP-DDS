package Logica.Servicio;

import Logica.Dominio.Entidades.Usuario;
import Logica.Excepciones.CredencialesInvalidasException;
import Persistencia.Repositorios.UsuarioDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestorUsuarioTest {
    @Mock
    private UsuarioDAO usuarioDAO;

    @InjectMocks
    private GestorUsuario gestorUsuario;

    // ✅ TEST 1: Usuario y contraseña correctos → NO lanza excepción
    @Test
    void autenticar_usuarioValido_noLanzaExcepcion() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId("Conserje");
        usuario.setPassword("conserje123");

        when(usuarioDAO.findById("Conserje")).thenReturn(Optional.of(usuario));

        // Act
        assertDoesNotThrow(() -> gestorUsuario.autenticar("Conserje", "conserje123"));

        // Assert
        verify(usuarioDAO, times(1)).findById("Conserje");
    }

    // ❌ TEST 2: Usuario no existe → lanza excepción
    @Test
    void autenticar_usuarioNoExiste_lanzaExcepcion() {
        // Arrange
        when(usuarioDAO.findById("Admi")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                CredencialesInvalidasException.class,
                () -> gestorUsuario.autenticar("Admi", "123")
        );

        verify(usuarioDAO, times(1)).findById("Admi");
    }

    // ❌ TEST 3: Contraseña incorrecta → lanza excepción
    @Test
    void autenticar_passwordIncorrecta_lanzaExcepcion() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId("Conserje");
        usuario.setPassword("conserje123");

        when(usuarioDAO.findById("Conserje")).thenReturn(Optional.of(usuario));

        // Act & Assert
        assertThrows(
                CredencialesInvalidasException.class,
                () -> gestorUsuario.autenticar("Conserje", "incorrecta123")
        );

        verify(usuarioDAO, times(1)).findById("Conserje");
    }
}
