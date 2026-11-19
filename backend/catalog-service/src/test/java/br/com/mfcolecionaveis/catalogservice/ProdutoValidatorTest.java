package br.com.mfcolecionaveis.catalogservice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import br.com.mfcolecionaveis.catalogservice.validation.ProdutoValidator;

@ExtendWith(MockitoExtension.class)
class ProdutoValidatorTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoValidator produtoValidator;

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
    void naoDeveValidarCadastroQuandoSkuJaExiste() {
        when(produtoRepository.existePorSku(produto.getSku())).thenReturn(true);

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> produtoValidator.validarCadastro(produto));

        assertEquals("SKU já cadastrado", erro.getMessage());
        verify(produtoRepository, never()).existePorEan(any(Ean.class));
    }

    @Test
    void naoDeveValidarCadastroQuandoEanJaExiste() {
        when(produtoRepository.existePorSku(produto.getSku())).thenReturn(false);
        when(produtoRepository.existePorEan(produto.getEan())).thenReturn(true);

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> produtoValidator.validarCadastro(produto));

        assertEquals("EAN já cadastrado", erro.getMessage());
    }

    @Test
    void deveValidarCadastroQuandoSkuEEanEstaoDisponiveis() {
        when(produtoRepository.existePorSku(produto.getSku())).thenReturn(false);
        when(produtoRepository.existePorEan(produto.getEan())).thenReturn(false);

        assertDoesNotThrow(() -> produtoValidator.validarCadastro(produto));
    }

    @Test
    void devePermitirAtualizacaoMantendoSkuEEanDoProduto() {
        DadosCadastraisProduto dados = dadosAtualizados(
                produto.getSku(),
                produto.getEan());

        assertDoesNotThrow(() -> produtoValidator.validarAtualizacao(produto, dados));

        verify(produtoRepository, never()).existePorSku(any(Sku.class));
        verify(produtoRepository, never()).existePorEan(any(Ean.class));
    }

    @Test
    void naoDeveValidarAtualizacaoQuandoNovoSkuJaExiste() {
        Sku novoSku = new Sku("sku-de-outro-produto");
        DadosCadastraisProduto dados = dadosAtualizados(novoSku, produto.getEan());
        when(produtoRepository.existePorSku(novoSku)).thenReturn(true);

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> produtoValidator.validarAtualizacao(produto, dados));

        assertEquals("SKU já cadastrado", erro.getMessage());
    }

    @Test
    void naoDeveValidarAtualizacaoQuandoNovoEanJaExiste() {
        Ean novoEan = new Ean("1111111111111");
        DadosCadastraisProduto dados = dadosAtualizados(produto.getSku(), novoEan);
        when(produtoRepository.existePorEan(novoEan)).thenReturn(true);

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> produtoValidator.validarAtualizacao(produto, dados));

        assertEquals("EAN já cadastrado", erro.getMessage());
    }

    @Test
    void deveValidarAtualizacaoQuandoNovosSkuEEanEstaoDisponiveis() {
        Sku novoSku = new Sku("produto-teste-novo");
        Ean novoEan = new Ean("1111111111111");
        DadosCadastraisProduto dados = dadosAtualizados(novoSku, novoEan);
        when(produtoRepository.existePorSku(novoSku)).thenReturn(false);
        when(produtoRepository.existePorEan(novoEan)).thenReturn(false);

        assertDoesNotThrow(() -> produtoValidator.validarAtualizacao(produto, dados));
    }

    private DadosCadastraisProduto dadosAtualizados(Sku sku, Ean ean) {
        return new DadosCadastraisProduto(
                new Nome("Produto atualizado"),
                sku,
                ean,
                TipoProduto.FUNKO_GRANDE,
                new NumeroFigura("1235"),
                new Franquia("Franquia atualizada"));
    }
}
