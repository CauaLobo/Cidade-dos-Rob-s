package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe predioResidencial.
 */
class predioResidencialTest {

    private predioResidencial predio;
    private City cidade;

    @BeforeEach
    void setUp() {
        predio = new predioResidencial(10, 10);
        cidade = new City("Teste");
    }

    @Test
    void testConstrutor() {
        assertEquals(TipoPredio.RESIDENCIAL, predio.getTipo());
        assertEquals(10, predio.getPosX());
        assertEquals(10, predio.getPosY());
        assertEquals(2, predio.getLargura());
        assertEquals(2, predio.getAltura());
    }

    @Test
    void testAddRoboQualquerTipo() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        Engenheiro engenheiro = new Engenheiro(1, 1);
        Seguranca seguranca = new Seguranca(2, 2);
        
        assertTrue(predio.addRobo(trabalhador));
        assertTrue(predio.addRobo(engenheiro));
        assertTrue(predio.addRobo(seguranca));
        
        assertEquals(3, predio.getRobos().size());
    }

    @Test
    void testAddRoboMaximo() {
        for (int i = 0; i < 5; i++) {
            Trabalhador robo = new Trabalhador(i, i);
            assertTrue(predio.addRobo(robo));
        }
        
        Trabalhador robo6 = new Trabalhador(6, 6);
        assertFalse(predio.addRobo(robo6));
    }

    @Test
    void testRemoveRobo() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        predio.addRobo(trabalhador);
        
        predio.removeRobo(trabalhador);
        
        assertEquals(0, predio.getRobos().size());
    }

    @Test
    void testEfeitoRecuperaEnergia() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        trabalhador.setEnergia(50.0);
        trabalhador.setFelicidade(50.0);
        predio.addRobo(trabalhador);
        
        predio.efeito(cidade);
        
        assertEquals(90.0, trabalhador.getEnergia()); // 50 + 40
        assertEquals(70.0, trabalhador.getFelicidade()); // 50 + 20
    }

    @Test
    void testEfeitoSemRobos() {
        // Não deve causar erro
        assertDoesNotThrow(() -> predio.efeito(cidade));
    }

    @Test
    void testEfeitoMultiplosRobos() {
        Trabalhador robo1 = new Trabalhador(0, 0);
        robo1.setEnergia(30.0);
        robo1.setFelicidade(40.0);
        
        Engenheiro robo2 = new Engenheiro(1, 1);
        robo2.setEnergia(20.0);
        robo2.setFelicidade(50.0);
        
        predio.addRobo(robo1);
        predio.addRobo(robo2);
        
        predio.efeito(cidade);
        
        assertEquals(70.0, robo1.getEnergia()); // 30 + 40
        assertEquals(60.0, robo1.getFelicidade()); // 40 + 20
        
        assertEquals(60.0, robo2.getEnergia()); // 20 + 40
        assertEquals(70.0, robo2.getFelicidade()); // 50 + 20
    }
}

