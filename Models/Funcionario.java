package models;

public class Funcionario extends Usuario {
    private static final long serialVersionUID = 1L;

    public Funcionario(int id, String nome, String login, String senha) {
        super(id, nome, login, senha);
    }

    @Override
    public String getRole() {
        return "Funcionario";
    }


}
