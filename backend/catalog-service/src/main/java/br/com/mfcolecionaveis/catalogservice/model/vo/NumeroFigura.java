package br.com.mfcolecionaveis.catalogservice.model.vo;

public record NumeroFigura(String numeroFigura) {
    public NumeroFigura {
        if (numeroFigura == null || numeroFigura.isEmpty()) {
            throw new IllegalArgumentException("Número da figura não pode ser nulo ou vazio");
        }
        if (numeroFigura.length() > 4) {
            throw new IllegalArgumentException("Número da figura não pode ter mais de 4 caracteres");
        }
        if (!numeroFigura.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Número da figura contém caracteres inválidos");
        }
    }

    public String numeroFigura() {
        return numeroFigura;
    }
}
