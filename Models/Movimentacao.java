package Models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimentacao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum TipoMovimentacao {
        ENTRADA, SAIDA
    }

    private int id;
    private TipoMovimentacao tipo;
    private int quantidade;
    private Produto produto;
    private String observacao;
    private LocalDateTime dataHora;

    public Movimentacao(int id, TipoMovimentacao tipo, int quantidade, Produto produto, String observacao) {
        this.id = id;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.produto = produto;
        this.observacao = observacao;
        this.dataHora = LocalDateTime.now();
    }

    // Getters para a Main
    public TipoMovimentacao getTipo() {
        return tipo;
    }
    
    public int getQuantidade() {
        return quantidade;
    }
    
    public Produto getProduto() {
        return produto;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("ID: %d | Tipo: %s | Produto: %s (Cód: %d) | Qtd: %d | Data: %s | Obs: %s",
                             id, tipo, produto.getNome(), produto.getCodigo(), quantidade, dataHora.format(formatter), observacao);
    }
}