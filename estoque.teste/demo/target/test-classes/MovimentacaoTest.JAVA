package estoque.teste;

import models.Movimentacao;
import models.Movimentacao.TipoMovimentacao;
import models.Produto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovimentacaoTest {
    
    // Cria um produto simulado para ser usado na Movimentacao
    private Produto getMockProduto() {
        return new Produto(100, "Mock Item", "Desc", "Cat", 50, 10.0, 5);
    }

    @Test
    void deveCriarMovimentacaoDeEntradaCorretamente() {
        Produto produto = getMockProduto();
        Movimentacao movimentacao = new Movimentacao(1, TipoMovimentacao.ENTRADA, 15, produto, "Compra NF-001");

        assertEquals(TipoMovimentacao.ENTRADA, movimentacao.getTipo(), "O tipo deve ser ENTRADA.");
        assertEquals(15, movimentacao.getQuantidade(), "A quantidade deve ser 15.");
        assertEquals(produto, movimentacao.getProduto(), "O produto da movimentação deve ser o produto mock.");
    }
    
    @Test
    void deveCriarMovimentacaoDeSaidaCorretamente() {
        Produto produto = getMockProduto();
        Movimentacao movimentacao = new Movimentacao(2, TipoMovimentacao.SAIDA, 5, produto, "Consumo interno");

        assertEquals(TipoMovimentacao.SAIDA, movimentacao.getTipo(), "O tipo deve ser SAIDA.");
        assertEquals(5, movimentacao.getQuantidade(), "A quantidade deve ser 5.");
    }

    @Test
    void toStringDeveConterInformacoesChave() {
        Produto produto = getMockProduto();
        Movimentacao movimentacao = new Movimentacao(3, TipoMovimentacao.ENTRADA, 10, produto, "Teste");
        String output = movimentacao.toString();
        
        // Verifica se a string de saída contém informações essenciais
        assertTrue(output.contains("ID: 3"), "O ID deve estar presente.");
        assertTrue(output.contains("Tipo: ENTRADA"), "O tipo deve estar presente.");
        assertTrue(output.contains("Produto: Mock Item"), "O nome do produto deve estar presente.");
        assertTrue(output.contains("Qtd: 10"), "A quantidade deve estar presente.");
        assertTrue(output.contains("Obs: Teste"), "A observação deve estar presente.");
    }
}