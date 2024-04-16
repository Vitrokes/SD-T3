import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Servidor {
    private static List<Cliente> clientes = new ArrayList<>();
    private static List<Funcionario> funcionarios = new ArrayList<>();
    private static List<Carro> bancoDeDados = new ArrayList<>();

    public static void main(String[] args) {
        final int PORTA_INICIAL = 12345;
        final int NUMERO_PORTAS = 5;

        // Adicionando alguns clientes e funcionários para teste
        clientes.add(new Cliente("cliente1", "senha1"));
        clientes.add(new Cliente("cliente2", "senha2"));
        funcionarios.add(new Funcionario("funcionario1", "senha1"));

        bancoDeDados.add(new Carro("22222222222", "Chevrolet Onix", Categoria.ECONOMICO, 2019, 7, 35000));
        bancoDeDados.add(new Carro("33333333333", "Ford Ka", Categoria.ECONOMICO, 2021, 3, 32000));
        bancoDeDados.add(new Carro("44444444444", "Hyundai HB20", Categoria.ECONOMICO, 2020, 6, 28000));
        bancoDeDados.add(new Carro("55555555555", "Nissan March", Categoria.ECONOMICO, 2019, 4, 27000));
        bancoDeDados.add(new Carro("66666666666", "Ford Ka Sedan", Categoria.INTERMEDIARIO, 2021, 5, 40000));
        bancoDeDados.add(new Carro("77777777777", "Chevrolet Onix Plus", Categoria.INTERMEDIARIO, 2020, 6, 42000));
        bancoDeDados.add(new Carro("88888888888", "Hyundai HB20S", Categoria.INTERMEDIARIO, 2019, 4, 38000));
        bancoDeDados.add(new Carro("99999999999", "Renault Logan", Categoria.INTERMEDIARIO, 2018, 3, 35000));
        bancoDeDados.add(new Carro("10101010101", "Toyota Corolla", Categoria.EXECUTIVO, 2021, 8, 80000));
        bancoDeDados.add(new Carro("11111111111", "Honda Civic", Categoria.EXECUTIVO, 2020, 7, 75000));
        bancoDeDados.add(new Carro("12121212121", "Chevrolet Cruze", Categoria.EXECUTIVO, 2019, 6, 78000));
        bancoDeDados.add(new Carro("13131313131", "Audi A3", Categoria.EXECUTIVO, 2018, 5, 85000));

        try {
            for (int i = 0; i < NUMERO_PORTAS;) {
                int porta = PORTA_INICIAL + i;
                try (ServerSocket servidor = new ServerSocket(porta)) {
                    System.out.println("Servidor iniciado na porta " + porta + " e aguardando conexão...");

                    while (true) {
                        Socket cliente = servidor.accept();
                        System.out.println("Novo cliente conectado: " + cliente.getInetAddress().getHostAddress());

                        // Criar uma nova thread para tratar a conexão com o cliente
                        ThreadCliente threadCliente = new ThreadCliente(cliente);
                        threadCliente.start();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    static class ThreadCliente extends Thread {
        private Socket cliente;
        private BufferedReader entrada;
        private PrintWriter saida;
        private boolean isFuncionario;

        public ThreadCliente(Socket cliente) {
            this.cliente = cliente;
            try {
                this.entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                this.saida = new PrintWriter(cliente.getOutputStream(), true);
            } catch (IOException e) {
                System.err.println("Erro ao obter fluxos de entrada/saída: " + e.getMessage());
            }
        }

        public void run() {
            try {
                // Lógica para autenticar o cliente
                String login = entrada.readLine();
                String senha = entrada.readLine();
                if (autenticarCliente(login, senha)) {
                    isFuncionario = ehFuncionario(login);
                    if (isFuncionario) {
                        saida.println("Login de funcionário realizado com sucesso.");
                    } else {
                        saida.println("Login de cliente realizado com sucesso.");
                    }
                } else {
                    saida.println("Falha na autenticação. Login ou senha incorretos.");
                    return; // Fecha a conexão com o cliente
                }

                int opcao;
                do {
                    opcao = Integer.parseInt(entrada.readLine());
                    System.out.println("Enviada solicitação de opção: " + opcao);

                    switch (opcao) {
                        case 1:
                            listarCarros();
                            break;
                        case 2:
                            pesquisarCarro();
                            break;
                        case 3:
                            exibirQuantidadeCarros();
                            break;
                        case 4:
                            if (isFuncionario) {
                                adicionarCarro();
                            } else {
                                comprarCarro();
                            }
                            break;
                        case 5:
                            if (isFuncionario) {
                                alterarAtributosCarro(entrada);
                            } else {
                                System.out.println("Opção inválida. Tente novamente.");
                            }
                            break;
                        case 6:
                            if (isFuncionario) {
                                apagarCarro(entrada);
                            } else {
                                System.out.println("Opção inválida. Tente novamente.");
                            }
                            break;
                        case 0:
                            System.out.println("Encerrando conexão...");
                            break;
                        default:
                            System.out.println("Opção inválida. Tente novamente.");
                    }
                } while (opcao != 0);
            } catch (IOException e) {
                System.err.println("Erro ao tratar a conexão com o cliente: " + e.getMessage());
            } finally {
                try {
                    cliente.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar conexão com o cliente: " + e.getMessage());
                }
            }
        }

        private boolean autenticarCliente(String login, String senha) {
            // Lógica para autenticar o cliente
            for (Cliente c : clientes) {
                if (c.getLogin().equals(login) && c.getSenha().equals(senha)) {
                    return true;
                }
            }
            for (Funcionario f : funcionarios) {
                if (f.getLogin().equals(login) && f.getSenha().equals(senha)) {
                    return true;
                }
            }
            return false;
        }

        private boolean ehFuncionario(String login) {
            // Lógica para verificar se o login corresponde a um funcionário
            for (Funcionario f : funcionarios) {
                if (f.getLogin().equals(login)) {
                    return true;
                }
            }
            return false;
        }

        private void listarCarros() {
            try {
                PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
                for (Carro carro : bancoDeDados) {
                    saida.println(carro); // Envia cada carro para o cliente
                }
                saida.println("FIM_LISTA"); // Indica o fim da lista para o cliente
            } catch (IOException e) {
                System.err.println("Erro ao enviar a lista de carros: " + e.getMessage());
            }
        }

        private void pesquisarCarro() {
            try {
                String termo = entrada.readLine();
                PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
                Carro carroEncontrado = null;
                for (Carro carro : bancoDeDados) {
                    if (carro.getRenavam().equals(termo) || carro.getNome().equalsIgnoreCase(termo)) {
                        carroEncontrado = carro;
                        break;
                    }
                }
                if (carroEncontrado != null) {
                    saida.println(carroEncontrado.toString());
                } else {
                    saida.println("Carro não encontrado.");
                }
            } catch (IOException e) {
                System.err.println("Erro ao pesquisar carro: " + e.getMessage());
            }
        }

        private void exibirQuantidadeCarros() {
            try {
                PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
                saida.println(bancoDeDados.size());
            } catch (IOException e) {
                System.err.println("Erro ao exibir quantidade de carros: " + e.getMessage());
            }
        }

        private void adicionarCarro() {
            try {
                String renavam = entrada.readLine();
                String nome = entrada.readLine();
                String categoria = entrada.readLine();
                int anoFabricacao = Integer.parseInt(entrada.readLine());
                int quantidadeDisponivel = Integer.parseInt(entrada.readLine());
                int preco = Integer.parseInt(entrada.readLine());
                Categoria categoriaEnum = Categoria.valueOf(categoria.toUpperCase());
                Carro novoCarro = new Carro(renavam, nome, categoriaEnum, anoFabricacao, quantidadeDisponivel, preco);
                bancoDeDados.add(novoCarro);
                saida.println("Carro adicionado com sucesso.");
            } catch (IOException e) {
                System.err.println("Erro ao adicionar carro: " + e.getMessage());
            }
        }

        private void comprarCarro() {
            try {
                String renavam = entrada.readLine();
                PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
                for (Carro carro : bancoDeDados) {
                    if (carro.getRenavam().equals(renavam)) {
                        int novaQuantidade = carro.getQuantidadeDisponivel() - 1;
                        carro.setQuantidadeDisponivel(novaQuantidade);
                        saida.println("Carro comprado com sucesso: " + carro.getNome());
                        return;
                    }
                }
                saida.println("Carro não encontrado.");
            } catch (IOException e) {
                System.err.println("Erro ao comprar carro: " + e.getMessage());
            }
        }

        private void apagarCarro(BufferedReader entrada) {
            try {
                String termo = entrada.readLine();
                boolean removido = false;
                Iterator<Carro> iter = bancoDeDados.iterator();
                while (iter.hasNext()) {
                    Carro carro = iter.next();
                    if (carro.getRenavam().equals(termo) || carro.getNome().equalsIgnoreCase(termo)) {
                        iter.remove();
                        removido = true;
                        break;
                    }
                }
                if (removido) {
                    saida.println("Carro removido com sucesso.");
                } else {
                    saida.println("Carro não encontrado.");
                }
            } catch (IOException e) {
                System.err.println("Erro ao apagar carro: " + e.getMessage());
            }
        }
        
        private void alterarAtributosCarro(BufferedReader entrada) {
            try {
                String renavam = entrada.readLine();
                String nome = entrada.readLine();
                String categoriaStr = entrada.readLine();
                int anoFabricacao = Integer.parseInt(entrada.readLine());
                int quantidadeDisponivel = Integer.parseInt(entrada.readLine());
                int preco = Integer.parseInt(entrada.readLine());
                Categoria categoriaEnum = Categoria.valueOf(categoriaStr.toUpperCase());
                
                for (Carro carro : bancoDeDados) {
                    if (carro.getRenavam().equals(renavam)) {
                        carro.setNome(nome);
                        carro.setCategoria(categoriaEnum);
                        carro.setAnoFabricacao(anoFabricacao);
                        carro.setQuantidadeDisponivel(quantidadeDisponivel);
                        carro.setPreco(preco);
                        saida.println("Atributos do carro alterados com sucesso.");
                        return;
                    }
                }
                saida.println("Carro não encontrado.");
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Erro ao alterar atributos do carro: " + e.getMessage());
            }
        }
    }
}
