package Models;

import java.io.Serializable;
import java.util.Collection;

public class Estoque implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idEstoque;
    private Produto produto; // Referência ao Produto que está em estoque
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

    // --- Métodos de Lógica de Estoque (Movidos de Produto) ---

    public void atualizarQuantidade(int delta) {
        this.quantidadeAtual += delta;
    }

    public boolean verificarEstoqueMinimo() {
        return this.quantidadeAtual <= this.nivelMinimo;
    }

    // --- Getters e Setters ---

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
    
    @Override
    public String toString() {
        return String.format(
            "| ESTOQUE: %d | Local: %s | Qtd: %d | Min: %d | Produto ID: %d | %s",
            idEstoque, localizacao, quantidadeAtual, nivelMinimo, produto.getCodigo(), produto.getNome()
        );
    }

    public static Object remove(int idEstoque2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    public static void put(int idEstoque2, Estoque e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    public static Collection<Movimentacao> values() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'values'");
    }
}