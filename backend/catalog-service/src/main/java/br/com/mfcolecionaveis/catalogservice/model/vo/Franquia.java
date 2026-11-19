package br.com.mfcolecionaveis.catalogservice.model.vo;

public record Franquia(String franquia) {
    public Franquia {
        if (franquia == null || franquia.isBlank()) {
            throw new IllegalArgumentException("Franquia não pode ser nula ou vazia");
        }
        if (franquia.length() > 100) {
            throw new IllegalArgumentException("Franquia não pode ter mais de 100 caracteres");
        }
    }
}
