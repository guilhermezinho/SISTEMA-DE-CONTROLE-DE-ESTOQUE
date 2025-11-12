package estoque.Teste;

import Models.Movimentacao.TipoMovimentacao;
import Models.Produto;
import estoque.repository.EstoqueRepository;
import estoque.service.EstoqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EstoqueServiceTest<EstoqueRepository, EstoqueService> {
    
    private EstoqueRepository repository; 
    private EstoqueService service;

    @BeforeEach
    void setUp() {
        // Garantimos um estado limpo para cada teste.
        repository = EstoqueRepository.carregar(); // O mock de repositório pode ser usado aqui
        service = new EstoqueService(repository);
    }
    
    @Test
    void deveCadastrarEBuscarProdutoCorretamente() {
        // Testa a comunicação Service -> Repository
        Produto p = service.cadastrarProduto("Notebook Teste", "i7", "Eletronicos", 5, 2000.0, 1);
        
        assertNotNull(p, "O produto deve ser criado.");
        assertNotNull(service.buscarProduto(p.getCodigo()), "O produto deve ser encontrado após o cadastro.");
    }
    
    private void assertNotNull(Produto p, String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assertNotNull'");
    }

    @Test
    void deveRegistrarEntradaEAtualizarEstoque() throws Exception {
        Produto p = service.cadastrarProduto("Entrada Item", "Desc", "Cat", 10, 5.0, 5);
        int codigo = p.getCodigo();
        
        service.registrarEntrada(codigo, 20, "Compra de teste");
        
        // Verifica a lógica de negócio (atualização da quantidade)
        assertEquals(30, service.buscarProduto(codigo).getQuantidade(), "Estoque deve ser 10 + 20 = 30.");
        
        // Verifica a lógica de negócio (registro da movimentação)
        assertTrue(service.listarMovimentacoes().get(0).getTipo() == TipoMovimentacao.ENTRADA);
    }

    @Test
    void deveFalharAoRegistrarSaidaComEstoqueInsuficiente() {
        Produto p = service.cadastrarProduto("Saida Item", "Desc", "Cat", 10, 5.0, 5);
        int codigo = p.getCodigo();
        
        // Testa a validação do Service
        assertThrows(Exception.class, () -> {
            service.registrarSaida(codigo, 50, "Excesso de saída"); // Tentativa de tirar 50 de 10
        }, "O Service deve lançar uma exceção quando o estoque é insuficiente.");
    }
    
    @Test
    void deveChecarAlertasCorretamente() {
        service.cadastrarProduto("Produto OK", "Desc", "Cat", 20, 10.0, 5);
        service.cadastrarProduto("Produto Baixo", "Desc", "Cat", 3, 10.0, 10);
        
        List<Produto> alertas = service.checarAlertas();
        
        assertEquals(1, alertas.size(), "Apenas um produto deve estar na lista de alertas.");
        assertEquals("Produto Baixo", alertas.get(0).getNome());
    }
}