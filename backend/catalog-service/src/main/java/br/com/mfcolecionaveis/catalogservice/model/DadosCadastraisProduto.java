package br.com.mfcolecionaveis.catalogservice.model;

import java.util.Objects;

import br.com.mfcolecionaveis.catalogservice.model.vo.Ean;
import br.com.mfcolecionaveis.catalogservice.model.vo.Franquia;
import br.com.mfcolecionaveis.catalogservice.model.vo.Nome;
import br.com.mfcolecionaveis.catalogservice.model.vo.NumeroFigura;
import br.com.mfcolecionaveis.catalogservice.model.vo.Sku;

public record DadosCadastraisProduto(
    Nome nome,
    Sku sku,
    Ean ean,
    TipoProduto tipoProduto,
    NumeroFigura numeroFigura,
    Franquia franquia
) {
    public DadosCadastraisProduto {
    Objects.requireNonNull(nome, "Nome não pode ser nulo");
    Objects.requireNonNull(sku, "SKU não pode ser nulo");
    Objects.requireNonNull(ean, "EAN não pode ser nulo");
    Objects.requireNonNull(tipoProduto, "Tipo do produto não pode ser nulo");
    Objects.requireNonNull(numeroFigura, "Número da figura não pode ser nulo");
    Objects.requireNonNull(franquia, "Franquia não pode ser nula");
}
}
