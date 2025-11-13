package Models;

import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codigo;
    private String nome;
    private String descricao;
    private String categoria;
    private int quantidade;
    private double valorUnitario; // Valor de CUSTO
    private int estoqueMinimo;

    public Produto(int codigo, String nome, String descricao, String categoria, int quantidade, double valorUnitario, int estoqueMinimo) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.estoqueMinimo = estoqueMinimo;
    }
    
    public double calcularValorTotal(int quantidade) {
        return quantidade * valorUnitario;
    }

    // --- Getters e Setters ---
    
    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }
    
    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    // Métodos de Mutação (Setters)
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // Usado pela Main nos métodos registrarEntrada e registrarSaida
    public void atualizarQuantidade(int delta) {
        this.quantidade += delta;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    // Métodos de Negócio
    public double calcularValorTotal() {
        return this.quantidade * this.valorUnitario;
    }

    public boolean verificarEstoqueMinimo() {
        return this.quantidade <= this.estoqueMinimo;
    }
    
    @Override
    public String toString() {
        return String.format("Cód: %d | Nome: %s | Categoria: %s | Qtd: %d (Mín: %d) | Custo Unit: R$ %.2f | Custo Total: R$ %.2f",
                             codigo, nome, categoria, quantidade, estoqueMinimo, valorUnitario, calcularValorTotal());
    }
}