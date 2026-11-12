package br.com.mfcolecionaveis.catalogservice.model.vo;

public class Nome {
    private String nome;

    public Nome(String nome) {
        if (nome == null || nome.isEmpty() || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        if (nome.length() > 100) {
            throw new IllegalArgumentException("Nome não pode ter mais de 100 caracteres");
        }
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
