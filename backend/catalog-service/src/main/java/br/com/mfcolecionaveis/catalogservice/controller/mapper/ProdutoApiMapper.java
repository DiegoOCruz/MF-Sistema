package br.com.mfcolecionaveis.catalogservice.controller.mapper;

import org.springframework.stereotype.Component;

import br.com.mfcolecionaveis.catalogservice.dto.ProdutoRequestDTO;
import br.com.mfcolecionaveis.catalogservice.dto.ProdutoResponseDTO;
import br.com.mfcolecionaveis.catalogservice.model.DadosCadastraisProduto;
import br.com.mfcolecionaveis.catalogservice.model.Produto;
import br.com.mfcolecionaveis.catalogservice.model.TipoProduto;
import br.com.mfcolecionaveis.catalogservice.model.vo.Ean;
import br.com.mfcolecionaveis.catalogservice.model.vo.Franquia;
import br.com.mfcolecionaveis.catalogservice.model.vo.Nome;
import br.com.mfcolecionaveis.catalogservice.model.vo.NumeroFigura;
import br.com.mfcolecionaveis.catalogservice.model.vo.Sku;

@Component
public class ProdutoApiMapper {

    public Produto toDomain(ProdutoRequestDTO dto) {
        return new Produto(
            new Nome(dto.nome()),
            new Sku(dto.sku()),
            new Ean(dto.ean()),
            TipoProduto.valueOf(dto.tipoProduto()),
            new NumeroFigura(dto.numeroFigura()),
            new Franquia(dto.franquia()),
            true
        );
    }

    public ProdutoResponseDTO toResponse(Produto produto) {
        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getNome().nome(),
            produto.getSku().sku(),
            produto.getEan().ean(),
            produto.getFranquia().franquia(),
            produto.getTipoProduto().name(),
            produto.getNumeroFigura().numeroFigura(),
            produto.isAtivo()
        );
    }

    public DadosCadastraisProduto toDadosAtualizacaoProduto(ProdutoRequestDTO dto){
        return new DadosCadastraisProduto(
            new Nome(dto.nome()),
            new Sku(dto.sku()),
            new Ean(dto.ean()),
            TipoProduto.valueOf(dto.tipoProduto()),
            new NumeroFigura(dto.numeroFigura()),
            new Franquia(dto.franquia())
        );
    }
}