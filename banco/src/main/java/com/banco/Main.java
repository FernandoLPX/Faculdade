package com.banco;

import java.util.Scanner;

import com.banco.dados.JsonFileManager;

public class Main {

    public static void main(String[] args) {

        // Criar um Scanner para poder fazer a entrada de dados
        var sc = new Scanner(System.in);

        // Opcional - Somente insere alguns dados de clientes no JSON para testes
        // popularDB();

        // Criar a instância do banco para carregar os dados do database em memória
        Banco banco = Banco.getInstance();
        // Opcional - Exibir os clientes carregados para debug
        // banco.exibirClientesEmContasBancarias();

        System.out.println(" _________________________________________ ");
        System.out.println("|                                         |");
        System.out.println("| Bem vindo ao sistema do " + banco.getNome() + ". |");
        System.out.println("|_________________________________________|");
        System.out.println("|                                         |");
        System.out.print("| Digite o número do seu CPF: ");
        long cpf = sc.nextLong();

        // Verifica se possui conta ou não
        System.out.println("|                                         |");
        System.out.println("|   Verificando CPF na base de dados...   |");
        System.out.println("|_________________________________________|");
        Cliente cliente = banco.verificarCliente(cpf);

        if (cliente != null) {
            // Se possui conta então mostra o nome do cliente no console e
            // vai para as operações
            mostrarNomeConsole(cliente.getNome());            
            iniciarOperacao(sc, banco, cliente);
        } else {
            // Se não possui conta, então oferece a criação de uma
            System.out.println("|                                         |");
            System.out.println("|     Você ainda não possui uma conta.    |");
            System.out.print("|     Gostaria de criar uma? [y/n]: ");
            String opcao = sc.next();

            if (opcao.equals("y")) {
                // Se quer criar uma conta, basta inserir o nome que a criação
                // será feita somente com o nome e o cpf já digitado anteriormente
                System.out.print("| Insira seu sobrenome: ");
                String sobrenomeConta = sc.next();
                System.out.print("| Insira seu nome: ");
                String nomeConta = sc.next();

                // System.out.println("Seus dados estão corretos? y/n");
                // System.out.println("Nome: " + nomeConta);
                // System.out.println("CPF: " + cpf);
                // opcao = sc.next();

                cliente = banco.cadastrarCliente(nomeConta, sobrenomeConta, cpf);
                System.out.println("|                                         |");
                System.out.println("|        Conta criada com sucesso!        |");
                System.out.println("|     Agora você é nosso novo cliente.    |");
                iniciarOperacao(sc, banco, cliente);
            } else {
                // Se não possui conta e não quer ter uma, então não pode fazer nada, até logo!
                System.out.println("|                                         |");
                System.out.println("|              OK, Até logo!              |");
                System.out.println("|_________________________________________|");
            }
        }
        sc.close();
    }

