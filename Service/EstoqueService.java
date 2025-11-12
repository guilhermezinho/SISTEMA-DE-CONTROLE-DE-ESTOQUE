package Service;

import models.Estoque;
import Models.Funcionario;
import Models.Movimentacao;
import Models.Movimentacao.TipoMovimentacao;
import Models.Produto;
import Models.Relatorio;
import repository.EstoqueRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EstoqueService {
    
    private final EstoqueRepository repository;
    
    public EstoqueService(EstoqueRepository repository) {
        this.repository = repository;
        // Inicia o funcionário padrão se o sistema estiver vazio
        if (repository.getFuncionarios().isEmpty()) {
            repository.adicionarFuncionario(new Funcionario(repository.getNextUserId(), "Funcionario Inicial", "func", "123"));
            System.out.println("\n*** ATENÇÃO: Sistema inicializado com Funcionário Padrão (Login: func | Senha: 123) ***");
        }
    }

    // --- Lógica de Autenticação ---
    
    public Funcionario fazerLogin(String login, String senha) {
        // Busca o funcionário pelo login
        for (Funcionario f : repository.getFuncionarios()) {
            // O método fazerLogin() está na classe Funcionario, mantendo SRP na Model.
            if (f.fazerLogin(login, senha)) {
                return f;
            }
        }
        return null;
    }

    // --- Lógica CRUD de Produtos ---

    public Produto cadastrarProduto(String nome, String descricao, String categoria, double valorUnitario, String localizacao, int quantidadeInicial, int estoqueMinimo) {
        // 1. Cria o Produto
        int novoCodigo = repository.getNextProdutoId();
        Produto p = new Produto(novoCodigo, nome, descricao, categoria, valorUnitario);
        
        // 2. Cria o Estoque e associa ao Produto
        int idEstoque = repository.getNextProdutoId();
        Estoque e = new Estoque(idEstoque, p, localizacao, estoqueMinimo, quantidadeInicial);
        
        // 3. Persiste ambos
        repository.adicionarProduto(p);
        repository.adicionarEstoque(e);
        
        return p;
    }
public Models.Estoque buscarEstoque(int codigoProduto) {
        return repository.buscarEstoquePorProduto(codigoProduto);
    }
    public Produto buscarProduto(int codigo) {
        return repository.buscarProduto(codigo);
    }

    public boolean editarProduto(int codigo, String nome, String descricao, String categoria, Double valorUnitario, Integer estoqueMinimo) {
        Produto p = repository.buscarProduto(codigo);
        if (p == null) return false;
        
        // Aplica as atualizações apenas se o valor não for null/vazio.
        if (nome != null && !nome.isEmpty()) p.setNome(nome);
        if (descricao != null && !descricao.isEmpty()) p.setDescricao(descricao);
        if (categoria != null && !categoria.isEmpty()) p.setCategoria(categoria);
        if (valorUnitario != null) p.setValorUnitario(valorUnitario);
        if (estoqueMinimo != null) p.setEstoqueMinimo(estoqueMinimo);
        
        return true;
    }
    public boolean editarEstoque(int codigoProduto, String localizacao, Integer estoqueMinimo) {
        // Lógica de edição de dados de estoque
        Estoque e = repository.buscarEstoquePorProduto(codigoProduto);
        if (e == null) return false;

        if (localizacao != null && !localizacao.isEmpty()) e.setLocalizacao(localizacao);
        if (estoqueMinimo != null) e.setNivelMinimo(estoqueMinimo);
        
        return true;
    }

    public boolean excluirProduto(int codigo) {
        return repository.removerProduto(codigo);
    }
    
    // --- Lógica de Movimentação ---

    public Produto registrarEntrada(int codigo, int quantidade, String observacao) throws Exception {
        Produto p = repository.buscarProduto(codigo);
        if (p == null) throw new Exception("Produto não encontrado.");
        if (quantidade <= 0) throw new Exception("Quantidade de entrada deve ser positiva.");

        // Atualiza a Model e registra a Movimentacao
        p.atualizarQuantidade(quantidade);
        Movimentacao m = new Movimentacao(repository.getNextMovId(), TipoMovimentacao.ENTRADA, quantidade, p, observacao);
        repository.adicionarMovimentacao(m);
        return p;
    }

    public Produto registrarSaida(int codigo, int quantidade, String observacao) throws Exception {
        Produto p = repository.buscarProduto(codigo);
        if (p == null) throw new Exception("Produto não encontrado.");
        if (quantidade <= 0) throw new Exception("Quantidade de saída deve ser positiva.");
        
        // **Melhoria**: Validação de estoque como lógica de negócio
        if (quantidade > p.getQuantidade()) {
            throw new Exception("Quantidade insuficiente em estoque. Disponível: " + p.getQuantidade());
        }

        // Atualiza a Model e registra a Movimentacao
        p.atualizarQuantidade(-quantidade);
        Movimentacao m = new Movimentacao(repository.getNextMovId(), TipoMovimentacao.SAIDA, quantidade, p, observacao);
        repository.adicionarMovimentacao(m);
        return p;
    }
    
    // --- Lógica de Relatórios e Consultas ---

    public List<Produto> listarProdutos() {
        return repository.listarProdutos();
    }
    public List<Estoque> listarEstoquesComDetalhes() {
        return repository.listarEstoques();
    }
    
    public double calcularValorTotalEstoque() {
         return repository.listarProdutos().stream()
                .mapToDouble(Produto::calcularValorTotal)
                .sum();
    }

    public List<Movimentacao> listarMovimentacoes() {
        return repository.listarMovimentacoes();
    }
    
    public Relatorio gerarRelatorio(String tipo) {
        List<Produto> produtosList = repository.listarProdutos();
        if ("1".equals(tipo)) {
            return Relatorio.relatorioEstoque(produtosList);
        } else if ("2".equals(tipo)) {
            return Relatorio.relatorioBaixoEstoque(produtosList);
        }
        return null; // Caso contrário, o relatório será gerado na Main
    }

    public List<Produto> checarAlertas() {
        return repository.listarProdutos().stream()
                .filter(Produto::verificarEstoqueMinimo)
                .collect(Collectors.toList());
    }
}