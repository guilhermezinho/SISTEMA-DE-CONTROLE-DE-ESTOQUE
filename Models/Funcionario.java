package Models;

import java.io.Serializable;

// Removendo 'extends Usuario' para torná-la a classe base do perfil.
public class Funcionario implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String login;
    private String senha; // Em um sistema real, a senha deve ser hasheada e nunca armazenada em texto simples.

    public Funcionario(int id, String nome, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    // Métodos de Acesso (Getters)
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }
    
    // Método que garante a compatibilidade com a Main anterior.
    public String getRole() {
        return "Funcionario";
    }

    // Método de Negócio (Login)
    public boolean fazerLogin(String login, String senha) {
        return this.login.equals(login) && this.senha.equals(senha);
    }
    
    // ----------------------------------------------------------------------
    // OBSERVAÇÃO IMPORTANTE SOBRE A LÓGICA DE NEGÓCIO:
    // ----------------------------------------------------------------------
    /*
     * As operações de 'atualizar/cadastrar produto', 'registrar entrada/saída',
     * 'consultar estoque', 'gerar relatório' e 'inventário' (contagem/checagem)
     * NÃO devem estar nos métodos da classe Funcionario.
     * * Essas operações são ações que o funcionário *executa* no sistema de estoque (Main).
     * O código atual da classe Main já implementa todas essas funcionalidades:
     * * - CADASTRAR/ATUALIZAR PRODUTO: Métodos 'cadastrarProduto(sc)' e 'editarProduto(sc)'
     * - BUSCAR/CONSULTAR ESTOQUE: Método 'listarProdutos()'
     * - REGISTRAR ENTRADA/SAÍDA: Métodos 'registrarEntrada(sc, atual)' e 'registrarSaida(sc, atual)'
     * - GERAR RELATÓRIO: Método 'gerarRelatorios(sc)'
     * - ATUALIZAR PREÇO (CUSTO): Incluído implicitamente no método 'editarProduto(sc)'
     * * A classe Funcionario deve ser usada apenas para gerenciar o perfil e acesso,
     * como está agora. A classe Main é o 'controlador' que executa as ações no estoque.
     */
}