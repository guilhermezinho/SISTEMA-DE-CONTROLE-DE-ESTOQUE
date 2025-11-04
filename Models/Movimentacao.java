package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimentacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private LocalDateTime dataHora;
    private TipoMovimentacao tipo;
    private int quantidade;
    private Produto produto;
    private String observacao;

    public enum TipoMovimentacao { ENTRADA, SAIDA }

    public Movimentacao(int id, TipoMovimentacao tipo, int quantidade, Produto produto, String observacao) {
        this.id = id;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.produto = produto;
        this.observacao = observacao;
        this.dataHora = LocalDateTime.now();
    }

    public int getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public TipoMovimentacao getTipo() { return tipo; }
    public int getQuantidade() { return quantidade; }
    public Produto getProduto() { return produto; }
    public String getObservacao() { return observacao; }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Mov[%d] %s | %s | Qtd: %d | Produto: %s | Obs: %s",
                id, dataHora.format(f), tipo, quantidade, produto.getNome(), observacao);
    }
}
