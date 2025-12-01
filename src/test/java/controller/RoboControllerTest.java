package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe RoboController.
 */
class RoboControllerTest {

    private RoboController controller;
    private City cidade;

    @BeforeEach
    void setUp() {
        controller = new RoboController();
        cidade = new City("Teste");
    }

    @Test
    void testTreinarRobo() {
        // Configura recursos suficientes
        cidade.setDinheiro(1000.0);
        cidade.setPecas(1000);
        
        assertTrue(controller.treinarRobo(cidade, TipoDeRobo.TRABALHADOR));
        
        // Verifica que os recursos foram gastos
        assertEquals(800.0, cidade.getDinheiro()); // 1000 - 200
        assertEquals(950, cidade.getPecas()); // 1000 - 50
        
        // Verifica que o treinamento foi iniciado
        Centro centro = encontrarCentro(cidade);
        assertNotNull(centro);
        assertEquals(1, centro.getFilaDeTreinamento().size());
    }

    @Test
    void testTreinarRoboSemRecursos() {
        cidade.setDinheiro(100.0); // Insuficiente
        cidade.setPecas(1000);
        
        assertFalse(controller.treinarRobo(cidade, TipoDeRobo.TRABALHADOR));
        
        Centro centro = encontrarCentro(cidade);
        assertEquals(0, centro.getFilaDeTreinamento().size());
    }

    @Test
    void testFazerManutencao() {
        Trabalhador robo = new Trabalhador(0, 0);
        assertFalse(robo.isEmManutencao());
        
        assertTrue(controller.fazerManutencao(robo));
        assertTrue(robo.isEmManutencao());
        assertEquals(2, robo.getTurnosRestantesManutencao());
    }

    @Test
    void testFazerManutencaoJaEmManutencao() {
        Trabalhador robo = new Trabalhador(0, 0);
        robo.iniciarManutencao();
        
        assertFalse(controller.fazerManutencao(robo)); // Não pode iniciar outra manutenção
    }

    @Test
    void testMoverRoboParaPredioComercial() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        cidade.addRobo(trabalhador);
        
        predioComercial predio = new predioComercial(10, 10);
        cidade.addPredio(predio);
        
        assertTrue(controller.moverRoboParaPredio(trabalhador, predio, cidade));
        assertEquals(1, predio.getRobos().size());
        assertTrue(predio.getRobos().contains(trabalhador));
    }

    @Test
    void testMoverRoboParaPredioResidencial() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        cidade.addRobo(trabalhador);
        
        predioResidencial predio = new predioResidencial(10, 10);
        cidade.addPredio(predio);
        
        assertTrue(controller.moverRoboParaPredio(trabalhador, predio, cidade));
        assertEquals(1, predio.getRobos().size());
    }

    @Test
    void testMoverSegurancaParaComercial() {
        Seguranca seguranca = new Seguranca(0, 0);
        cidade.addRobo(seguranca);
        
        predioComercial predio = new predioComercial(10, 10);
        cidade.addPredio(predio);
        
        assertFalse(controller.moverRoboParaPredio(seguranca, predio, cidade)); // Segurança não pode trabalhar em comercial
    }

    @Test
    void testRemoverRoboDePredio() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        cidade.addRobo(trabalhador);
        
        predioComercial predio = new predioComercial(10, 10);
        cidade.addPredio(predio);
        predio.addRobo(trabalhador);
        
        assertTrue(controller.removerRoboDePredio(trabalhador, cidade));
        assertEquals(0, predio.getRobos().size());
    }

    @Test
    void testListarRobosDisponiveis() {
        Trabalhador robo1 = new Trabalhador(0, 0);
        Trabalhador robo2 = new Trabalhador(1, 1);
        cidade.addRobo(robo1);
        cidade.addRobo(robo2);
        
        predioComercial predio = new predioComercial(10, 10);
        cidade.addPredio(predio);
        predio.addRobo(robo1);
        
        var disponiveis = controller.listarRobosDisponiveis(cidade);
        
        assertEquals(1, disponiveis.size());
        assertTrue(disponiveis.contains(robo2));
        assertFalse(disponiveis.contains(robo1));
    }

    @Test
    void testEncontrarPredioDoRobo() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        cidade.addRobo(trabalhador);
        
        predioComercial predio = new predioComercial(10, 10);
        cidade.addPredio(predio);
        predio.addRobo(trabalhador);
        
        Predio predioEncontrado = controller.encontrarPredioDoRobo(trabalhador, cidade);
        assertEquals(predio, predioEncontrado);
    }

    @Test
    void testListarRobosPorTipo() {
        Trabalhador robo1 = new Trabalhador(0, 0);
        Trabalhador robo2 = new Trabalhador(1, 1);
        Engenheiro engenheiro = new Engenheiro(2, 2);
        
        cidade.addRobo(robo1);
        cidade.addRobo(robo2);
        cidade.addRobo(engenheiro);
        
        var trabalhadores = controller.listarRobosPorTipo(cidade, TipoDeRobo.TRABALHADOR);
        assertEquals(2, trabalhadores.size());
        
        var engenheiros = controller.listarRobosPorTipo(cidade, TipoDeRobo.ENGENHEIRO);
        assertEquals(1, engenheiros.size());
    }

    @Test
    void testCalcularBonusFelicidadeSeguranca() {
        Seguranca seg1 = new Seguranca(0, 0);
        Seguranca seg2 = new Seguranca(1, 1);
        cidade.addRobo(seg1);
        cidade.addRobo(seg2);
        
        double bonus = controller.calcularBonusFelicidadeSeguranca(cidade);
        assertEquals(6.0, bonus); // 2 seguranças * 3 pontos
    }

    @Test
    void testAplicarBonusFelicidadeSeguranca() {
        cidade.setFelicidadeMedia(50.0);
        Seguranca seg1 = new Seguranca(0, 0);
        Seguranca seg2 = new Seguranca(1, 1);
        cidade.addRobo(seg1);
        cidade.addRobo(seg2);
        
        controller.aplicarBonusFelicidadeSeguranca(cidade);
        
        assertEquals(56.0, cidade.getFelicidadeMedia()); // 50 + 6
    }

    @Test
    void testDeletarRobo() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        cidade.addRobo(trabalhador);
        
        predioComercial predio = new predioComercial(10, 10);
        cidade.addPredio(predio);
        predio.addRobo(trabalhador);
        
        assertTrue(controller.deletarRobo(trabalhador, cidade));
        assertEquals(0, cidade.getRobos().size());
        assertEquals(0, predio.getRobos().size());
    }

    private Centro encontrarCentro(City city) {
        for (Predio predio : city.getPredios()) {
            if (predio instanceof Centro) {
                return (Centro) predio;
            }
        }
        return null;
    }
}

