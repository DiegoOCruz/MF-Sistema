package br.com.mfcolecionaveis.catalogservice.model.vo;

public record Ean(String ean) {
    public Ean {
        if (ean == null || ean.isEmpty()) {
            throw new IllegalArgumentException("EAN não pode ser nulo ou vazio");
        }
        if (ean.length() != 13) {
            throw new IllegalArgumentException("EAN deve ter exatamente 13 caracteres");
        }
        if (!ean.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("EAN contém caracteres inválidos");
        }
    }
    public String ean() {
        return ean;
    }
}
