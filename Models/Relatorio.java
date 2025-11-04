package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Relatorio implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime dataGeracao;
    private String tipo;
    private String conteudo;

    public Relatorio(String tipo, String conteudo) {
        this.dataGeracao = LocalDateTime.now();
        this.tipo = tipo;
        this.conteudo = conteudo;
    }

    public String gerar() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Relatorio: %s\nGerado em: %s\n\n%s", tipo, dataGeracao.format(f), conteudo);
    }

    @Override
    public String toString() {
        return gerar();
    }

    // Fábrica de relatório simples
    public static Relatorio relatorioEstoque(List<Produto> produtos) {
        StringBuilder sb = new StringBuilder();
        double total = 0;
        for (Produto p : produtos) {
            sb.append(p.toString()).append("\n");
            total += p.calcularValorTotal();
        }
        sb.append(String.format("\nValor total do estoque: R$ %.2f\n", total));
        return new Relatorio("Estoque Completo", sb.toString());
    }

    public static Relatorio relatorioBaixoEstoque(List<Produto> produtos) {
        StringBuilder sb = new StringBuilder();
        for (Produto p : produtos) {
            if (p.verificarEstoqueMinimo()) {
                sb.append(p.toString()).append("\n");
            }
        }
        return new Relatorio("Produtos com Baixo Estoque", sb.toString());
    }
}
