 package estoque.teste;

import Models.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioTest {
    private Funcionario funcionario;

    @BeforeEach
    void setUp() {
        // Funcionario: ID 1, Nome "João", Login "joao123", Senha "senha123"
        funcionario = new Funcionario(1, "João Silva", "joao123", "senha123");
    }

    @Test
    void deveConcederAcessoComCredenciaisCorretas() {
        // Testa login e senha corretos
        assertTrue(funcionario.fazerLogin("joao123", "senha123"), "O login deve ser bem-sucedido com credenciais corretas.");
    }

    private void assertTrue(boolean fazerLogin, String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assertTrue'");
    }

    @Test
    void deveNegarAcessoComSenhaIncorreta() {
        // Testa login correto e senha errada
        assertTrue(funcionario.fazerLogin("joao123", "senhaerrada"), "O login deve falhar com senha incorreta.");
    }

    @Test
    void deveNegarAcessoComLoginIncorreto() {
        // Testa login errado e senha correta
        assertTrue(funcionario.fazerLogin("joao errado", "senha123"), "O login deve falhar com login incorreto.");
    }
    
    @Test
    void deveRetornarRoleCorreto() {
        // Testa se o perfil (role) está correto
        assertEquals("Funcionario", funcionario.getRole(), "A função (Role) do funcionário deve ser 'Funcionario'.");
    }
    
    private void assertEquals(String string, String role, String string2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assertEquals'");
    }

    @Test
    void deveRetornarNomeCorreto() {
        // Testa se o nome está correto
        assertEquals("João Silva", funcionario.getNome(), "O nome do funcionário deve ser 'João Silva'.");
    }
}