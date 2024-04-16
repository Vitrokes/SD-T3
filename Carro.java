public class Carro {
    private String renavam;
    private String nome;
    private Categoria categoria;
    private int anoFabricacao;
    private int quantidadeDisponivel;
    private int preco;

    public Carro(String renavam, String nome, Categoria categoria, int anoFabricacao, int quantidadeDisponivel, int preco) {
        this.renavam = renavam;
        this.nome = nome;
        this.categoria = categoria;
        this.anoFabricacao = anoFabricacao;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.preco = preco;
    }

    @Override
        public String toString() {
            return "Renavam: " + renavam + ", Nome: " + nome + ", Categoria: " + categoria +
            ", Ano de Fabricação: " + anoFabricacao + ", Quantidade Disponível: " +
            quantidadeDisponivel + ", Preço: R$ " + preco;
        }

    // Métodos para acessar e modificar os atributos
    public String getRenavam() {
        return renavam;
    }

    public String getNome() {
        return nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public int getPreco() {
        return preco;
    }

    public void setNome(String novoNome) {
        this.nome = novoNome;
    }
    
    public void setCategoria(Categoria novaCategoria) {
        this.categoria = novaCategoria;
    }
    
    public void setAnoFabricacao(int novoAnoFabricacao) {
        this.anoFabricacao = novoAnoFabricacao;
    }
    
    public void setPreco(int novoPreco) {
        this.preco = novoPreco;
    }
}

// Enumeração para representar as categorias dos carros
enum Categoria {
    ECONOMICO("EC"),
    INTERMEDIARIO("IN"),
    EXECUTIVO("EX");

    private final String abreviacao;

    Categoria(String abreviacao) {
        this.abreviacao = abreviacao;
    }

    public String getAbreviacao() {
        return abreviacao;
    }
}