package models;

import java.io.Serializable;
import java.util.Objects;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codigo;
    private String nome;
    private String descricao;
    private String categoria;
    private int quantidade;
    private double valorUnitario;
    private int estoqueMinimo;

    public Produto(int codigo, String nome, String descricao, String categoria,
                   int quantidade, double valorUnitario, int estoqueMinimo) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.estoqueMinimo = estoqueMinimo;
    }

    // getters e setters
    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public int getQuantidade() { return quantidade; }
    public double getValorUnitario() { return valorUnitario; }
    public int getEstoqueMinimo() { return estoqueMinimo; }

    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }
    public void setEstoqueMinimo(int estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }

    public void atualizarQuantidade(int delta) {
        this.quantidade += delta;
        if (this.quantidade < 0) this.quantidade = 0;
    }

    public boolean verificarEstoqueMinimo() {
        return this.quantidade <= this.estoqueMinimo;
    }

    public double calcularValorTotal() {
        return this.quantidade * this.valorUnitario;
    }

    @Override
    public String toString() {
        return String.format("Produto[%d] %s - %s | Cat: %s | Qtd: %d | R$ %.2f | Min: %d",
                codigo, nome, descricao, categoria, quantidade, valorUnitario, estoqueMinimo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto)) return false;
        Produto produto = (Produto) o;
        return codigo == produto.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
