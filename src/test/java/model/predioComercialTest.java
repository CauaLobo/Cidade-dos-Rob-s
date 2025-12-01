package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe predioComercial.
 */
class predioComercialTest {

    private predioComercial predio;
    private City cidade;

    @BeforeEach
    void setUp() {
        predio = new predioComercial(10, 10);
        cidade = new City("Teste");
    }

    @Test
    void testConstrutor() {
        assertEquals(TipoPredio.COMERCIAL, predio.getTipo());
        assertEquals(10, predio.getPosX());
        assertEquals(10, predio.getPosY());
        assertEquals(2, predio.getLargura());
        assertEquals(2, predio.getAltura());
        assertEquals(50.0, predio.getTaxaDinheiro());
        assertEquals(25, predio.getTaxaPecas());
    }

    @Test
    void testAddRoboTrabalhador() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        assertTrue(predio.addRobo(trabalhador));
        assertEquals(1, predio.getRobos().size());
    }

    @Test
    void testAddRoboEngenheiro() {
        Engenheiro engenheiro = new Engenheiro(0, 0);
        assertTrue(predio.addRobo(engenheiro));
        assertEquals(1, predio.getRobos().size());
    }

    @Test
    void testAddRoboSeguranca() {
        Seguranca seguranca = new Seguranca(0, 0);
        assertFalse(predio.addRobo(seguranca)); // Segurança não pode trabalhar em comercial
    }

    @Test
    void testAddRoboMaximo() {
        // Adiciona 5 trabalhadores (máximo)
        for (int i = 0; i < 5; i++) {
            Trabalhador robo = new Trabalhador(i, i);
            assertTrue(predio.addRobo(robo));
        }
        
        // Tentativa de adicionar o 6º deve falhar
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
    void testEfeitoSemRobos() {
        double dinheiroInicial = cidade.getDinheiro();
        int pecasInicial = cidade.getPecas();
        
        predio.efeito(cidade);
        
        // Sem robôs, não deve gerar recursos
        assertEquals(dinheiroInicial, cidade.getDinheiro());
        assertEquals(pecasInicial, cidade.getPecas());
    }

    @Test
    void testEfeitoComTrabalhador() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        trabalhador.setEnergia(50.0);
        trabalhador.setIntegridade(50.0);
        predio.addRobo(trabalhador);
        
        double dinheiroInicial = cidade.getDinheiro();
        
        predio.efeito(cidade);
        
        // Trabalhador gera dinheiro
        assertEquals(dinheiroInicial + 50.0, cidade.getDinheiro());
    }

    @Test
    void testEfeitoComEngenheiro() {
        Engenheiro engenheiro = new Engenheiro(0, 0);
        engenheiro.setEnergia(50.0);
        engenheiro.setIntegridade(50.0);
        predio.addRobo(engenheiro);
        
        int pecasInicial = cidade.getPecas();
        
        predio.efeito(cidade);
        
        // Engenheiro descobre peças
        assertEquals(pecasInicial + 25, cidade.getPecas());
    }

    @Test
    void testEfeitoComTrabalhadorEEngenheiro() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        trabalhador.setEnergia(50.0);
        trabalhador.setIntegridade(50.0);
        
        Engenheiro engenheiro = new Engenheiro(1, 1);
        engenheiro.setEnergia(50.0);
        engenheiro.setIntegridade(50.0);
        
        predio.addRobo(trabalhador);
        predio.addRobo(engenheiro);
        
        double dinheiroInicial = cidade.getDinheiro();
        int pecasInicial = cidade.getPecas();
        
        predio.efeito(cidade);
        
        // Trabalhador gera dinheiro + bônus de 20% do engenheiro
        assertEquals(dinheiroInicial + 50.0 + (50.0 * 0.20), cidade.getDinheiro());
        // Engenheiro descobre peças
        assertEquals(pecasInicial + 25, cidade.getPecas());
    }

    @Test
    void testEfeitoRoboComEnergiaBaixa() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        trabalhador.setEnergia(20.0); // Abaixo de 30
        trabalhador.setIntegridade(50.0);
        predio.addRobo(trabalhador);
        
        double dinheiroInicial = cidade.getDinheiro();
        
        predio.efeito(cidade);
        
        // Robô com energia baixa não trabalha
        assertEquals(dinheiroInicial, cidade.getDinheiro());
    }

    @Test
    void testEfeitoRoboEmManutencao() {
        Trabalhador trabalhador = new Trabalhador(0, 0);
        trabalhador.setEnergia(50.0);
        trabalhador.setIntegridade(50.0);
        trabalhador.iniciarManutencao();
        predio.addRobo(trabalhador);
        
        double dinheiroInicial = cidade.getDinheiro();
        
        predio.efeito(cidade);
        
        // Robô em manutenção não trabalha
        assertEquals(dinheiroInicial, cidade.getDinheiro());
    }
}