    private static void iniciarOperacao(Scanner sc, Banco banco, Cliente cliente) {
        boolean controle = true;
        do {
            System.out.println("|_________________________________________|");
            System.out.println("|                                         |");
            System.out.println("|           O que deseja fazer?           |");
            System.out.println("|                                         |");
            System.out.println("| 1 - Verificar saldo                     |");
            System.out.println("| 2 - Fazer um depósito                   |");
            System.out.println("| 3 - Fazer um saque                      |");
            System.out.println("| 4 - Sair                                |");
            System.out.println("|_________________________________________|");

            System.out.println("|                                         |");
            System.out.print("| Digite o número de uma das opções: ");
            int opcao = sc.nextInt();
            // System.out.print("    |");
            System.out.println("|                                         |");

            // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // System.out.print("| Digite o número de uma das opções: ");
            // System.out.flush(); // Garantir que a saída seja exibida imediatamente
            // String opcaoStr = "";
            // try {
            //     // Ler a entrada do usuário como uma string
            //     opcaoStr = reader.readLine();
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
            // int opcao = Integer.parseInt(opcaoStr); // Converter a string para inteiro
            // System.out.print("    |");

            // System.out.print("| Digite o número de uma das opções: ");
            // String opcaoStr = System.console().readLine();
            // int opcao = Integer.parseInt(opcaoStr.trim());
            // System.out.print("    |");

            // LineReader lineReader = LineReaderBuilder.builder().build();        
            // System.out.print("| Digite o número de uma das opções: ");
            // String opcaoStr = lineReader.readLine();
            // int opcao = Integer.parseInt(opcaoStr.trim());
            // System.out.print("    |");

            // int opcao = 0;
            // try {
            //     // Criar um terminal
            //     Terminal terminal = TerminalBuilder.builder().build();

            //     // Criar um leitor de linha
            //     LineReader lineReader = LineReaderBuilder.builder()
            //             .terminal(terminal)
            //             .completer(new NullCompleter())
            //             .build();

            //     // Ler entrada do usuário
            //     String input = lineReader.readLine("| Digite o número de uma das opções: ");

            //     // Imprimir na mesma linha
            //     terminal.writer().print("    |");
            //     terminal.writer().flush();

            //     // Processar entrada
            //     opcao = Integer.parseInt(input.trim());

            //     // Fazer algo com a entrada
            //     System.out.println("\nOpção escolhida: " + opcao);

            //     // Fechar o terminal
            //     terminal.close();
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }

            // System.out.flush();
            // System.out.println("");
            switch (opcao) {
                case 1:
                    System.out.println("| Seu saldo é = R$" + banco.verificarSaldo(cliente));
                    break;
                case 2:
                    System.out.print("| Digite o valor que deseja depositar:");
                    float valorDeposito = sc.nextFloat();
                    banco.depositar(cliente, valorDeposito);
                    System.out.println(" _________________________________________ ");
                    System.out.println("|                                         |");
                    System.out.println("| O Depósito de R$" + valorDeposito + " foi realizado.\r");
                    System.out.println("| Seu saldo atual = R$" + banco.verificarSaldo(cliente));
                    break;
                case 3:
                    double saldo = banco.verificarSaldo(cliente);
                    System.out.println("| Seu saldo é = R$" + saldo);
                    System.out.print("| Digite o valor que deseja sacar: R$");
                    float valorSaque = sc.nextFloat();
                    boolean sacou = banco.sacar(cliente, valorSaque);
                    System.out.println("|_________________________________________|");
                    System.out.println("|                                         |");
                    if (sacou)
                        System.out.println("| O saque de R$" + valorSaque + " foi realizado.");
                    else
                        System.out.println("| ********* SALDO  INSUFICIENTE ********* |");
                    System.out.println("| Seu saldo atual = R$" + banco.verificarSaldo(cliente));
                    break;
                default:
                    System.out.println("| Até logo!                               |");
                    System.out.println("|_________________________________________|");
                    controle = false;
                    break;
            }
        } while (controle);
    }
    
    private static void mostrarNomeConsole(String nome) {
        System.out.println("|                                         |");
        System.out.println("|                   Olá                   |");
        int espacos = 41 - nome.length();
            System.out.print("|");
            if (espacos % 2 != 0)
                System.out.print(" ");
            for (int i = 0;i<espacos/2; i++) {
                System.out.print(" ");
            }
            System.out.print(nome);
            for (int i = 0; i < espacos / 2; i++) {
                System.out.print(" ");
            }
            System.out.println("|");
    }

    private static void popularDB() {
        Cliente cliente1 = new Cliente("Cliente_1", "Sobrenome_Cli_1", 12345678900L);
        cliente1.getConta().setSaldo(1000);
        Cliente cliente2 = new Cliente("Cliente_2", "Sobrenome_Cli_2", 98765432100L);
        cliente2.getConta().setSaldo(9.99);
        Cliente cliente3 = new Cliente("Cliente_3", "Sobrenome_Cli_3", 123L);
        cliente3.getConta().setSaldo(0);

        JsonFileManager.adicionarCliente(cliente1);
        JsonFileManager.adicionarCliente(cliente2);
        JsonFileManager.adicionarCliente(cliente3);

        // Cliente[] clientes = new Cliente[3];
        // clientes[0] = cliente1;
        // clientes[1] = cliente2;
        // clientes[2] = cliente3;
        // JsonFileManager.criarArquivo(clientes);

        cliente1 = null;
        cliente2 = null;
        cliente3 = null;
    }
}
