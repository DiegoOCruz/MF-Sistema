package br.com.mfcolecionaveis.catalogservice.model.vo;

public class Sku {
    private String sku;

    public Sku(String sku) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU não pode ser nulo ou vazio");
        }
        if (sku.length() > 50) {
            throw new IllegalArgumentException("SKU não pode ter mais de 50 caracteres");
        }
        if (!sku.matches("^[a-zA-Z0-9\\-]+$")) {
            throw new IllegalArgumentException("SKU contém caracteres inválidos");
        }
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }
}
