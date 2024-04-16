public class Funcionario {
    private String login;
    private String senha;

    public Funcionario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    // Métodos para acessar os atributos
    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }
}