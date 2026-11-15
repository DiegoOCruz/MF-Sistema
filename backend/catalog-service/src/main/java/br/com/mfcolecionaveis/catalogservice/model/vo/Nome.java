package br.com.mfcolecionaveis.catalogservice.model.vo;

public record Nome(String nome) {
    public Nome {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        if (nome.length() > 100) {
            throw new IllegalArgumentException("Nome não pode ter mais de 100 caracteres");
        }
    }
}