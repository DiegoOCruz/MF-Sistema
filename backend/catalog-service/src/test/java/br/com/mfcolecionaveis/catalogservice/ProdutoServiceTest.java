package br.com.mfcolecionaveis.catalogservice;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.mfcolecionaveis.catalogservice.model.DadosCadastraisProduto;
import br.com.mfcolecionaveis.catalogservice.model.Produto;
import br.com.mfcolecionaveis.catalogservice.model.TipoProduto;
import br.com.mfcolecionaveis.catalogservice.model.vo.Ean;
import br.com.mfcolecionaveis.catalogservice.model.vo.Franquia;
import br.com.mfcolecionaveis.catalogservice.model.vo.Nome;
import br.com.mfcolecionaveis.catalogservice.model.vo.NumeroFigura;
import br.com.mfcolecionaveis.catalogservice.model.vo.Sku;
import br.com.mfcolecionaveis.catalogservice.repository.ProdutoRepository;
import br.com.mfcolecionaveis.catalogservice.service.ProdutoService;
import br.com.mfcolecionaveis.catalogservice.validation.ProdutoValidator;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoValidator produtoValidator;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    void setup() {
        produto = new Produto(
                new Nome("Produto teste"),
                new Sku("produto-teste-123"),
                new Ean("0000000000000"),
                TipoProduto.FUNKO_NORMAL,
                new NumeroFigura("1234"),
                new Franquia("Teste"),
                true);
    }

    @Test
    void deveValidarESalvarProduto() {
        when(produtoRepository.salvar(produto)).thenReturn(produto);

        Produto produtoSalvo = produtoService.save(produto);

        assertSame(produto, produtoSalvo);
        verify(produtoValidator).validarCadastro(produto);
        verify(produtoRepository).salvar(produto);
    }

    @Test
    void deveBuscarPorId() {
        when(produtoRepository.buscarPorId(produto.getId()))
                .thenReturn(Optional.of(produto));

        Optional<Produto> encontrado = produtoService.findById(produto.getId());

        assertTrue(encontrado.isPresent());
        assertSame(produto, encontrado.get());
        verify(produtoRepository).buscarPorId(produto.getId());
    }

    @Test
    void deveListarTodosOsProdutos() {
        when(produtoRepository.listarProdutos()).thenReturn(List.of(produto));

        List<Produto> encontrados = produtoService.findAll();

        assertEquals(1, encontrados.size());
        assertSame(produto, encontrados.get(0));
        verify(produtoRepository).listarProdutos();
    }

    @Test
    void deveLocalizarPorNome() {
        when(produtoRepository.buscarPorNome("Produto teste"))
                .thenReturn(List.of(produto));

        List<Produto> encontrados = produtoService.findByNome("Produto teste");

        assertSame(produto, encontrados.get(0));
        verify(produtoRepository).buscarPorNome("Produto teste");
    }

    @Test
    void deveLocalizarPorFranquia() {
        when(produtoRepository.buscarPorFranquia("teste"))
                .thenReturn(List.of(produto));

        List<Produto> encontrados = produtoService.findByFranquia("teste");

        assertSame(produto, encontrados.get(0));
        verify(produtoRepository).buscarPorFranquia("teste");
    }

    @Test
    void deveLocalizarPorNumeroFigura() {
        when(produtoRepository.buscarPorNumeroFigura("1234"))
                .thenReturn(List.of(produto));

        List<Produto> encontrados = produtoService.findByNumeroFigura("1234");

        assertSame(produto, encontrados.get(0));
        verify(produtoRepository).buscarPorNumeroFigura("1234");
    }

    @Test
    void deveAtualizarProdutoPreservandoIdEEstadoAtivo() {
        UUID idOriginal = produto.getId();
        DadosCadastraisProduto dados = dadosAtualizados();
        when(produtoRepository.buscarPorId(idOriginal)).thenReturn(Optional.of(produto));
        when(produtoRepository.salvar(produto)).thenReturn(produto);

        Produto atualizado = produtoService.update(idOriginal, dados);

        assertSame(produto, atualizado);
        assertEquals(idOriginal, atualizado.getId());
        assertEquals(dados.nome(), atualizado.getNome());
        assertEquals(dados.sku(), atualizado.getSku());
        assertEquals(dados.ean(), atualizado.getEan());
        assertEquals(dados.tipoProduto(), atualizado.getTipoProduto());
        assertEquals(dados.numeroFigura(), atualizado.getNumeroFigura());
        assertEquals(dados.franquia(), atualizado.getFranquia());
        assertTrue(atualizado.isAtivo());
        verify(produtoValidator).validarAtualizacao(produto, dados);
        verify(produtoRepository).salvar(produto);
    }

    @Test
    void naoDeveAtualizarProdutoInexistente() {
        UUID idInexistente = UUID.randomUUID();
        DadosCadastraisProduto dados = dadosAtualizados();
        when(produtoRepository.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.update(idInexistente, dados));

        assertEquals("Produto não encontrado", erro.getMessage());
        verify(produtoValidator, never()).validarAtualizacao(any(Produto.class), any(DadosCadastraisProduto.class));
        verify(produtoRepository, never()).salvar(any(Produto.class));
    }

    @Test
    void naoDeveSalvarAtualizacaoQuandoValidadorRejeitaDados() {
        UUID idOriginal = produto.getId();
        DadosCadastraisProduto dados = dadosAtualizados();
        when(produtoRepository.buscarPorId(idOriginal)).thenReturn(Optional.of(produto));
        doThrow(new IllegalArgumentException("SKU já cadastrado"))
                .when(produtoValidator).validarAtualizacao(produto, dados);

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.update(idOriginal, dados));

        assertEquals("SKU já cadastrado", erro.getMessage());
        assertEquals(new Nome("Produto teste"), produto.getNome());
        verify(produtoRepository, never()).salvar(any(Produto.class));
    }

    @Test
    void deveAtivarProdutoInativo() {
        produto.desativar();
        when(produtoRepository.buscarPorId(produto.getId())).thenReturn(Optional.of(produto));

        produtoService.ativar(produto.getId());

        assertTrue(produto.isAtivo());
        verify(produtoRepository).salvar(produto);
    }

    @Test
    void naoDeveAtivarProdutoInexistente() {
        UUID idInexistente = UUID.randomUUID();
        when(produtoRepository.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.ativar(idInexistente));

        assertEquals("Produto não encontrado", erro.getMessage());
        verify(produtoRepository, never()).salvar(any(Produto.class));
    }

    @Test
    void naoDeveAtivarProdutoJaAtivo() {
        when(produtoRepository.buscarPorId(produto.getId())).thenReturn(Optional.of(produto));

        IllegalStateException erro = assertThrows(
                IllegalStateException.class,
                () -> produtoService.ativar(produto.getId()));

        assertEquals("Produto já está ativo", erro.getMessage());
        verify(produtoRepository, never()).salvar(any(Produto.class));
    }

    @Test
    void deveDesativarProdutoAtivo() {
        when(produtoRepository.buscarPorId(produto.getId())).thenReturn(Optional.of(produto));

        produtoService.desativar(produto.getId());

        assertFalse(produto.isAtivo());
        verify(produtoRepository).salvar(produto);
    }

    @Test
    void naoDeveDesativarProdutoInexistente() {
        UUID idInexistente = UUID.randomUUID();
        when(produtoRepository.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.desativar(idInexistente));

        assertEquals("Produto não encontrado", erro.getMessage());
        verify(produtoRepository, never()).salvar(any(Produto.class));
    }

    @Test
    void naoDeveDesativarProdutoJaInativo() {
        produto.desativar();
        when(produtoRepository.buscarPorId(produto.getId())).thenReturn(Optional.of(produto));

        IllegalStateException erro = assertThrows(
                IllegalStateException.class,
                () -> produtoService.desativar(produto.getId()));

        assertEquals("Produto já está desativado", erro.getMessage());
        verify(produtoRepository, never()).salvar(any(Produto.class));
    }

    private DadosCadastraisProduto dadosAtualizados() {
        return new DadosCadastraisProduto(
                new Nome("Produto atualizado"),
                new Sku("produto-atualizado-456"),
                new Ean("1111111111111"),
                TipoProduto.FUNKO_MEDIO,
                new NumeroFigura("2345"),
                new Franquia("Franquia atualizada"));
    }
}
