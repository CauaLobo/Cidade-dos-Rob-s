package model;

/**
 * Classe que representa um robô engenheiro.
 * 
 * <p>Robôs engenheiros são especializados em descobrir peças quando trabalham
 * em prédios comerciais. Também fornecem bônus de produção para trabalhadores.
 * Consomem 50% mais energia ao trabalhar.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class Engenheiro extends Robo {
    /**
     * Construtor padrão para deserialização JSON (Jackson).
     */
    public Engenheiro() {
        super();
        super.setTipo(TipoDeRobo.ENGENHEIRO);
    }
    
    /**
     * Construtor para criar um robô engenheiro em uma posição específica.
     * 
     * @param x Posição X inicial no mapa
     * @param y Posição Y inicial no mapa
     */
    public Engenheiro(int x, int y){
        super(TipoDeRobo.ENGENHEIRO, x, y);
    }
}
