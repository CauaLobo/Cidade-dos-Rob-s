package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe PredioController.
 */
class PredioControllerTest {

    private PredioController controller;
    private City cidade;

    @BeforeEach
    void setUp() {
        controller = new PredioController();
        cidade = new City("Teste");
    }

    @Test
    void testConstruirPredioComercial() {
        double dinheiroInicial = cidade.getDinheiro();
        int pecasInicial = cidade.getPecas();
        int prediosIniciais = cidade.getPredios().size();
        
        assertTrue(controller.construirPredio(TipoPredio.COMERCIAL, cidade, 5, 5));
        
        assertEquals(dinheiroInicial - 500.0, cidade.getDinheiro());
        assertEquals(pecasInicial - 300, cidade.getPecas());
        assertEquals(prediosIniciais + 1, cidade.getPredios().size());
    }

    @Test
    void testConstruirPredioResidencial() {
        double dinheiroInicial = cidade.getDinheiro();
        int pecasInicial = cidade.getPecas();
        
        assertTrue(controller.construirPredio(TipoPredio.RESIDENCIAL, cidade, 8, 8));
        
        assertEquals(dinheiroInicial - 300.0, cidade.getDinheiro());
        assertEquals(pecasInicial - 150, cidade.getPecas());
    }

    @Test
    void testConstruirPredioSemRecursos() {
        cidade.setDinheiro(100.0); // Insuficiente
        cidade.setPecas(50); // Insuficiente
        
        assertFalse(controller.construirPredio(TipoPredio.COMERCIAL, cidade, 5, 5));
    }

    @Test
    void testConstruirPredioForaDosLimites() {
        assertFalse(controller.construirPredio(TipoPredio.COMERCIAL, cidade, 29, 29)); // Fora dos limites
        assertFalse(controller.construirPredio(TipoPredio.COMERCIAL, cidade, -1, 5)); // Coordenada negativa
    }

    @Test
    void testConstruirPredioSobreOutroPredio() {
        controller.construirPredio(TipoPredio.COMERCIAL, cidade, 5, 5);
        
        // Tenta construir outro prédio na mesma posição
        assertFalse(controller.construirPredio(TipoPredio.RESIDENCIAL, cidade, 5, 5));
    }

    @Test
    void testConstruirCentro() {
        assertFalse(controller.construirPredio(TipoPredio.CENTRO, cidade, 5, 5)); // Centro não pode ser construído
    }

    @Test
    void testConstruirPredioDecorativo() {
        // Prédios decorativos não podem ser construídos pelo jogador
        assertFalse(controller.construirPredio(TipoPredio.MONUMENTO, cidade, 5, 5));
        assertFalse(controller.construirPredio(TipoPredio.TORRE_COMUNICACAO, cidade, 5, 5));
    }

    @Test
    void testConstruirPredioRecursosRestauradosSeFalhar() {
        double dinheiroInicial = cidade.getDinheiro();
        int pecasInicial = cidade.getPecas();
        
        // Tenta construir em posição inválida
        controller.construirPredio(TipoPredio.COMERCIAL, cidade, 29, 29);
        
        // Recursos não devem ter sido gastos
        assertEquals(dinheiroInicial, cidade.getDinheiro());
        assertEquals(pecasInicial, cidade.getPecas());
    }
}

