package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe City.
 */
class CityTest {

    private City cidade;

    @BeforeEach
    void setUp() {
        cidade = new City("Teste");
    }

    @Test
    void testConstrutor() {
        assertEquals("Teste", cidade.getNome());
        assertEquals(15000.0, cidade.getDinheiro());
        assertEquals(10000, cidade.getPecas());
        assertEquals(0, cidade.getTurnoAtual());
        assertEquals(100.0, cidade.getFelicidadeMedia());
        assertNotNull(cidade.getRobos());
        assertNotNull(cidade.getPredios());
    }

    @Test
    void testGastarDinheiro() {
        assertTrue(cidade.gastarDinheiro(5000.0));
        assertEquals(10000.0, cidade.getDinheiro());
        
        assertFalse(cidade.gastarDinheiro(20000.0));
        assertEquals(10000.0, cidade.getDinheiro()); // Não mudou
    }

    @Test
    void testGastarPecas() {
        assertTrue(cidade.gastarPecas(5000));
        assertEquals(5000, cidade.getPecas());
        
        assertFalse(cidade.gastarPecas(10000));
        assertEquals(5000, cidade.getPecas()); // Não mudou
    }

    @Test
    void testAddDinheiro() {
        cidade.addDinheiro(1000.0);
        assertEquals(16000.0, cidade.getDinheiro());
    }

    @Test
    void testAddPecas() {
        cidade.addPecas(500);
        assertEquals(10500, cidade.getPecas());
    }

    @Test
    void testFelicidadeMedia() {
        Trabalhador robo1 = new Trabalhador(0, 0);
        robo1.setFelicidade(80.0);
        
        Trabalhador robo2 = new Trabalhador(1, 1);
        robo2.setFelicidade(60.0);
        
        cidade.addRobo(robo1);
        cidade.addRobo(robo2);
        
        cidade.felicidadeMedia();
        
        assertEquals(70.0, cidade.getFelicidadeMedia()); // (80 + 60) / 2
    }

    @Test
    void testFelicidadeMediaSemRobos() {
        cidade.felicidadeMedia();
        // Mantém o valor atual se não houver robôs
        assertEquals(100.0, cidade.getFelicidadeMedia());
    }

    @Test
    void testAddRobo() {
        Trabalhador robo = new Trabalhador(5, 5);
        cidade.addRobo(robo);
        
        assertEquals(1, cidade.getRobos().size());
        assertTrue(cidade.getRobos().contains(robo));
    }

    @Test
    void testAddPredio() {
        predioComercial predio = new predioComercial(10, 10);
        cidade.addPredio(predio);
        
        assertEquals(8, cidade.getPredios().size()); // 7 iniciais (Centro + 6 decorativos) + 1 novo
        assertTrue(cidade.getPredios().contains(predio));
    }

    @Test
    void testIncrementaTurno() {
        cidade.incrementaTurno();
        assertEquals(1, cidade.getTurnoAtual());
        
        cidade.incrementaTurno();
        assertEquals(2, cidade.getTurnoAtual());
    }

    @Test
    void testPrediosIniciais() {
        // Verifica que o Centro foi adicionado
        boolean temCentro = false;
        for (Predio predio : cidade.getPredios()) {
            if (predio instanceof Centro) {
                temCentro = true;
                break;
            }
        }
        assertTrue(temCentro);
        
        // Verifica que há prédios decorativos
        int decorativos = 0;
        for (Predio predio : cidade.getPredios()) {
            if (predio instanceof PredioDecorativo) {
                decorativos++;
            }
        }
        assertEquals(6, decorativos); // 6 prédios decorativos pré-colocados
    }
}

