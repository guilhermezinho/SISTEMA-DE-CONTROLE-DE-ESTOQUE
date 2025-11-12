
package estoque;

import Models.Estoque;
import Models.Funcionario;
import Models.Produto;
import Models.Relatorio;
import estoque.repository.EstoqueRepository;
import  Service.EstoqueService;

import java.util.*;
import java.util.List;

// A classe Main não é mais serializável
public class Main {
    
    // O Controller referencia o Serviço
    private final EstoqueService service;
    private final EstoqueRepository repository; // Mantido para o save
    
    public static void main(String[] args) {
        // 1. Carrega o Repositório de Dados
        EstoqueRepository repo = EstoqueRepository.carregar();
        
        // 2. Cria o Serviço de Negócio
        EstoqueService service = new EstoqueService(repo);
        
        // 3. Inicia o Controlador (Main)
        Main app = new Main(service, repo); 
        app.loop();
    }

    public Main(EstoqueService service, EstoqueRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    private void loop() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Sistema de Controle de Estoque (Console) ===");
        
        Funcionario atual = login(sc); 
        
        if (atual == null) {
            System.out.println("Login falhou. Saindo.");
            return;
        }
        System.out.println("Bem-vindo, " + atual.getNome() + " (Funcionário)");
        
        boolean sair = false;
        while (!sair) {
            mostrarMenu();
            String opc = sc.nextLine().trim();
            switch (opc) {
                case "1": listarProdutos(); break;
                case "2": cadastrarProduto(sc); break;
                case "3": editarProduto(sc); break;
                case "4": excluirProduto(sc); break;
                
                case "5": registrarEntrada(sc); break;
                case "6": registrarSaida(sc); break; 
                case "7": gerarRelatorios(sc); break;
                case "8": listarMovimentacoes(); break;
                
                case "9": repository.salvar(); System.out.println("Dados salvos."); break;
                case "0": repository.salvar(); sair = true; break;
                default: System.out.println("Opção inválida."); break;
            }
            checarAlertas();
        }
        System.out.println("Encerrando sistema. Até mais!");
        sc.close();
    }

    // --- Métodos de Interface (Foco em I/O e Serviço) ---
    
    private Funcionario login(Scanner sc) {
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        return service.fazerLogin(login, senha); 
    }

    private void mostrarMenu() {
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

    private void listarProdutos() {
        System.out.println("\n--- Produtos ---");
        List<Produto> produtos = service.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        produtos.forEach(System.out::println);
        
        double total = service.calcularValorTotalEstoque();
        System.out.printf("\n*** VALOR TOTAL DE CUSTO DO ESTOQUE: R$ %.2f ***\n", total);
        System.out.println("Total de itens distintos em estoque: " + produtos.size());
    }

    private void cadastrarProduto(Scanner sc) {
        try {
            System.out.print("Nome: "); String nome = sc.nextLine();
            System.out.print("Descricao: "); String desc = sc.nextLine();
            System.out.print("Categoria: "); String cat = sc.nextLine();
            System.out.print("Quantidade inicial: "); int qtd = Integer.parseInt(sc.nextLine());
            System.out.print("Valor unitario de CUSTO: "); double val = Double.parseDouble(sc.nextLine());
            System.out.print("Estoque minimo (alerta): "); int min = Integer.parseInt(sc.nextLine());
            
            Produto p = service.cadastrarProduto(nome, desc, cat, qtd, val, min);
            System.out.println("Produto cadastrado: " + p);
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato: Certifique-se de inserir números válidos.");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void editarProduto(Scanner sc) {
        try {
            System.out.print("Codigo do produto a editar: "); int cod = Integer.parseInt(sc.nextLine());
            Produto p = service.buscarProduto(cod);
            if (p == null) { System.out.println("Produto nao encontrado."); return; }
            
            System.out.println("Produto atual: " + p);
            
            // Lendo valores (vazios são tratados no Service)
            System.out.print("Novo nome (enter para manter): "); String nome = sc.nextLine(); 
            System.out.print("Nova descricao (enter p manter): "); String desc = sc.nextLine(); 
            System.out.print("Nova categoria (enter p manter): "); String cat = sc.nextLine();
            
            // Lendo números com tratamento de enter (vazio = null)
            Double val = null;
            System.out.print("Novo valor de CUSTO (enter p manter): "); String vals = sc.nextLine(); 
            if (!vals.isEmpty()) val = Double.parseDouble(vals);
            
            Integer min = null;
            System.out.print("Novo estoque minimo (enter p manter): "); String mins = sc.nextLine(); 
            if (!mins.isEmpty()) min = Integer.parseInt(mins);
            
            service.editarProduto(cod, nome, desc, cat, val, min);
            System.out.println("Produto atualizado: " + service.buscarProduto(cod));
            
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato: Certifique-se de inserir números válidos.");
        } catch (Exception e) {
            System.out.println("Erro ao editar: " + e.getMessage());
        }
    }

    private void excluirProduto(Scanner sc) {
        try {
            System.out.print("Codigo do produto a remover: "); int cod = Integer.parseInt(sc.nextLine());
            if (service.excluirProduto(cod)) {
                System.out.println("Produto removido.");
            } else {
                System.out.println("Produto nao encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato: Certifique-se de inserir números válidos.");
        }
    }

    private void registrarEntrada(Scanner sc) {
        try {
            System.out.print("Codigo do produto: "); int cod = Integer.parseInt(sc.nextLine());
            System.out.print("Quantidade entrada (compra/reposição): "); int qtd = Integer.parseInt(sc.nextLine());
            System.out.print("Observacao (Ex: Nota Fiscal 123): "); String obs = sc.nextLine();
            
            Produto p = service.registrarEntrada(cod, qtd, obs);
            System.out.println("Entrada registrada. Novo estoque: " + p.getQuantidade());
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato: Certifique-se de inserir números válidos.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void registrarSaida(Scanner sc) {
        try {
            System.out.print("Codigo do produto: "); int cod = Integer.parseInt(sc.nextLine());
            System.out.print("Quantidade saída (baixa/consumo): "); int qtd = Integer.parseInt(sc.nextLine());
            System.out.print("Motivo da baixa (Ex: Uso interno, Descarte, Perda): "); String obs = sc.nextLine();
            
            Produto p = service.registrarSaida(cod, qtd, obs);
            System.out.println("Saída/Baixa registrada. Novo estoque: " + p.getQuantidade());
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato: Certifique-se de inserir números válidos.");
        } catch (Exception e) {
            // O serviço lança a exceção se a quantidade for insuficiente
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarMovimentacoes() {
        System.out.println("\n--- Histórico de Movimentações ---");
        List<Movimentacao> movimentacoes = service.listarMovimentacoes();
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
        
        Relatorio r = service.gerarRelatorio(op);
        
        if (r != null) {
            System.out.println(r.gerar());
        } else if ("3".equals(op)) {
            // Se for relatório de movimentações, imprime direto do service/repository
            System.out.println("\n--- Relatório de Movimentações ---");
            listarMovimentacoes(); 
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void checarAlertas() {
        List<Produto> baixos = service.checarAlertas();
        if (!baixos.isEmpty()) {
            System.out.println("\n*** ALERTA: Produtos com baixo estoque ***");
            baixos.forEach(p -> System.out.println(p));
            System.out.println("*** FIM DO ALERTA ***\n");
        }
    }
}