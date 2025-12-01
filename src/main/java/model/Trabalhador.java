package model;

/**
 * Classe que representa um robô trabalhador.
 * 
 * <p>Robôs trabalhadores são especializados em gerar dinheiro quando trabalham
 * em prédios comerciais.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class Trabalhador extends Robo {
    /**
     * Construtor padrão para deserialização JSON (Jackson).
     */
    public Trabalhador() {
        super();
        super.setTipo(TipoDeRobo.TRABALHADOR);
    }
    
    /**
     * Construtor para criar um robô trabalhador em uma posição específica.
     * 
     * @param x Posição X inicial no mapa
     * @param y Posição Y inicial no mapa
     */
    public Trabalhador(int x, int y){
        super(TipoDeRobo.TRABALHADOR, x, y);
    }
}
