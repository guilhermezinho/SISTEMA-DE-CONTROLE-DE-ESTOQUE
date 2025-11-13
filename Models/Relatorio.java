package Models;

import java.util.List;
import java.util.stream.Collectors;

public class Relatorio {
    private String titulo;
    private String conteudo;

    private Relatorio(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
    }
    
    // Recebe a lista de Produtos para gerar relatório.
    public static Relatorio relatorioEstoque(List<Produto> produtos) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n==========================================\n");
        sb.append("         RELATÓRIO COMPLETO DE ESTOQUE        \n");
        sb.append("==========================================\n");
        
        double valorTotalCusto = 0;
        int totalUnidades = 0;

        // Itera sobre a lista de Produtos
        for (Produto p : produtos) {
            int quantidade = p.getQuantidade();
            
            // Exibe a informação do Produto
            sb.append(String.format("CÓD: %d | %s | Qtd: %d | Custo Un: R$ %.2f\n",
                    p.getCodigo(), p.getNome(), quantidade, p.getValorUnitario()));
            
            // Cálculo do Valor Total
            valorTotalCusto += p.calcularValorTotal();
            
            // Total de Unidades
            totalUnidades += quantidade;
        }

        sb.append("\n------------------------------------------\n");
        sb.append(String.format("Total de Produtos Distintos: %d\n", produtos.size()));
        sb.append(String.format("Total de Unidades em Estoque: %d\n", totalUnidades));
        sb.append(String.format("Valor Total de Custo do Estoque: R$ %.2f\n", valorTotalCusto));
        sb.append("==========================================\n");

        return new Relatorio("Estoque Completo", sb.toString());
    }

    // --- Relatório de Baixo Estoque ---
    public static Relatorio relatorioBaixoEstoque(List<Produto> produtos) {
        List<Produto> baixos = produtos.stream()
            .filter(Produto::verificarEstoqueMinimo)
            .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("\n==========================================\n");
        sb.append("        RELATÓRIO DE BAIXO ESTOQUE          \n");
        sb.append("==========================================\n");
        
        if (baixos.isEmpty()) {
            sb.append("Nenhum produto abaixo ou no estoque mínimo.\n");
        } else {
            for (Produto p : baixos) {
                sb.append(String.format("Cód: %d | Produto: %s | Qtd Atual: %d | Mínimo: %d\n",
                                         p.getCodigo(), 
                                         p.getNome(), 
                                         p.getQuantidade(), 
                                         p.getEstoqueMinimo()));
            }
        }
        sb.append("==========================================\n");

        return new Relatorio("Baixo Estoque", sb.toString());
    }

    public String gerar() {
        return conteudo;
    }
}