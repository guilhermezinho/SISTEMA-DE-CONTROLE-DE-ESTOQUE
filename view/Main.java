package estoque;

import estoque.models.*;
import estoque.models.Movimentacao.TipoMovimentacao;
import estoque.persistence.Repositorio;

import java.util.*;
import java.util.stream.Collectors;

public class Main implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Integer, Produto> produtos = new HashMap<>();
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private int nextProdutoId = 1;
    private int nextMovId = 1;
    private int nextUserId = 1;
    private int nextClienteId = 1;

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
        if (usuarios.isEmpty()) {
            usuarios.add(new Administrador(nextUserId++, "Admin", "admin", "1234"));
            usuarios.add(new Funcionario(nextUserId++, "Joao", "joao", "1234"));
        }
        if (produtos.isEmpty()) {
            adicionarProduto("Parafuso", "Parafuso M4", "Ferramentas", 100, 0.10, 10);
            adicionarProduto("Cabo USB", "Cabo USB-C 1m", "Cabos", 20, 15.0, 5);
        }
    }

    private void loop() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Sistema de Controle de Estoque (Console) ===");
        Usuario atual = login(sc);
        if (atual == null) {
            System.out.println("Login falhou. Saindo.");
            return;
        }
        System.out.println("Bem-vindo, " + atual.getNome() + " (" + atual.getRole() + ")");
        boolean sair = false;
        while (!sair) {
            mostrarMenu(atual);
            String opc = sc.nextLine().trim();
            switch (opc) {
                case "1": listarProdutos(); break;
                case "2": if (permitido(atual)) cadastrarProdutoInteractive(sc); else acessoNegado(); break;
                case "3": if (permitido(atual)) editarProdutoInteractive(sc); else acessoNegado(); break;
                case "4": if (permitido(atual)) excluirProdutoInteractive(sc); else acessoNegado(); break;
                case "5": registrarEntradaInteractive(sc, atual); break;
                case "6": registrarSaidaInteractive(sc, atual); break;
                case "7": gerarRelatoriosInteractive(sc); break;
                case "8": listarMovimentacoes(); break;
                case "9": salvarEstado(); System.out.println("Dados salvos."); break;
                case "0": salvarEstado(); sair = true; break;
                default: System.out.println("Opção inválida."); break;
            }
            checarAlertas();
        }
        System.out.println("Encerrando sistema. Até mais!");
    }

    private Usuario login(Scanner sc) {
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        for (Usuario u : usuarios) {
            if (u.fazerLogin(login, senha)) return u;
        }
        return null;
    }

    private boolean permitido(Usuario u) {
        return u instanceof Administrador;
    }

    private void acessoNegado() {
        System.out.println("Acesso negado: permissão insuficiente.");
    }

    private void mostrarMenu(Usuario u) {
        System.out.println("\n--- Menu ---");
        System.out.println("1 - Listar Produtos");
        System.out.println((u instanceof Administrador ? "2 - Cadastrar Produto" : "2 - (Requer admin)") );
        System.out.println((u instanceof Administrador ? "3 - Editar Produto" : "3 - (Requer admin)") );
        System.out.println((u instanceof Administrador ? "4 - Excluir Produto" : "4 - (Requer admin)") );
        System.out.println("5 - Registrar Entrada");
        System.out.println("6 - Registrar Saída");
        System.out.println("7 - Gerar Relatórios");
        System.out.println("8 - Listar Movimentações");
        System.out.println("9 - Salvar");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    // CRUD produtos (internos)
    private Produto adicionarProduto(String nome, String descricao, String categoria,
                                     int quantidade, double valorUnitario, int estoqueMinimo) {
        Produto p = new Produto(nextProdutoId++, nome, descricao, categoria, quantidade, valorUnitario, estoqueMinimo);
        produtos.put(p.getCodigo(), p);
        return p;
    }

    private boolean editarProduto(int codigo, String nome, String descricao, String categoria,
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
        System.out.printf("Valor total do estoque: R$ %.2f\n", total);
    }

    // Interativos
    private void cadastrarProdutoInteractive(Scanner sc) {
        try {
            System.out.print("Nome: "); String nome = sc.nextLine();
            System.out.print("Descricao: "); String desc = sc.nextLine();
            System.out.print("Categoria: "); String cat = sc.nextLine();
            System.out.print("Quantidade: "); int qtd = Integer.parseInt(sc.nextLine());
            System.out.print("Valor unitario: "); double val = Double.parseDouble(sc.nextLine());
            System.out.print("Estoque minimo: "); int min = Integer.parseInt(sc.nextLine());
            Produto p = adicionarProduto(nome, desc, cat, qtd, val, min);
            System.out.println("Produto cadastrado: " + p);
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void editarProdutoInteractive(Scanner sc) {
        try {
            System.out.print("Codigo do produto a editar: "); int cod = Integer.parseInt(sc.nextLine());
            Produto p = produtos.get(cod);
            if (p == null) { System.out.println("Produto nao encontrado."); return; }
            System.out.println("Produto atual: " + p);
            System.out.print("Novo nome (enter para manter): "); String nome = sc.nextLine(); if (nome.isEmpty()) nome = null;
            System.out.print("Nova descricao (enter p manter): "); String desc = sc.nextLine(); if (desc.isEmpty()) desc = null;
            System.out.print("Nova categoria (enter p manter): "); String cat = sc.nextLine(); if (cat.isEmpty()) cat = null;
            System.out.print("Nova quantidade (enter p manter): "); String qtds = sc.nextLine(); Integer qtd = qtds.isEmpty()? null: Integer.parseInt(qtds);
            System.out.print("Novo valor unitario (enter p manter): "); String vals = sc.nextLine(); Double val = vals.isEmpty()? null: Double.parseDouble(vals);
            System.out.print("Novo estoque minimo (enter p manter): "); String mins = sc.nextLine(); Integer min = mins.isEmpty()? null: Integer.parseInt(mins);
            editarProduto(cod, nome, desc, cat, qtd, val, min);
            System.out.println("Produto atualizado: " + produtos.get(cod));
        } catch (Exception e) {
            System.out.println("Erro ao editar: " + e.getMessage());
        }
    }

    private void excluirProdutoInteractive(Scanner sc) {
        try {
            System.out.print("Codigo do produto a remover: "); int cod = Integer.parseInt(sc.nextLine());
            if (removerProduto(cod)) System.out.println("Produto removido.");
            else System.out.println("Produto nao encontrado.");
        } catch (Exception e) {
            System.out.println("Erro ao remover: " + e.getMessage());
        }
    }

    private void registrarEntradaInteractive(Scanner sc, Usuario u) {
        try {
            System.out.print("Codigo do produto: "); int cod = Integer.parseInt(sc.nextLine());
            Produto p = produtos.get(cod);
            if (p == null) { System.out.println("Produto nao encontrado."); return; }
            System.out.print("Quantidade entrada: "); int qtd = Integer.parseInt(sc.nextLine());
            System.out.print("Observacao: "); String obs = sc.nextLine();
            registrarMovimentacao(TipoMovimentacao.ENTRADA, qtd, p, obs);
            System.out.println("Entrada registrada.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void registrarSaidaInteractive(Scanner sc, Usuario u) {
        try {
            System.out.print("Codigo do produto: "); int cod = Integer.parseInt(sc.nextLine());
            Produto p = produtos.get(cod);
            if (p == null) { System.out.println("Produto nao encontrado."); return; }
            System.out.print("Quantidade saida: "); int qtd = Integer.parseInt(sc.nextLine());
            System.out.print("Observacao: "); String obs = sc.nextLine();
            if (qtd > p.getQuantidade()) {
                System.out.println("Quantidade insuficiente em estoque.");
                return;
            }
            registrarMovimentacao(TipoMovimentacao.SAIDA, qtd, p, obs);
            System.out.println("Saida registrada.");
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
        System.out.println("\n--- Movimentacoes ---");
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentacao registrada.");
            return;
        }
        movimentacoes.forEach(System.out::println);
    }

    private void gerarRelatoriosInteractive(Scanner sc) {
        System.out.println("1 - Relatorio completo de estoque");
        System.out.println("2 - Relatorio de baixo estoque");
        System.out.print("Escolha: ");
        String op = sc.nextLine();
        if ("1".equals(op)) {
            Relatorio r = Relatorio.relatorioEstoque(new ArrayList<>(produtos.values()));
            System.out.println(r.gerar());
        } else if ("2".equals(op)) {
            Relatorio r = Relatorio.relatorioBaixoEstoque(new ArrayList<>(produtos.values()));
            System.out.println(r.gerar());
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
