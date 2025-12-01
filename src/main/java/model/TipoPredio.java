package model;

/**
 * Enum que define os tipos de prédios disponíveis no jogo.
 * 
 * <ul>
 *   <li>COMERCIAL: Prédio onde trabalhadores e engenheiros geram recursos</li>
 *   <li>RESIDENCIAL: Prédio onde robôs recuperam energia e felicidade</li>
 *   <li>CENTRO: Prédio central de treinamento (não visível no mapa)</li>
 *   <li>MONUMENTO: Prédio decorativo sem função</li>
 *   <li>TORRE_COMUNICACAO: Prédio decorativo sem função</li>
 *   <li>ESTACAO_ENERGIA: Prédio decorativo sem função</li>
 *   <li>JARDIM_ZEN: Prédio decorativo sem função</li>
 *   <li>OBSERVATORIO: Prédio decorativo sem função</li>
 * </ul>
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public enum TipoPredio {
    /** Prédio comercial onde robôs trabalham e geram recursos */
    COMERCIAL,
    /** Prédio residencial onde robôs descansam e recuperam recursos */
    RESIDENCIAL,
    /** Prédio central de treinamento (não visível no mapa) */
    CENTRO,
    /** Prédio decorativo - Monumento */
    MONUMENTO,
    /** Prédio decorativo - Torre de Comunicação */
    TORRE_COMUNICACAO,
    /** Prédio decorativo - Estação de Energia */
    ESTACAO_ENERGIA,
    /** Prédio decorativo - Jardim Zen */
    JARDIM_ZEN,
    /** Prédio decorativo - Observatório */
    OBSERVATORIO
}
