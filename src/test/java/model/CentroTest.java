package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe Centro.
 */
class CentroTest {

    private Centro centro;
    private City cidade;

    @BeforeEach
    void setUp() {
        centro = new Centro(15, 15);
        cidade = new City("Teste");
    }

    @Test
    void testConstrutor() {
        assertEquals(TipoPredio.CENTRO, centro.getTipo());
        assertEquals(15, centro.getPosX());
        assertEquals(15, centro.getPosY());
        assertEquals(200.0, centro.getCustoTreinamentoDinheiro());
        assertEquals(50, centro.getCustoTreinamentoPecas());
        assertNotNull(centro.getFilaDeTreinamento());
    }

    @Test
    void testIniciarTreinamentoTrabalhador() {
        centro.iniciarTreinamento(TipoDeRobo.TRABALHADOR);
        
        assertEquals(1, centro.getFilaDeTreinamento().size());
        RoboEmTreinamento robo = centro.getFilaDeTreinamento().get(0);
        assertEquals(TipoDeRobo.TRABALHADOR, robo.getTipo());
        assertEquals(2, robo.getTempoRestante()); // Trabalhador: 2 turnos
    }

    @Test
    void testIniciarTreinamentoEngenheiro() {
        centro.iniciarTreinamento(TipoDeRobo.ENGENHEIRO);
        
        RoboEmTreinamento robo = centro.getFilaDeTreinamento().get(0);
        assertEquals(TipoDeRobo.ENGENHEIRO, robo.getTipo());
        assertEquals(4, robo.getTempoRestante()); // Engenheiro: 4 turnos
    }

    @Test
    void testIniciarTreinamentoSeguranca() {
        centro.iniciarTreinamento(TipoDeRobo.SEGURANCA);
        
        RoboEmTreinamento robo = centro.getFilaDeTreinamento().get(0);
        assertEquals(TipoDeRobo.SEGURANCA, robo.getTipo());
        assertEquals(3, robo.getTempoRestante()); // Segurança: 3 turnos
    }

    @Test
    void testEfeitoProcessaTreinamento() {
        centro.iniciarTreinamento(TipoDeRobo.TRABALHADOR);
        int robosIniciais = cidade.getRobos().size();
        
        // Processa 2 turnos (tempo de treinamento do trabalhador)
        centro.efeito(cidade);
        assertEquals(1, centro.getFilaDeTreinamento().get(0).getTempoRestante());
        assertEquals(robosIniciais, cidade.getRobos().size()); // Ainda não terminou
        
        centro.efeito(cidade);
        
        // Agora o robô deve ter sido criado
        assertEquals(robosIniciais + 1, cidade.getRobos().size());
        assertEquals(0, centro.getFilaDeTreinamento().size());
        
        // Verifica que o robô criado é do tipo correto
        Robo roboCriado = cidade.getRobos().get(robosIniciais);
        assertEquals(TipoDeRobo.TRABALHADOR, roboCriado.getTipo());
    }

    @Test
    void testEfeitoComFilaVazia() {
        assertDoesNotThrow(() -> centro.efeito(cidade));
        assertEquals(0, cidade.getRobos().size());
    }

    @Test
    void testEfeitoMultiplosTreinamentos() {
        centro.iniciarTreinamento(TipoDeRobo.TRABALHADOR);
        centro.iniciarTreinamento(TipoDeRobo.ENGENHEIRO);
        
        assertEquals(2, centro.getFilaDeTreinamento().size());
        
        // Processa 2 turnos - trabalhador termina, engenheiro ainda não
        centro.efeito(cidade);
        centro.efeito(cidade);
        
        assertEquals(1, centro.getFilaDeTreinamento().size()); // Engenheiro ainda em treinamento
        assertEquals(1, cidade.getRobos().size()); // Apenas trabalhador foi criado
    }
}

