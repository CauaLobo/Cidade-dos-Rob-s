package model;

/**
 * Enum que define os tipos de robôs disponíveis no jogo.
 * 
 * <ul>
 *   <li>TRABALHADOR: Gera dinheiro em prédios comerciais</li>
 *   <li>ENGENHEIRO: Descobre peças em prédios comerciais e fornece bônus de produção</li>
 *   <li>SEGURANCA: Aumenta a felicidade média e reduz impacto de eventos negativos</li>
 * </ul>
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public enum TipoDeRobo {
    /** Robô trabalhador que gera dinheiro */
    TRABALHADOR,
    /** Robô engenheiro que descobre peças e fornece bônus */
    ENGENHEIRO,
    /** Robô de segurança que aumenta felicidade e protege contra eventos */
    SEGURANCA
}
