package model;

/**
 * Classe que representa um robô de segurança.
 * 
 * <p>Robôs de segurança aumentam a felicidade média de todos os robôs da cidade
 * quando presentes. Também reduzem o impacto de eventos negativos como apagões.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class Seguranca extends Robo {
    /**
     * Construtor padrão para deserialização JSON (Jackson).
     */
    public Seguranca() {
        super();
        super.setTipo(TipoDeRobo.SEGURANCA);
    }
    
    /**
     * Construtor para criar um robô de segurança em uma posição específica.
     * 
     * @param x Posição X inicial no mapa
     * @param y Posição Y inicial no mapa
     */
    public Seguranca(int x, int y){
        super(TipoDeRobo.SEGURANCA, x , y);
    }
}
