package estoque.tests;

import models.Relatorio;
import models.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RelatorioTest {
    private Produto p1; // Estoque OK
    private Produto p2; // Estoque BAIXO (Igual ao mínimo)
    private Produto p3; // Estoque BAIXO (Abaixo do mínimo)
    private List<Produto> estoque;

    @BeforeEach
    void setUp() {
        // Produto 1: OK (20 > 5). Custo Total: 20 * 50.00 = 1000.00
        p1 = new Produto(1, "Teclado", "Mecânico", "Periféricos", 20, 50.00, 5);
        // Produto 2: BAIXO (10 == 10). Custo Total: 10 * 10.00 = 100.00
        p2 = new Produto(2, "Mousepad", "Grande", "Acessórios", 10, 10.00, 10);
        // Produto 3: BAIXO (4 < 8). Custo Total: 4 * 25.00 = 100.00
        p3 = new Produto(3, "Webcam", "HD", "Periféricos", 4, 25.00, 8);
        
        estoque = Arrays.asList(p1, p2, p3);
    }

    // --- Teste de Relatório Completo ---

    @Test
    void relatorioEstoqueDeveCalcularValorTotalCorretamente() {
        Relatorio relatorio = Relatorio.relatorioEstoque(estoque);
        String conteudo = relatorio.gerar();

        // 1000.00 (p1) + 100.00 (p2) + 100.00 (p3) = 1200.00
        double totalEsperado = 1200.00; 
        
        // Verifica se o valor total está no conteúdo do relatório
        assertTrue(conteudo.contains(String.format("Valor Total de Custo do Estoque: R$ %.2f", totalEsperado)),
                   "O relatório deve exibir o valor total de custo correto.");
        
        // Verifica se a contagem de itens distintos está correta
        assertTrue(conteudo.contains("Total de Produtos Distintos: 3"), 
                   "O relatório deve exibir a contagem correta de produtos distintos.");
    }

    // --- Teste de Relatório de Baixo Estoque ---

    @Test
    void relatorioBaixoEstoqueDeveIncluirApenasItensAbaixoDoMinimo() {
        Relatorio relatorio = Relatorio.relatorioBaixoEstoque(estoque);
        String conteudo = relatorio.gerar();

        // Teclado (p1) está OK, não deve aparecer
        assertFalse(conteudo.contains("Teclado"), "Produto com estoque OK não deve aparecer.");
        
        // Mousepad (p2) está no mínimo, deve aparecer
        assertTrue(conteudo.contains("Mousepad"), "Produto no estoque mínimo deve aparecer.");
        
        // Webcam (p3) está abaixo do mínimo, deve aparecer
        assertTrue(conteudo.contains("Webcam"), "Produto abaixo do estoque mínimo deve aparecer.");
    }
    
    @Test
    void relatorioBaixoEstoqueDeveSerVazioQuandoNaoHaProblemas() {
        // Cria um estoque onde todos os itens estão OK
        Produto pOk1 = new Produto(4, "Cabo", "USB", "Cabos", 50, 5.0, 5);
        Produto pOk2 = new Produto(5, "Hub", "4 portas", "Conectividade", 20, 30.0, 10);
        List<Produto> estoqueOk = Arrays.asList(pOk1, pOk2);

        Relatorio relatorio = Relatorio.relatorioBaixoEstoque(estoqueOk);
        String conteudo = relatorio.gerar();
        
        assertTrue(conteudo.contains("Nenhum produto abaixo do estoque mínimo."), 
                   "O relatório deve indicar que não há problemas.");
    }
}