package br.com.mfcolecionaveis.catalogservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.mfcolecionaveis.catalogservice.controller.mapper.ProdutoApiMapper;
import br.com.mfcolecionaveis.catalogservice.dto.ProdutoRequestDTO;
import br.com.mfcolecionaveis.catalogservice.dto.ProdutoResponseDTO;
import br.com.mfcolecionaveis.catalogservice.service.ProdutoService;
import br.com.mfcolecionaveis.catalogservice.model.DadosCadastraisProduto;
import br.com.mfcolecionaveis.catalogservice.model.Produto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;
    private final ProdutoApiMapper produtoApiMapper;

    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarPorId(@PathVariable UUID id) {
        return produtoService.findById(id)
                .map(produtoApiMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @GetMapping("/buscar-por-nome")
    public List<ProdutoResponseDTO> buscarPorNome(
            @RequestParam String nome) {
        return produtoService.findByNome(nome)
                .stream()
                .map(produtoApiMapper::toResponse)
                .toList();
    }

    @GetMapping
    public List<ProdutoResponseDTO> buscarTodos() {
        return produtoService.findAll()
                .stream()
                .map(produtoApiMapper::toResponse)
                .toList();
    }

    @GetMapping("/buscar-por-franquia")
    public List<ProdutoResponseDTO> buscarPorFranquia(
            @RequestParam String franquia) {
        return produtoService.findByFranquia(franquia)
                .stream()
                .map(produtoApiMapper::toResponse)
                .toList();
    }

    @GetMapping("/buscar-por-numero-figura")
    public List<ProdutoResponseDTO> buscarPorNumeroFigura(
            @RequestParam String numeroFigura) {
        return produtoService.findByNumeroFigura(numeroFigura)
                .stream()
                .map(produtoApiMapper::toResponse)
                .toList();
    }

    @PostMapping
    public ProdutoResponseDTO criarProduto(@RequestBody ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = produtoApiMapper.toDomain(produtoRequestDTO);
        Produto produtoSalvo = produtoService.save(produto);
        return produtoApiMapper.toResponse(produtoSalvo);
    }

    @PutMapping ("/{id}")
    public ProdutoResponseDTO atualizarProduto(
        @PathVariable UUID id,
        @RequestBody ProdutoRequestDTO request
    ){
        DadosCadastraisProduto dados = produtoApiMapper.toDadosAtualizacaoProduto(request);
        Produto atualizado = produtoService.update(id, dados);
        return produtoApiMapper.toResponse(atualizado);

    }

    @DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void desativarProduto(@PathVariable UUID id) {
    produtoService.desativar(id);
}

@PatchMapping("/{id}/ativar")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void ativarProduto(@PathVariable UUID id) {
    produtoService.ativar(id);
}

}
