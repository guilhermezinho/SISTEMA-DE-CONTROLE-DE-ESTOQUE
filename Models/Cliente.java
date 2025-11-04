package models;

import java.io.Serializable;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String endereco;
    private String telefone;

    public Cliente(int id, String nome, String endereco, String telefone) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }

    @Override
    public String toString() {
        return String.format("Cliente[%d] %s | %s | %s", id, nome, endereco, telefone);
    }
}

