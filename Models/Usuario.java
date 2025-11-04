package models;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected String nome;
    protected String login;
    protected String senha;

    public Usuario(int id, String nome, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getLogin() { return login; }

    public boolean fazerLogin(String login, String senha) {
        return this.login.equals(login) && this.senha.equals(senha);
    }

    public abstract String getRole();

    @Override
    public String toString() {
        return String.format("%s[id=%d, nome=%s, login=%s]", getRole(), id, nome, login);
    }
}
