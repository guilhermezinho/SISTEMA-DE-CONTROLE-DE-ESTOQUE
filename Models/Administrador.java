package models;

public class Administrador extends Usuario {
    private static final long serialVersionUID = 1L;

    public Administrador(int id, String nome, String login, String senha) {
        super(id, nome, login, senha);
    }

    @Override
    public String getRole() {
        return "Administrador";
    }

}
