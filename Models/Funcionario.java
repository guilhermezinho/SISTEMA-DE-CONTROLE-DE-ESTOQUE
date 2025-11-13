package Models;

import java.io.Serializable;
public class Funcionario implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String login;
    private String senha; 
    public Funcionario(int id, String nome, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getRole() {
        return "Funcionario";
    }

    // Método de Negócio (Login)
    public boolean fazerLogin(String login, String senha) {
        return this.login.equals(login) && this.senha.equals(senha);
    }
}