package service;

import org.caixacvp.model.Produto;
import org.caixacvp.repository.ProdutoRepository;
import org.caixacvp.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos() {
        // Dado que temos uma lista de produtos mockada
        Produto produto1 = new Produto(1L, "Produto 1", 10.0);
        Produto produto2 = new Produto(2L, "Produto 2", 20.0);

        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto1, produto2));

        // Quando chamamos o método listarTodos()
        List<Produto> produtos = produtoService.listarTodos();

        // Então verificamos se a lista retornada contém os produtos esperados
        assertEquals(2, produtos.size());
        assertEquals("Produto 1", produtos.get(0).getNome());
        assertEquals("Produto 2", produtos.get(1).getNome());

        // Verificamos se o método findAll() foi chamado uma vez
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void criarProduto() {
        // Dado que temos um produto para salvar
        Produto produto = new Produto(null, "Novo Produto", 50.0);
        Produto produtoSalvo = new Produto(1L, "Novo Produto", 50.0);

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        // Quando chamamos o método criarProduto()
        Produto novoProduto = produtoService.criarProduto(produto);

        // Então verificamos se o produto foi salvo corretamente
        assertNotNull(novoProduto.getId());
        assertEquals("Novo Produto", novoProduto.getNome());
        assertEquals(50.0, novoProduto.getPreco());

        // Verificamos se o método save() foi chamado uma vez
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void buscarPorId_ProdutoExistente() {
        // Dado que temos um produto existente
        Produto produto = new Produto(1L, "Produto Existente", 100.0);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        // Quando chamamos o método buscarPorId()
        Produto produtoSaved = produtoService.buscarPorId(1L);

        // Então verificamos se o produto correto foi retornado
        assertNotNull(produtoSaved);
        assertEquals("Produto Existente", produtoSaved.getNome());

        // Verificamos se o método findById() foi chamado uma vez
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_ProdutoNaoExistente() {
        // Dado que o produto não existe
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        // Quando chamamos o método buscarPorId()
        Produto produto = produtoService.buscarPorId(1L);

        // Então verificamos se o retorno é null
        assertNull(produto);

        // Verificamos se o método findById() foi chamado uma vez
        verify(produtoRepository, times(1)).findById(1L);
    }
}