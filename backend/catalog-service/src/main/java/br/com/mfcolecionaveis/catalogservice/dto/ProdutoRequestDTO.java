package br.com.mfcolecionaveis.catalogservice.dto;

public record ProdutoRequestDTO(
    String nome,
    String sku,
    String ean,
    String franquia,
    String tipoProduto,
    String numeroFigura
) {
}
