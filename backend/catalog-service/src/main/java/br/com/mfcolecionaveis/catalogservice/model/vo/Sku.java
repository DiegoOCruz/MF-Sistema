package br.com.mfcolecionaveis.catalogservice.model.vo;

public record Sku(String sku) {
    public Sku {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU não pode ser nulo ou vazio");
        }
        if (sku.length() > 50) {
            throw new IllegalArgumentException("SKU não pode ter mais de 50 caracteres");
        }
        if (!sku.matches("^[a-zA-Z0-9\\-]+$")) {
            throw new IllegalArgumentException("SKU contém caracteres inválidos");
        }
    }

    public String sku() {
        return sku;
    }
}
