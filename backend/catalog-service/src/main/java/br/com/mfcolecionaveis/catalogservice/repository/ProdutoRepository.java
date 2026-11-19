package br.com.mfcolecionaveis.catalogservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.mfcolecionaveis.catalogservice.model.Produto;
import br.com.mfcolecionaveis.catalogservice.model.vo.Ean;
import br.com.mfcolecionaveis.catalogservice.model.vo.Sku;

public interface ProdutoRepository {
    Produto salvar(Produto produto);
    Optional<Produto> buscarPorId(UUID id);
    List<Produto> buscarPorFranquia(String franquia);
    boolean existePorSku(Sku sku);
    boolean existePorEan(Ean ean);
    List<Produto> buscarPorNome(String nome);
    List<Produto> buscarPorNumeroFigura(String numeroFigura);
    List<Produto> listarProdutos();
}
