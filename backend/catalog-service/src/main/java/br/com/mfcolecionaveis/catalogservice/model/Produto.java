package br.com.mfcolecionaveis.catalogservice.model;

import br.com.mfcolecionaveis.catalogservice.model.vo.Nome;
import br.com.mfcolecionaveis.catalogservice.model.vo.NumeroFigura;
import br.com.mfcolecionaveis.catalogservice.model.vo.Sku;
import br.com.mfcolecionaveis.catalogservice.model.vo.Ean;

import lombok.Getter;
import java.util.UUID;

@Getter
public class Produto {
    private UUID id;
    private Nome nome;
    private Sku sku;
    private Ean ean;
    private TipoProduto tipoProduto;
    private NumeroFigura numeroFigura;
    private boolean ativo;

    public Produto(Nome nome, Sku sku, Ean ean, TipoProduto tipoProduto, NumeroFigura numeroFigura, boolean ativo) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.sku = sku;
        this.ean = ean;
        this.tipoProduto = tipoProduto;
        this.numeroFigura = numeroFigura;
        this.ativo = ativo;
    }

    private Produto(UUID id, Nome nome, Sku sku, Ean ean,
            TipoProduto tipoProduto, NumeroFigura numeroFigura,
            boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.sku = sku;
        this.ean = ean;
        this.tipoProduto = tipoProduto;
        this.numeroFigura = numeroFigura;
        this.ativo = ativo;
    }

    public static Produto reconstituir(UUID id, Nome nome, Sku sku, Ean ean,
            TipoProduto tipoProduto, NumeroFigura numeroFigura,
            boolean ativo) {
        return new Produto(id, nome, sku, ean, tipoProduto, numeroFigura, ativo);
    }

    public void ativar() {
        if (this.ativo) {
            throw new IllegalStateException("Produto já está ativo");
        }
        this.ativo = true;
    }

    public void desativar() {
        if (!this.ativo) {
            throw new IllegalStateException("Produto já está desativado");
        }
        this.ativo = false;
    }

    public void atualizarNome(Nome nome) {
        if (this.nome.equals(nome)) {
            throw new IllegalArgumentException("Novo nome é igual ao nome atual");
        }
        this.nome = nome;
    }

    public void atualizarSku(Sku sku) {
        if (this.sku.equals(sku)) {
            throw new IllegalArgumentException("Novo SKU é igual ao SKU atual");
        }
        this.sku = sku;
    }

    public void atualizarEan(Ean ean) {
        if (this.ean.equals(ean)) {
            throw new IllegalArgumentException("Novo EAN é igual ao EAN atual");
        }
        this.ean = ean;
    }

    public void atualizarTipoProduto(TipoProduto tipoProduto) {
        if (this.tipoProduto.equals(tipoProduto)) {
            throw new IllegalArgumentException("Novo tipo de produto é igual ao tipo de produto atual");
        }
        this.tipoProduto = tipoProduto;
    }

    public void atualizarNumeroFigura(NumeroFigura numeroFigura) {
        if (this.numeroFigura.equals(numeroFigura)) {
            throw new IllegalArgumentException("Novo número da figura é igual ao número da figura atual");
        }
        this.numeroFigura = numeroFigura;
    }

}
