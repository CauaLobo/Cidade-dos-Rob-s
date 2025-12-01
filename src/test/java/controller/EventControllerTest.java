package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe EventController.
 */
class EventControllerTest {

    private EventController controller;
    private City cidade;
    private Turno turno;

    @BeforeEach
    void setUp() {
        controller = new EventController();
        cidade = new City("Teste");
        turno = new Turno(1);
    }

    @Test
    void testVerificarEventos() {
        // Eventos são aleatórios, então apenas verificamos que não causa erro
        assertDoesNotThrow(() -> controller.verificarEventos(cidade, turno));
    }

    @Test
    void testAplicarApagao() {
        Trabalhador robo1 = new Trabalhador(0, 0);
        Trabalhador robo2 = new Trabalhador(1, 1);
        cidade.addRobo(robo1);
        cidade.addRobo(robo2);
        
        double energiaInicial1 = robo1.getEnergia();
        double felicidadeInicial1 = robo1.getFelicidade();
        double energiaInicial2 = robo2.getEnergia();
        double felicidadeInicial2 = robo2.getFelicidade();
        
        // Simula apagão diretamente (método privado, então testamos através do efeito)
        robo1.apagao();
        robo2.apagao();
        
        assertEquals(energiaInicial1 - 40.0, robo1.getEnergia());
        assertEquals(felicidadeInicial1 - 40.0, robo1.getFelicidade());
        assertEquals(energiaInicial2 - 40.0, robo2.getEnergia());
        assertEquals(felicidadeInicial2 - 40.0, robo2.getFelicidade());
    }

    @Test
    void testAplicarGreve() {
        Trabalhador robo = new Trabalhador(0, 0);
        cidade.addRobo(robo);
        
        double felicidadeInicial = robo.getFelicidade();
        
        robo.greve();
        
        assertEquals(felicidadeInicial - 30.0, robo.getFelicidade());
        // Energia não deve mudar na greve
        assertEquals(100.0, robo.getEnergia());
    }

    @Test
    void testAplicarDescobertaPecasRaras() {
        Trabalhador robo = new Trabalhador(0, 0);
        robo.setFelicidade(80.0);
        cidade.addRobo(robo);
        
        robo.descobertaPecasRaras();
        
        assertEquals(95.0, robo.getFelicidade()); // 80 + 15
    }

    @Test
    void testEventoRegistradoNoTurno() {
        controller.verificarEventos(cidade, turno);
        
        // Se um evento ocorreu, deve estar registrado
        // Como eventos são aleatórios, apenas verificamos que o método não causa erro
        assertNotNull(turno.getEventosOcorridos());
    }
}

