public class Cliente {
    private String login;
    private String senha;

    public Cliente(String login, String senha) {
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