package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe JogoController.
 */
class JogoControllerTest {

    private JogoController controller;
    private City cidade;

    @BeforeEach
    void setUp() {
        cidade = new City("Teste");
        controller = new JogoController(cidade);
    }

    @Test
    void testConstrutor() {
        assertNotNull(controller);
        assertEquals(cidade, controller.getCidadeAtual());
    }

    @Test
    void testProximoTurno() {
        int turnoInicial = cidade.getTurnoAtual();
        
        controller.proximoTurno();
        
        assertEquals(turnoInicial + 1, cidade.getTurnoAtual());
        assertNotNull(controller.getUltimoTurno());
    }

    @Test
    void testProximoTurnoProcessaEfeitosPredios() {
        predioComercial comercial = new predioComercial(5, 5);
        Trabalhador trabalhador = new Trabalhador(0, 0);
        trabalhador.setEnergia(50.0);
        trabalhador.setIntegridade(50.0);
        comercial.addRobo(trabalhador);
        cidade.addRobo(trabalhador);
        cidade.addPredio(comercial);
        
        double dinheiroInicial = cidade.getDinheiro();
        
        controller.proximoTurno();
        
        // Prédio comercial deve ter gerado dinheiro
        assertTrue(cidade.getDinheiro() > dinheiroInicial);
    }

    @Test
    void testProximoTurnoConsumoDiario() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        cidade.addRobo(trabalhador);
        
        double energiaInicial = trabalhador.getEnergia();
        double integridadeInicial = trabalhador.getIntegridade();
        
        controller.proximoTurno();
        
        // Robôs não em prédios residenciais devem consumir recursos
        assertTrue(trabalhador.getEnergia() < energiaInicial);
        assertTrue(trabalhador.getIntegridade() < integridadeInicial);
    }

    @Test
    void testProximoTurnoRoboEmResidencialNaoConsome() {
        predioResidencial residencial = new predioResidencial(5, 5);
        Trabalhador trabalhador = new Trabalhador(0, 0);
        trabalhador.setEnergia(50.0);
        residencial.addRobo(trabalhador);
        cidade.addRobo(trabalhador);
        cidade.addPredio(residencial);
        
        controller.proximoTurno();
        
        // Robô em residencial deve ter recuperado energia (não consumido)
        assertTrue(trabalhador.getEnergia() > 50.0);
    }

    @Test
    void testProximoTurnoCalculaFelicidadeMedia() {
        Trabalhador robo1 = new Trabalhador(0, 0);
        robo1.setFelicidade(80.0);
        Trabalhador robo2 = new Trabalhador(1, 1);
        robo2.setFelicidade(60.0);
        
        cidade.addRobo(robo1);
        cidade.addRobo(robo2);
        
        controller.proximoTurno();
        
        cidade.felicidadeMedia();
        assertEquals(70.0, cidade.getFelicidadeMedia()); // (80 + 60) / 2
    }

    @Test
    void testProximoTurnoAplicaBonusSeguranca() {
        Seguranca seg1 = new Seguranca(0, 0);
        Seguranca seg2 = new Seguranca(1, 1);
        cidade.addRobo(seg1);
        cidade.addRobo(seg2);
        cidade.setFelicidadeMedia(50.0);
        
        controller.proximoTurno();
        
        // Felicidade deve ter aumentado pelo bônus dos seguranças
        assertTrue(cidade.getFelicidadeMedia() > 50.0);
    }

    @Test
    void testGetUltimoTurno() {
        assertNull(controller.getUltimoTurno()); // Antes do primeiro turno
        
        controller.proximoTurno();
        
        assertNotNull(controller.getUltimoTurno());
        assertEquals(1, controller.getUltimoTurno().getnTurno());
    }

    @Test
    void testGetCidadeAtual() {
        assertEquals(cidade, controller.getCidadeAtual());
    }
}

