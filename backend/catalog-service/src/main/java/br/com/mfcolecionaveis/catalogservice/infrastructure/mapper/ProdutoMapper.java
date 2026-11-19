package br.com.mfcolecionaveis.catalogservice.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.mfcolecionaveis.catalogservice.infrastructure.persistence.entity.ProdutoEntity;
import br.com.mfcolecionaveis.catalogservice.model.Produto;
import br.com.mfcolecionaveis.catalogservice.model.vo.Ean;
import br.com.mfcolecionaveis.catalogservice.model.vo.Nome;
import br.com.mfcolecionaveis.catalogservice.model.vo.NumeroFigura;
import br.com.mfcolecionaveis.catalogservice.model.vo.Sku;
import br.com.mfcolecionaveis.catalogservice.model.vo.Franquia;

@Component
public class ProdutoMapper {

    public ProdutoEntity toEntity(Produto produto) {
    return new ProdutoEntity(
        produto.getId(),
        produto.getNome().nome(),
        produto.getSku().sku(),
        produto.getEan().ean(),
        produto.getTipoProduto(),
        produto.getNumeroFigura().numeroFigura(),
        produto.getFranquia().franquia(),
        produto.isAtivo()
    );
}

    public Produto toDomain(ProdutoEntity entity) {
    return Produto.reconstituir(
        entity.getId(),
        new Nome(entity.getNome()),
        new Sku(entity.getSku()),
        new Ean(entity.getEan()),
        entity.getTipoProduto(),
        new NumeroFigura(entity.getNumeroFigura()),
        new Franquia(entity.getFranquia()),
        entity.isAtivo()
    );
}
    
}
