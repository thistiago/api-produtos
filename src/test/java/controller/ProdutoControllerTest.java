package controller;


import org.caixacvp.controller.ProdutoController;
import org.caixacvp.model.Produto;
import org.caixacvp.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarProdutos() {
        // Dado que temos uma lista de produtos mockada
        Produto produto1 = new Produto(1L, "Produto 1", 10.0);
        Produto produto2 = new Produto(2L, "Produto 2", 20.0);

        when(produtoService.listarTodos()).thenReturn(Arrays.asList(produto1, produto2));

        // Quando chamamos o método listarProdutos()
        List<Produto> produtos = produtoController.listarProdutos();

        // Então verificamos se a lista retornada contém os produtos esperados
        assertEquals(2, produtos.size());
        assertEquals("Produto 1", produtos.get(0).getNome());
        assertEquals("Produto 2", produtos.get(1).getNome());

        // Verificamos se o método listarTodos() foi chamado uma vez
        verify(produtoService, times(1)).listarTodos();
    }

    @Test
    void criarProduto() {
        // Dado que temos um produto para criar
        Produto produto = new Produto(null, "Novo Produto", 50.0);
        Produto produtoCriado = new Produto(1L, "Novo Produto", 50.0);

        when(produtoService.criarProduto(any(Produto.class))).thenReturn(produtoCriado);

        // Quando chamamos o método criarProduto()
        ResponseEntity<Produto> response = produtoController.criarProduto(produto);

        // Então verificamos se o produto foi criado corretamente
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Novo Produto", response.getBody().getNome());

        // Verificamos se o método criarProduto() foi chamado uma vez
        verify(produtoService, times(1)).criarProduto(any(Produto.class));
    }

    @Test
    void buscarProdutoPorId_ProdutoExistente() {
        // Dado que temos um produto existente
        Produto produto = new Produto(1L, "Produto Existente", 100.0);

        when(produtoService.buscarPorId(1L)).thenReturn(produto);
    }

    @Test
    void buscarProdutoPorId_ProdutoExistenteStatus() {
        // Dado que temos um produto existente
        Produto produtoDTO = new Produto(1L, "Produto Teste", 100.0);

        // Mockando o comportamento do service para retornar o produto quando o ID for 1
        when(produtoService.buscarPorId(1L)).thenReturn(produtoDTO);

        // Quando chamamos o método buscarProdutoPorId()
        ResponseEntity<Produto> response = produtoController.buscarProdutoPorId(1L);

        // Então verificamos se o produto correto foi retornado com status 200 OK
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Produto Teste", response.getBody().getNome());
        assertEquals(100.0, response.getBody().getPreco());
        assertEquals(200, response.getStatusCodeValue());

        // Verificamos se o método buscarPorId() foi chamado uma vez com o ID correto
        verify(produtoService, times(1)).buscarPorId(1L);
    }

    @Test
    void buscarProdutoPorId_ProdutoNaoExistente() {
        // Mockando o comportamento do service para retornar null quando o produto não existir
        when(produtoService.buscarPorId(1L)).thenReturn(null);

        // Quando chamamos o método buscarProdutoPorId() com um ID inexistente
        ResponseEntity<Produto> response = produtoController.buscarProdutoPorId(1L);

        // Então verificamos se o retorno é 404 NOT FOUND
        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());

        // Verificamos se o método buscarPorId() foi chamado uma vez com o ID correto
        verify(produtoService, times(1)).buscarPorId(1L);
    }
}