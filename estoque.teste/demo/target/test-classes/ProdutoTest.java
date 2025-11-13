package estoque.teste; // Crie um pacote de testes separado

import models.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {
    private Produto produto;

    // Configuração: Inicializa um produto antes de cada teste
    @BeforeEach
    void setUp() {
        // Produto: Cód 1, Nome "Monitor", Qtd 50, Custo Unitário R$ 100.00, Mínimo 10
        produto = new Produto(1, "Monitor", "Monitor 24' LED", "Display", 50, 100.00, 10);
    }

    // --- Testes de Cálculo ---
    
    @Test
    void deveCalcularValorTotalCorretamente() {
        // 50 unidades * R$ 100.00 = R$ 5000.00
        assertEquals(5000.00, produto.calcularValorTotal(), 0.001, "O cálculo do valor total de custo deve estar correto.");
    }
    
    @Test
    void deveAtualizarQuantidadeParaEntrada() {
        // 50 (inicial) + 20 (entrada) = 70
        produto.atualizarQuantidade(20);
        assertEquals(70, produto.getQuantidade(), "A quantidade deve aumentar após a entrada.");
    }

    @Test
    void deveAtualizarQuantidadeParaSaida() {
        // 50 (inicial) - 15 (saída) = 35
        produto.atualizarQuantidade(-15);
        assertEquals(35, produto.getQuantidade(), "A quantidade deve diminuir após a saída.");
    }
    
    @Test
    void devePermitirAtualizarValorDeCusto() {
        double novoValor = 120.50;
        produto.setValorUnitario(novoValor);
        assertEquals(novoValor, produto.getValorUnitario(), 0.001, "Deve permitir atualizar o valor de custo.");
        // O valor total deve refletir o novo custo (50 * 120.50 = 6025.00)
        assertEquals(6025.00, produto.calcularValorTotal(), 0.001, "O valor total deve ser recalculado com o novo custo.");
    }

    // --- Testes de Alerta de Estoque ---

    @Test
    void naoDeveAlertarSeAcimaDoMinimo() {
        // Qtd 50 > Mínimo 10
        assertFalse(produto.verificarEstoqueMinimo(), "Não deve alertar se a quantidade for maior que o mínimo.");
    }
    
    @Test
    void deveAlertarSeIgualAoMinimo() {
        // Reduz a quantidade para o mínimo: 50 - 40 = 10
        produto.atualizarQuantidade(-40); 
        // Qtd 10 == Mínimo 10
        assertTrue(produto.verificarEstoqueMinimo(), "Deve alertar se a quantidade for igual ao mínimo.");
    }
    
    @Test
    void deveAlertarSeAbaixoDoMinimo() {
        // Reduz a quantidade abaixo do mínimo: 50 - 45 = 5
        produto.atualizarQuantidade(-45); 
        // Qtd 5 < Mínimo 10
        assertTrue(produto.verificarEstoqueMinimo(), "Deve alertar se a quantidade for menor que o mínimo.");
    }
}