package controller;

import model.City;
import model.Trabalhador;
import model.predioComercial;
import model.TipoDeRobo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

/**
 * Testes de unidade para a classe PersistenceController.
 */
class PersistenceControllerTest {

    private City cidade;

    @BeforeEach
    void setUp() {
        cidade = new City("CidadeTeste");
    }

    @Test
    void testSalvarECarregarCidade() throws IOException {
        // Adiciona alguns robôs e prédios para testar
        Trabalhador robo = new Trabalhador(5, 5);
        cidade.addRobo(robo);
        
        predioComercial predio = new predioComercial(10, 10);
        cidade.addPredio(predio);
        
        // Salva a cidade
        PersistenceController.salvarCidade(cidade, "CidadeTeste");
        
        // Carrega a cidade
        City cidadeCarregada = PersistenceController.carregarCidade("CidadeTeste");
        
        assertNotNull(cidadeCarregada);
        assertEquals("CidadeTeste", cidadeCarregada.getNome());
        assertEquals(1, cidadeCarregada.getRobos().size());
        assertEquals(TipoDeRobo.TRABALHADOR, cidadeCarregada.getRobos().get(0).getTipo());
        
        // Limpa o arquivo de teste
        PersistenceController.deletarCidade("CidadeTeste");
    }

    @Test
    void testListarCidadesSalvas() throws IOException {
        // Salva algumas cidades
        City cidade1 = new City("Cidade1");
        City cidade2 = new City("Cidade2");
        
        PersistenceController.salvarCidade(cidade1, "Cidade1");
        PersistenceController.salvarCidade(cidade2, "Cidade2");
        
        List<String> cidades = PersistenceController.listarCidadesSalvas();
        
        assertTrue(cidades.contains("Cidade1"));
        assertTrue(cidades.contains("Cidade2"));
        
        // Limpa os arquivos de teste
        PersistenceController.deletarCidade("Cidade1");
        PersistenceController.deletarCidade("Cidade2");
    }

    @Test
    void testDeletarCidade() throws IOException {
        PersistenceController.salvarCidade(cidade, "CidadeParaDeletar");
        
        assertTrue(PersistenceController.deletarCidade("CidadeParaDeletar"));
        
        // Tenta carregar - deve falhar
        assertThrows(IOException.class, () -> {
            PersistenceController.carregarCidade("CidadeParaDeletar");
        });
    }

    @Test
    void testCarregarCidadeInexistente() {
        assertThrows(IOException.class, () -> {
            PersistenceController.carregarCidade("CidadeInexistente");
        });
    }
}

