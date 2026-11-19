package br.com.mfcolecionaveis.catalogservice.dto;
import java.util.UUID;

public record ProdutoResponseDTO(
    UUID id,
    String nome,
    String sku,
    String ean,
    String franquia,
    String tipoProduto,
    String numeroFigura,
    boolean ativo
) {
}