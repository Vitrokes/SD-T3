import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteSocket {
    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PORTA_INICIAL = 12345;

        try {
            for (int i = 0; i < args.length; i++) {
                int porta = PORTA_INICIAL + i;
                Socket socket = new Socket(HOST, porta);
                System.out.println("Conectado ao servidor na porta " + porta + ".");

                BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Digite o login: ");
                String login = teclado.readLine();
                System.out.print("Digite a senha: ");
                String senha = teclado.readLine();

                PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
                saida.println(login);
                saida.println(senha);

                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String resposta = entrada.readLine();
                System.out.println("Resposta do servidor: " + resposta);

                // Processar as solicitações do cliente
                processarSolicitacoes(teclado, saida, entrada, resposta);

                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }

    private static void processarSolicitacoes(BufferedReader teclado, PrintWriter saida, BufferedReader entrada, String resposta) throws IOException {
        if (resposta.equals("Login de cliente realizado com sucesso.")) {
            exibirMenuCliente(teclado, saida, entrada);
        } else if (resposta.equals("Login de funcionário realizado com sucesso.")) {
            exibirMenuFuncionario(teclado, saida, entrada);
        } else {
            System.out.println("Falha no login. Encerrando conexão.");
        }
    }

    private static void exibirMenuCliente(BufferedReader teclado, PrintWriter saida, BufferedReader entrada) throws IOException {
        int opcao;
        do {
            System.out.println("### Menu do Cliente ###");
            System.out.println("1. Listar Carros");
            System.out.println("2. Pesquisar Carro");
            System.out.println("3. Exibir Quantidade de Carros");
            System.out.println("4. Comprar Carro");
            System.out.println("0. Sair");
            System.out.print("Digite a opção desejada: ");
            opcao = Integer.parseInt(teclado.readLine());
            saida.println(opcao);
    
            switch (opcao) {
                case 1:
                    listarCarros(entrada);
                    break;
                case 2:
                    pesquisarCarro(teclado, saida, entrada);
                    break;
                case 3:
                    exibirQuantidadeCarros(entrada);
                    break;
                case 4:
                    comprarCarro(teclado, saida, entrada);
                    break;
                case 0:
                    System.out.println("Encerrando conexão...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private static void exibirMenuFuncionario(BufferedReader teclado, PrintWriter saida, BufferedReader entrada) throws IOException {
        int opcao;
        do {
            System.out.println("### Menu do Funcionário ###");
            System.out.println("1. Listar Carros");
            System.out.println("2. Pesquisar Carro");
            System.out.println("3. Exibir Quantidade de Carros");
            System.out.println("4. Adicionar Carro");
            System.out.println("5. Alterar Atributos de Carro");
            System.out.println("6. Apagar Carro");
            System.out.println("0. Sair");
            System.out.print("Digite a opção desejada: ");
            opcao = Integer.parseInt(teclado.readLine());
            saida.println(opcao);
    
            switch (opcao) {
                case 1:
                    listarCarros(entrada);
                    break;
                case 2:
                    pesquisarCarro(teclado, saida, entrada);
                    break;
                case 3:
                    exibirQuantidadeCarros(entrada);
                    break;
                case 4:
                    adicionarCarro(teclado, saida, entrada);
                    break;
                case 5:
                    alterarAtributosCarro(teclado, saida, entrada);
                    break;
                case 6:
                    apagarCarro(teclado, saida, entrada);
                    break;
                case 0:
                    System.out.println("Encerrando conexão...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private static void listarCarros(BufferedReader entrada) throws IOException {
        String carro;
        System.out.println("Lista de Carros:");
        while (!(carro = entrada.readLine()).equals("FIM_LISTA")) {
            System.out.println(carro); // Exibe cada carro recebido do servidor
        }
    }

    private static void pesquisarCarro(BufferedReader teclado, PrintWriter saida, BufferedReader entrada) throws IOException {
        System.out.print("Digite o nome ou renavam do carro: ");
        String pesquisa = teclado.readLine();
        saida.println(pesquisa);

        String resultado = entrada.readLine();
        System.out.println("Resultado da Pesquisa:");
        System.out.println(resultado);
    }

    private static void exibirQuantidadeCarros(BufferedReader entrada) throws IOException {
        String quantidade = entrada.readLine();
        System.out.println("Quantidade Total de Carros: " + quantidade);
    }

    private static void comprarCarro(BufferedReader teclado, PrintWriter saida, BufferedReader entrada) throws IOException {
        System.out.print("Digite o renavam do carro desejado: ");
        String renavamCarro = teclado.readLine();
        saida.println(renavamCarro);
        
        String mensagem = entrada.readLine();
        System.out.println("Resposta do servidor: " + mensagem);
    }

    private static void adicionarCarro(BufferedReader teclado, PrintWriter saida, BufferedReader entrada) throws IOException {
        System.out.print("Digite o renavam do carro: ");
        String renavam = teclado.readLine();
        System.out.print("Digite o nome do carro: ");
        String nome = teclado.readLine();
        System.out.print("Digite a categoria do carro: ");
        String categoria = teclado.readLine();
        System.out.print("Digite o ano de fabricação do carro: ");
        int anoFabricacao = Integer.parseInt(teclado.readLine());
        System.out.print("Digite a quantidade disponível do carro: ");
        int quantidadeDisponivel = Integer.parseInt(teclado.readLine());
        System.out.print("Digite o preço do carro: ");
        int preco = Integer.parseInt(teclado.readLine());
    
        saida.println(renavam);
        saida.println(nome);
        saida.println(categoria);
        saida.println(anoFabricacao);
        saida.println(quantidadeDisponivel);
        saida.println(preco);
    
        System.out.println(entrada.readLine());
    }
    
    private static void alterarAtributosCarro(BufferedReader teclado, PrintWriter saida, BufferedReader entrada) throws IOException {
        System.out.print("Digite o renavam do carro que deseja alterar: ");
        String renavam = teclado.readLine();
        System.out.print("Digite o novo nome do carro: ");
        String nome = teclado.readLine();
        System.out.print("Digite a nova categoria do carro: ");
        String categoria = teclado.readLine();
        System.out.print("Digite o novo ano de fabricação do carro: ");
        int anoFabricacao = Integer.parseInt(teclado.readLine());
        System.out.print("Digite a nova quantidade disponível do carro: ");
        int quantidadeDisponivel = Integer.parseInt(teclado.readLine());
        System.out.print("Digite o novo preço do carro: ");
        int preco = Integer.parseInt(teclado.readLine());
    
        saida.println(renavam);
        saida.println(nome);
        saida.println(categoria);
        saida.println(anoFabricacao);
        saida.println(quantidadeDisponivel);
        saida.println(preco);
    
        System.out.println(entrada.readLine());
    }
    
    private static void apagarCarro(BufferedReader teclado, PrintWriter saida, BufferedReader entrada) throws IOException {
        System.out.print("Digite o renavam do carro que deseja apagar: ");
        String renavam = teclado.readLine();
    
        saida.println(renavam);
    
        System.out.println(entrada.readLine());
    }
}
