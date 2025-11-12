package Models;

import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codigo;
    private String nome;
    private String descricao;
    private String categoria;
    private double valorUnitario;

    // Campos de estoque removidos

    public Produto(int codigo, String nome, String descricao, String categoria, double valorUnitario) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valorUnitario = valorUnitario;
    }

    // --- Métodos de Lógica (Simplificados) ---
    
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
    
    @Override
    public String toString() {
        return String.format("COD: %d | %s | %s | Categoria: %s | Custo Un: R$ %.2f",
                codigo, nome, descricao, categoria, valorUnitario);
    }
}