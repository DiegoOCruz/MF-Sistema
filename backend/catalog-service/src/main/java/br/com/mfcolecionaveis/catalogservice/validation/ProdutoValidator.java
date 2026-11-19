package br.com.mfcolecionaveis.catalogservice.validation;

import org.springframework.stereotype.Component;

import br.com.mfcolecionaveis.catalogservice.model.DadosCadastraisProduto;
import br.com.mfcolecionaveis.catalogservice.model.Produto;
import br.com.mfcolecionaveis.catalogservice.model.vo.Ean;
import br.com.mfcolecionaveis.catalogservice.model.vo.Sku;
import br.com.mfcolecionaveis.catalogservice.repository.ProdutoRepository;

@Component
public class ProdutoValidator {
    private final ProdutoRepository produtoRepository;

    public ProdutoValidator(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void validarCadastro(Produto produto) {
        validarSkuDisponivel(produto.getSku());
        validarEanDisponivel(produto.getEan());
    }

    private void validarSkuDisponivel(Sku sku) {
        if (produtoRepository.existePorSku(sku)) {
            throw new IllegalArgumentException("SKU já cadastrado");
        }
    }

    private void validarEanDisponivel(Ean ean) {
        if (produtoRepository.existePorEan(ean)) {
            throw new IllegalArgumentException("EAN já cadastrado");
        }
    }

    public void validarAtualizacao(
            Produto existente,
            DadosCadastraisProduto dados) {

        if (!existente.getSku().equals(dados.sku())) {
            validarSkuDisponivel(dados.sku());
        }

        if (!existente.getEan().equals(dados.ean())) {
            validarEanDisponivel(dados.ean());
        }
    }

}
