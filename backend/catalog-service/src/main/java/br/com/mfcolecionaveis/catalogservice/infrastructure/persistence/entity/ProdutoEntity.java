package br.com.mfcolecionaveis.catalogservice.infrastructure.persistence.entity;

import java.util.UUID;

import br.com.mfcolecionaveis.catalogservice.model.TipoProduto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter 
@Entity 
@Table(name = "produtos",
    uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(columnNames = {"sku"}),
        @jakarta.persistence.UniqueConstraint(columnNames = {"ean"})
    }
)
public class ProdutoEntity  {

    @Id 
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column (nullable = false, length = 100)
    private String nome;

    @Column (nullable = false, length = 50)
    private String sku;

    @Column (nullable = false, length = 13)
    private String ean;

    @Enumerated (EnumType.STRING)
    @Column (name = "tipo_produto", nullable = false, length = 30)
    private TipoProduto tipoProduto;

    @Column (name = "numero_figura", nullable = false, length = 4)
    private String numeroFigura;

    @Column (nullable = false)
    private boolean ativo;

    @Column (nullable = false, length = 100)
    private String franquia;

    protected ProdutoEntity() {
        // Construtor protegido para uso do JPA
    }

    public ProdutoEntity(UUID id, String nome, String sku, String ean,
                     TipoProduto tipoProduto, String numeroFigura, String franquia,
                     boolean ativo) {
    this.id = id;
    this.nome = nome;
    this.sku = sku;
    this.ean = ean;
    this.tipoProduto = tipoProduto;
    this.numeroFigura = numeroFigura;
    this.franquia = franquia;
    this.ativo = ativo;
}

}
