package br.com.mfcolecionaveis.catalogservice.model.vo;

public class Ean {
    private String ean;

    public Ean(String ean) {
        if (ean == null || ean.isEmpty()) {
            throw new IllegalArgumentException("EAN não pode ser nulo ou vazio");
        }
        if (ean.length() != 13) {
            throw new IllegalArgumentException("EAN deve ter exatamente 13 caracteres");
        }
        if (!ean.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("EAN contém caracteres inválidos");
        }
        this.ean = ean;
    }

    public String getEan() {
        return ean;
    }
}
