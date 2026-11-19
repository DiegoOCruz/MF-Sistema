package br.com.mfcolecionaveis.catalogservice.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mfcolecionaveis.catalogservice.infrastructure.persistence.entity.ProdutoEntity;

public interface ProdutoJpaRepository
    extends JpaRepository<ProdutoEntity, UUID> {
        boolean existsBySku(String sku);
        boolean existsByEan(String ean);
        List<ProdutoEntity> findByFranquia(String franquia);
        List<ProdutoEntity> findByNomeContainingIgnoreCase(String nome);
        List<ProdutoEntity> findByNumeroFigura(String numeroFigura);
        List<ProdutoEntity> findAll();

}
