package br.com.mfcolecionaveis.catalogservice.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.mfcolecionaveis.catalogservice.infrastructure.mapper.ProdutoMapper;
import br.com.mfcolecionaveis.catalogservice.infrastructure.persistence.entity.ProdutoEntity;
import br.com.mfcolecionaveis.catalogservice.infrastructure.persistence.repository.ProdutoJpaRepository;
import br.com.mfcolecionaveis.catalogservice.model.Produto;
import br.com.mfcolecionaveis.catalogservice.repository.ProdutoRepository;
import br.com.mfcolecionaveis.catalogservice.model.vo.Ean;
import br.com.mfcolecionaveis.catalogservice.model.vo.Sku;

@Repository
public class ProdutoRepositoryAdapter implements ProdutoRepository {

    private final ProdutoJpaRepository produtoJpaRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoRepositoryAdapter(ProdutoJpaRepository produtoJpaRepository, ProdutoMapper produtoMapper) {
        this.produtoJpaRepository = produtoJpaRepository;
        this.produtoMapper = produtoMapper;
    }

    @Override
    public Produto salvar (Produto produto) {
        ProdutoEntity entidade = produtoMapper.toEntity(produto);
        ProdutoEntity entidadeSalva = produtoJpaRepository.save(entidade);
        return produtoMapper.toDomain(entidadeSalva);
    }

    @Override
    public Optional<Produto> buscarPorId(UUID id) {
        return produtoJpaRepository.findById(id)
                .map(produtoMapper::toDomain);
    }

    @Override
    public boolean existePorSku(Sku sku) {
        return produtoJpaRepository.existsBySku(sku.sku());
    }

    @Override
    public boolean existePorEan(Ean ean) {
        return produtoJpaRepository.existsByEan(ean.ean());
    }

    @Override
    public List<Produto> buscarPorFranquia(String franquia) {
        return produtoJpaRepository.findByFranquia(franquia)
                .stream()
                .map(produtoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Produto> buscarPorNome(String nome) {
        return produtoJpaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(produtoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Produto> buscarPorNumeroFigura(String numeroFigura) {
        return produtoJpaRepository.findByNumeroFigura(numeroFigura)
                .stream()
                .map(produtoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Produto> listarProdutos() {
        return produtoJpaRepository.findAll()
                .stream()
                .map(produtoMapper::toDomain)
                .toList();
    }
}
