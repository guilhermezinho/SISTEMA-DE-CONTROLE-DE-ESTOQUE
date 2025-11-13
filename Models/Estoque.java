package Models;

import java.io.Serializable;
import java.util.Collection;

public class Estoque implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idEstoque;
    private Produto produto; 
    private String localizacao;
    private int nivelMinimo;
    private int quantidadeAtual;

    public Estoque(int idEstoque, Produto produto, String localizacao, int nivelMinimo, int quantidadeAtual) {
        this.idEstoque = idEstoque;
        this.produto = produto;
        this.localizacao = localizacao;
        this.nivelMinimo = nivelMinimo;
        this.quantidadeAtual = quantidadeAtual;
    }

    public void atualizarQuantidade(int delta) {
        this.quantidadeAtual += delta;
    }

    public boolean verificarEstoqueMinimo() {
        return this.quantidadeAtual <= this.nivelMinimo;
    }

    public int getIdEstoque() {
        return idEstoque;
    }

    public Produto getProduto() {
        return produto;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getNivelMinimo() {
        return nivelMinimo;
    }

    public void setNivelMinimo(int nivelMinimo) {
        this.nivelMinimo = nivelMinimo;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }
    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }
    
    
    @Override
    public String toString() {
        return String.format(
            "| ESTOQUE: %d | Local: %s | Qtd: %d | Min: %d | Produto ID: %d | %s",
            idEstoque, localizacao, quantidadeAtual, nivelMinimo, produto.getCodigo(), produto.getNome()
        );
    }
} 