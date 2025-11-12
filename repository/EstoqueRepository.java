package repository;

import Models.Estoque;
import Models.Funcionario;
import Models.Movimentacao;
import Models.Produto;
import repository.Repositorio;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class EstoqueRepository implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- Coleções de Dados ---
    private Map<Integer, Produto> produtos = new HashMap<>();
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private List<Funcionario> funcionarios = new ArrayList<>();

    // --- Contadores para IDs ---
    private int nextProdutoId = 1;
    private int nextMovId = 1;
    private int nextUserId = 1;
    private int nextEstoqueId = 1;
    private EstoqueRepository() {} // Construtor privado

    // --- Persistência (Métodos estáticos e públicos) ---

    public List<Estoque> listarEstoques() { 
        return new ArrayList<>( Estoques.values());
    }

    public Estoque buscarEstoquePorProduto(int codigoProduto) { 
        return Estoques.values().stream()
                .filter(e -> e.getProduto().getCodigo() == codigoProduto)
                .findFirst()
                .orElse(null);
    }
    
    public void adicionarEstoque(Service.Estoque e) { 
          Estoque.put(e.getIdEstoque(), e);
    }

    public boolean removerEstoquePorProduto(int codigoProduto, Estoque estoque) {
        Estoque e = buscarEstoquePorProduto(codigoProduto);
        if (e != null) {
            return estoque.remove(e.getIdEstoque()) != null;
        }
        return false;
    }
    public static EstoqueRepository carregar() {
        Object o = Repositorio.carregar();
        if (o instanceof EstoqueRepository) {
            System.out.println("Estado do sistema carregado com sucesso.");
            return (EstoqueRepository) o;
        }
        System.out.println("Iniciando novo estado do sistema.");
        return new EstoqueRepository();
    }

    public void salvar() {
        Repositorio.salvar(this);
    }
    
    // --- Gerenciamento de IDs ---

    public int getNextProdutoId() {
        return nextProdutoId++;
    }

    public int getNextMovId() {
        return nextMovId++;
    }

    public int getNextUserId() {
        return nextUserId++;
    }

    // --- Getters e Lógica CRUD para o Service ---



    
    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtos.values());
    }

    public Produto buscarProduto(int codigo) {
        return produtos.get(codigo);
    }
    
    public void adicionarProduto(Produto p) {
        produtos.put(p.getCodigo(), p);
    }

    public boolean removerProduto(int codigo) {
        return produtos.remove(codigo) != null;
    }
    
    public Funcionario buscarFuncionario(String login) {
        // Encontra o funcionário pelo login
        return funcionarios.stream()
                .filter(f -> f.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }
    
    public void adicionarFuncionario(Funcionario f) {
        funcionarios.add(f);
    }
    
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }
    
    public void adicionarMovimentacao(Movimentacao m) {
        movimentacoes.add(m);
    }
    
    public List<Movimentacao> listarMovimentacoes() {
        return movimentacoes;
    }

    public void adicionarEstoque(Service.Estoque e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionarEstoque'");
    }
}