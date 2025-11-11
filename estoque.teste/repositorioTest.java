package estoque.teste;

public package estoque.tests;

import estoque.persistence.Repositorio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para Repositorio, focada em garantir a serialização e desserialização.
 */
public class RepositorioTest {

    // Simulação de um objeto que precisa ser persistido
    // A classe Main do seu projeto é um bom exemplo, mas para o teste
    // usaremos uma classe simples para isolar a funcionalidade.
    private static class MockData implements Serializable {
        private static final long serialVersionUID = 2L;
        private final String message;
        private final int number;

        public MockData(String message, int number) {
            this.message = message;
            this.number = number;
        }
        
        // Getters para verificação
        public String getMessage() {
            return message;
        }

        public int getNumber() {
            return number;
        }
    }

    // O nome do arquivo usado pela classe Repositorio
    private static final String NOME_ARQUIVO = "estoque_data.ser";
    private File arquivoDeTeste;

    @BeforeEach
    void setUp() {
        // Inicializa o objeto File para fácil manipulação
        arquivoDeTeste = new File(NOME_ARQUIVO);
        // Garante que o arquivo não existe antes de cada teste
        if (arquivoDeTeste.exists()) {
            arquivoDeTeste.delete();
        }
    }
    
    @AfterEach
    void tearDown() {
        // Limpa o arquivo após cada teste para não interferir nos próximos
        if (arquivoDeTeste.exists()) {
            arquivoDeTeste.delete();
        }
    }

    @Test
    void deveSalvarECarregarDadosCorretamente() {
        // 1. Configurar o estado inicial
        MockData dadosOriginais = new MockData("Estado inicial do sistema", 42);

        // 2. Salvar os dados
        Repositorio.salvar(dadosOriginais);
        
        // 3. Verificar se o arquivo foi criado
        assertTrue(arquivoDeTeste.exists(), "O arquivo de persistência deve existir após salvar.");

        // 4. Carregar os dados
        Object objetoCarregado = Repositorio.carregar();

        // 5. Verificar a desserialização e os dados
        assertNotNull(objetoCarregado, "O objeto carregado não deve ser nulo.");
        assertTrue(objetoCarregado instanceof MockData, "O objeto deve ser uma instância de MockData.");
        
        MockData dadosCarregados = (MockData) objetoCarregado;
        
        assertEquals(dadosOriginais.getMessage(), dadosCarregados.getMessage(), 
                     "A mensagem carregada deve ser igual à salva.");
        assertEquals(dadosOriginais.getNumber(), dadosCarregados.getNumber(), 
                     "O número carregado deve ser igual ao salvo.");
    }

    @Test
    void deveRetornarNuloSeOArquivoNaoExistir() {
        // Garante que o arquivo de teste não existe
        assertFalse(arquivoDeTeste.exists(), "O arquivo não deve existir antes de carregar pela primeira vez.");
        
        // Tenta carregar sem o arquivo
        Object objetoCarregado = Repositorio.carregar();
        
        // Deve retornar null (tratamento de FileNotFoundException)
        assertNull(objetoCarregado, "Deve retornar null se o arquivo de dados não for encontrado.");
    }
} {
    
}
