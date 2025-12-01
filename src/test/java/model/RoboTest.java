package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe Robo.
 */
class RoboTest {

    private Trabalhador trabalhador;
    private Engenheiro engenheiro;

    @BeforeEach
    void setUp() {
        trabalhador = new Trabalhador(10, 10);
        engenheiro = new Engenheiro(15, 15);
    }

    @Test
    void testConstrutor() {
        assertNotNull(trabalhador.getId());
        assertEquals(TipoDeRobo.TRABALHADOR, trabalhador.getTipo());
        assertEquals(100.0, trabalhador.getEnergia());
        assertEquals(100.0, trabalhador.getFelicidade());
        assertEquals(100.0, trabalhador.getIntegridade());
        assertEquals(10, trabalhador.getPosX());
        assertEquals(10, trabalhador.getPosY());
        assertFalse(trabalhador.isEmManutencao());
    }

    @Test
    void testTrabalho() {
        double energiaInicial = trabalhador.getEnergia();
        double integridadeInicial = trabalhador.getIntegridade();
        
        trabalhador.trabalho();
        
        assertEquals(energiaInicial - 10.0, trabalhador.getEnergia());
        assertEquals(integridadeInicial - 5.0, trabalhador.getIntegridade());
    }

    @Test
    void testTrabalhoEngenheiro() {
        double energiaInicial = engenheiro.getEnergia();
        engenheiro.trabalho();
        
        // Engenheiro consome 50% mais energia (10 * 1.5 = 15)
        assertEquals(energiaInicial - 15.0, engenheiro.getEnergia());
    }

    @Test
    void testIniciarManutencao() {
        assertFalse(trabalhador.isEmManutencao());
        trabalhador.iniciarManutencao();
        
        assertTrue(trabalhador.isEmManutencao());
        assertEquals(2, trabalhador.getTurnosRestantesManutencao());
    }

    @Test
    void testProcessarManutencao() {
        trabalhador.iniciarManutencao();
        trabalhador.processarManutencao();
        
        assertEquals(1, trabalhador.getTurnosRestantesManutencao());
        assertTrue(trabalhador.isEmManutencao());
        
        trabalhador.processarManutencao();
        
        assertFalse(trabalhador.isEmManutencao());
        assertEquals(100.0, trabalhador.getIntegridade());
        assertEquals(0, trabalhador.getTurnosDesdeAManutencao());
    }

    @Test
    void testDormir() {
        trabalhador.setEnergia(50.0);
        trabalhador.setFelicidade(50.0);
        
        trabalhador.dormir();
        
        assertEquals(90.0, trabalhador.getEnergia()); // 50 + 40
        assertEquals(70.0, trabalhador.getFelicidade()); // 50 + 20
    }

    @Test
    void testDormirLimiteMaximo() {
        trabalhador.setEnergia(95.0);
        trabalhador.setFelicidade(95.0);
        
        trabalhador.dormir();
        
        assertEquals(100.0, trabalhador.getEnergia()); // Não ultrapassa 100
        assertEquals(100.0, trabalhador.getFelicidade()); // Não ultrapassa 100
    }

    @Test
    void testApagao() {
        trabalhador.apagao();
        
        assertEquals(60.0, trabalhador.getEnergia()); // 100 - 40
        assertEquals(60.0, trabalhador.getFelicidade()); // 100 - 40
    }

    @Test
    void testApagaoLimiteMinimo() {
        trabalhador.setEnergia(20.0);
        trabalhador.setFelicidade(20.0);
        
        trabalhador.apagao();
        
        assertEquals(0.0, trabalhador.getEnergia()); // Não fica negativo
        assertEquals(0.0, trabalhador.getFelicidade()); // Não fica negativo
    }

    @Test
    void testGreve() {
        trabalhador.greve();
        
        assertEquals(70.0, trabalhador.getFelicidade()); // 100 - 30
        assertEquals(100.0, trabalhador.getEnergia()); // Energia não muda na greve
    }

    @Test
    void testDescobertaPecasRaras() {
        trabalhador.setFelicidade(80.0);
        trabalhador.descobertaPecasRaras();
        
        assertEquals(95.0, trabalhador.getFelicidade()); // 80 + 15
    }

    @Test
    void testConsumoDiario() {
        double energiaInicial = trabalhador.getEnergia();
        double integridadeInicial = trabalhador.getIntegridade();
        
        trabalhador.consumoDiario();
        
        assertEquals(energiaInicial - 10, trabalhador.getEnergia());
        assertEquals(integridadeInicial - 5.0, trabalhador.getIntegridade());
        assertEquals(1, trabalhador.getTurnosDesdeAManutencao());
    }

    @Test
    void testConsumoDiarioEmManutencao() {
        trabalhador.iniciarManutencao();
        double energiaInicial = trabalhador.getEnergia();
        
        trabalhador.consumoDiario();
        
        // Em manutenção, não consome energia, apenas processa manutenção
        assertEquals(energiaInicial, trabalhador.getEnergia());
        assertEquals(1, trabalhador.getTurnosRestantesManutencao());
    }

    @Test
    void testFelicidadeReduzidaComEnergiaBaixa() {
        trabalhador.setEnergia(30.0);
        trabalhador.setIntegridade(50.0);
        double felicidadeInicial = trabalhador.getFelicidade();
        
        trabalhador.consumoDiario();
        
        assertEquals(felicidadeInicial - 10.0, trabalhador.getFelicidade());
    }

    @Test
    void testFelicidadeReduzidaComIntegridadeBaixa() {
        trabalhador.setEnergia(50.0);
        trabalhador.setIntegridade(30.0);
        double felicidadeInicial = trabalhador.getFelicidade();
        
        trabalhador.consumoDiario();
        
        assertEquals(felicidadeInicial - 10.0, trabalhador.getFelicidade());
    }
}

