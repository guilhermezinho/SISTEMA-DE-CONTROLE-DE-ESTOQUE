
package estoque;

import estoque.models.Funcionario; // Mantido apenas Funcionario
import estoque.models.Movimentacao;
import estoque.models.Movimentacao.TipoMovimentacao;
import estoque.models.Produto;
import estoque.models.Relatorio;
import estoque.persistence.Repositorio;

import java.util.*;
import java.util.stream.Collectors;

public class Main implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Integer, Produto> produtos = new HashMap<>();
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private List<Funcionario> funcionarios = new ArrayList<>(); // Lista renomeada para 'funcionarios'
    
    // Variáveis não utilizadas foram removidas ou mantidas com foco
    private int nextProdutoId = 1;
    private int nextMovId = 1;
    private int nextUserId = 1;

    public static void main(String[] args) {
        Main app = carregarEstado();
        if (app == null) app = new Main();
        app.seedDados();
        app.loop();
    }

    private static Main carregarEstado() {
        Object o = Repositorio.carregar();
        if (o instanceof Main) return (Main) o;
        return null;
    }

    private void salvarEstado() {
        Repositorio.salvar(this);
    }

    private void seedDados() {
        // REMOÇÃO DE DADOS INVENTADOS (SEEDING) - APENAS UM FUNCIONÁRIO INICIAL É CRIADO
        // Nota: Em um sistema real, a criação do primeiro usuário deve ser feita fora do login.
        if (funcionarios.isEmpty()) {
            // Cria um funcionário padrão para permitir o primeiro login, se o sistema estiver vazio
            funcionarios.add(new Funcionario(nextUserId++, "Funcionario Inicial", "func", "123"));
            System.out.println("\n*** ATENÇÃO: Sistema inicializado com Funcionário Padrão (Login: func | Senha: 123) ***");
        }
    }

    private void loop() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Sistema de Controle de Estoque (Console) ===");
        
        // Agora o login é feito diretamente buscando na lista de funcionários
        Funcionario atual = login(sc); 
        
        if (atual == null) {
            System.out.println("Login falhou. Saindo.");
            return;
        }
        System.out.println("Bem-vindo, " + atual.getNome() + " (Funcionário)");
        boolean sair = false;
        while (!sair) {
            mostrarMenu(atual);
            String opc = sc.nextLine().trim();
            switch (opc) {
                case "1": listarProdutos(); break;
                case "2": cadastrarProduto(sc); break;
                case "3": editarProduto(sc); break;
                case "4": excluirProduto(sc); break;
                
                case "5": registrarEntrada(sc, atual); break;
                case "6": registrarSaida(sc, atual); break; 
                case "7": gerarRelatorios(sc); break;
                case "8": listarMovimentacoes(); break;
                case "9": salvarEstado(); System.out.println("Dados salvos."); break;
                case "0": salvarEstado(); sair = true; break;
                default: System.out.println("Opção inválida."); break;
            }
            checarAlertas();
        }
        System.out.println("Encerrando sistema. Até mais!");
    }

    // Método de login ajustado para buscar na lista de Funcionario
    private Funcionario login(Scanner sc) {
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        for (Funcionario f : funcionarios) {
            // Assumindo que a classe Funcionario tem o método fazerLogin
            if (f.fazerLogin(login, senha)) return f; 
        }
        return null;
    }

    private void mostrarMenu(Funcionario u) {
        System.out.println("\n--- Menu ---");
        System.out.println("1 - Listar Produtos");
        System.out.println("2 - Cadastrar Produto");
        System.out.println("3 - Editar Produto");
        System.out.println("4 - Excluir Produto");
        System.out.println("5 - Registrar Entrada");
        System.out.println("6 - Registrar Saída (Baixa/Consumo)");
        System.out.println("7 - Gerar Relatórios");
        System.out.println("8 - Listar Movimentações");
        System.out.println("9 - SALVAR");
        System.out.println("0 - SAIR");
        System.out.print("Escolha: ");
    }

    // CRUD produtos (internos)
    private Produto adicionarProduto(String nome, String descricao, String categoria, int quantidade, double valorUnitario, int estoqueMinimo) {
        Produto p = new Produto(nextProdutoId++, nome, descricao, categoria, quantidade, valorUnitario, estoqueMinimo);
        produtos.put(p.getCodigo(), p);
        return p;
    }

    private boolean editarProdutoInternal(int codigo, String nome, String descricao, String categoria,
                                          Integer quantidade, Double valorUnitario, Integer estoqueMinimo) {
        Produto p = produtos.get(codigo);
        if (p == null) return false;
        if (nome != null) p.setNome(nome);
        if (descricao != null) p.setDescricao(descricao);
        if (categoria != null) p.setCategoria(categoria);
        if (quantidade != null) p.setQuantidade(quantidade); 
        if (valorUnitario != null) p.setValorUnitario(valorUnitario);
        if (estoqueMinimo != null) p.setEstoqueMinimo(estoqueMinimo);
        return true;
    }

    private boolean removerProduto(int codigo) {
        return produtos.remove(codigo) != null;
    }

    private void listarProdutos() {
        System.out.println("\n--- Produtos ---");
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        produtos.values().forEach(System.out::println);
        
        double total = produtos.values().stream().mapToDouble(Produto::calcularValorTotal).sum();
        System.out.printf("\n*** VALOR TOTAL DE CUSTO DO ESTOQUE: R$ %.2f ***\n", total);
        System.out.println("Total de itens distintos em estoque: " + produtos.size());
    }

    // Métodos de console
    private void cadastrarProduto(Scanner sc) {
        try {
            System.out.print("Nome: "); String nome = sc.nextLine();
            System.out.print("Descricao: "); String desc = sc.nextLine();
            System.out.print("Categoria: "); String cat = sc.nextLine();
            System.out.print("Quantidade inicial: "); int qtd = Integer.parseInt(sc.nextLine());
            System.out.print("Valor unitario de CUSTO: "); double val = Double.parseDouble(sc.nextLine());
            System.out.print("Estoque minimo (alerta): "); int min = Integer.parseInt(sc.nextLine());
            Produto p = adicionarProduto(nome, desc, cat, qtd, val, min);
            System.out.println("Produto cadastrado: " + p);
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void editarProduto(Scanner sc) {
        try {
            System.out.print("Codigo do produto a editar: "); int cod = Integer.parseInt(sc.nextLine());
            Produto p = produtos.get(cod);
            if (p == null) { System.out.println("Produto nao encontrado."); return; }
            System.out.println("Produto atual: " + p);
            System.out.print("Novo nome (enter para manter): "); String nome = sc.nextLine(); if (nome.isEmpty()) nome = null;
            System.out.print("Nova descricao (enter p manter): "); String desc = sc.nextLine(); if (desc.isEmpty()) desc = null;
            System.out.print("Nova categoria (enter p manter): "); String cat = sc.nextLine(); if (cat.isEmpty()) cat = null;
            System.out.print("Novo valor de CUSTO (enter p manter): "); String vals = sc.nextLine(); Double val = vals.isEmpty()? null: Double.parseDouble(vals);
            System.out.print("Novo estoque minimo (enter p manter): "); String mins = sc.nextLine(); Integer min = mins.isEmpty()? null: Integer.parseInt(mins);
            
            editarProdutoInternal(cod, nome, desc, cat, null, val, min);
            System.out.println("Produto atualizado: " + produtos.get(cod));
        } catch (Exception e) {
            System.out.println("Erro ao editar: " + e.getMessage());
        }
    }

    private void excluirProduto(Scanner sc) {
        try {
            System.out.print("Codigo do produto a remover: "); int cod = Integer.parseInt(sc.nextLine());
            if (removerProduto(cod)) System.out.println("Produto removido.");
            else System.out.println("Produto nao encontrado.");
        } catch (Exception e) {
            System.out.println("Erro ao remover: " + e.getMessage());
        }
    }

    private void registrarEntrada(Scanner sc, Funcionario u) {
        try {
            System.out.print("Codigo do produto: "); int cod = Integer.parseInt(sc.nextLine());
            Produto p = produtos.get(cod);
            if (p == null) { System.out.println("Produto nao encontrado."); return; }
            System.out.print("Quantidade entrada (compra/reposição): "); int qtd = Integer.parseInt(sc.nextLine());
            System.out.print("Observacao (Ex: Nota Fiscal 123): "); String obs = sc.nextLine();
            registrarMovimentacao(TipoMovimentacao.ENTRADA, qtd, p, obs);
            System.out.println("Entrada registrada. Novo estoque: " + p.getQuantidade());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void registrarSaida(Scanner sc, Funcionario u) {
        try {
            System.out.print("Codigo do produto: "); int cod = Integer.parseInt(sc.nextLine());
            Produto p = produtos.get(cod);
            if (p == null) { System.out.println("Produto nao encontrado."); return; }
            System.out.print("Quantidade saída (baixa/consumo): "); int qtd = Integer.parseInt(sc.nextLine());
            System.out.print("Motivo da baixa (Ex: Uso interno, Descarte, Perda): "); String obs = sc.nextLine();
            if (qtd > p.getQuantidade()) {
                System.out.println("Quantidade insuficiente em estoque. Disponível: " + p.getQuantidade());
                return;
            }
            registrarMovimentacao(TipoMovimentacao.SAIDA, qtd, p, obs);
            System.out.println("Saída/Baixa registrada. Novo estoque: " + p.getQuantidade());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void registrarMovimentacao(TipoMovimentacao tipo, int quantidade, Produto produto, String obs) {
        Movimentacao m = new Movimentacao(nextMovId++, tipo, quantidade, produto, obs);
        movimentacoes.add(m);
        if (tipo == TipoMovimentacao.ENTRADA) produto.atualizarQuantidade(quantidade);
        else produto.atualizarQuantidade(-quantidade);
    }

    private void listarMovimentacoes() {
        System.out.println("\n--- Histórico de Movimentações ---");
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentacao registrada.");
            return;
        }
        movimentacoes.forEach(System.out::println);
    }

    private void gerarRelatorios(Scanner sc) {
        System.out.println("1 - Relatorio completo de estoque");
        System.out.println("2 - Relatorio de baixo estoque");
        System.out.println("3 - Relatório de Movimentações (Entrada/Saída)");
        System.out.print("Escolha: ");
        String op = sc.nextLine();
        
        List<Produto> produtosList = new ArrayList<>(produtos.values());
        
        if ("1".equals(op)) {
            Relatorio r = Relatorio.relatorioEstoque(produtosList);
            System.out.println(r.gerar());
        } else if ("2".equals(op)) {
            Relatorio r = Relatorio.relatorioBaixoEstoque(produtosList);
            System.out.println(r.gerar());
        } else if ("3".equals(op)) {
             // Simulação de relatório de movimentações
            System.out.println("\n--- Relatório de Movimentações ---");
            movimentacoes.forEach(m -> System.out.println(m));
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void checarAlertas() {
        List<Produto> baixos = produtos.values().stream()
                .filter(Produto::verificarEstoqueMinimo)
                .collect(Collectors.toList());
        if (!baixos.isEmpty()) {
            System.out.println("\n*** ALERTA: Produtos com baixo estoque ***");
            baixos.forEach(p -> System.out.println(p));
            System.out.println("*** FIM DO ALERTA ***\n");
        }
    }
}