package br.com.mfcolecionaveis.catalogservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.mfcolecionaveis.catalogservice.model.DadosCadastraisProduto;
import br.com.mfcolecionaveis.catalogservice.model.Produto;
import br.com.mfcolecionaveis.catalogservice.repository.ProdutoRepository;
import br.com.mfcolecionaveis.catalogservice.validation.ProdutoValidator;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoValidator produtoValidator;

    public ProdutoService(ProdutoRepository produtoRepository, ProdutoValidator produtoValidator) {
        this.produtoRepository = produtoRepository;
        this.produtoValidator = produtoValidator;
    }

    public Produto save(Produto produto) {
        produtoValidator.validarCadastro(produto);
        return produtoRepository.salvar(produto);
    }

    public Optional<Produto> findById(UUID id) {
        return produtoRepository.buscarPorId(id);
    }

    public List<Produto> findByFranquia(String franquia) {
        return produtoRepository.buscarPorFranquia(franquia);
    }

    public List<Produto> findByNome(String nome) {
        return produtoRepository.buscarPorNome(nome);
    }

    public List<Produto> findByNumeroFigura(String numeroFigura) {
        return produtoRepository.buscarPorNumeroFigura(numeroFigura);
    }

    public List<Produto> findAll() {
        return produtoRepository.listarProdutos();
    }

    public Produto update(UUID id, DadosCadastraisProduto dados) {
        Optional<Produto> produtoToUpdate = findById(id);
        if (produtoToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        Produto existente = produtoToUpdate.get();
        produtoValidator.validarAtualizacao(existente, dados);
        existente.atualizarDadosCadastrais(dados);
        return produtoRepository.salvar(existente);
    }

    public void desativar(UUID id) {
        Produto existente = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        existente.desativar();
        produtoRepository.salvar(existente);
    }

    public void ativar(UUID id) {
        Produto existente = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        existente.ativar();
        produtoRepository.salvar(existente);
    }

}
