package model;

/**
 * Classe que representa um robô em processo de treinamento.
 * 
 * <p>Armazena informações sobre um robô que está sendo treinado no Centro,
 * incluindo seu tipo e o tempo restante de treinamento.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class RoboEmTreinamento {

    private TipoDeRobo tipo;
    private int tempoRestante;

    /**
     * Construtor padrão para deserialização JSON (Jackson).
     */
    public RoboEmTreinamento() {
        this.tempoRestante = 0;
    }

    /**
     * Construtor para criar um robô em treinamento.
     * 
     * @param tipo O tipo de robô sendo treinado
     * @param tempoTotal O tempo total de treinamento em turnos
     */
    public RoboEmTreinamento(TipoDeRobo tipo, int tempoTotal) {
        this.tipo = tipo;
        this.tempoRestante = tempoTotal;
    }

    /**
     * Reduz o tempo restante de treinamento em 1 turno.
     * Não permite que o tempo fique negativo.
     */
    public void reduzirTempoRestante() {
        if (tempoRestante > 0) {
            tempoRestante--;
        }
    }

    // --- Getters e Setters ---
    public TipoDeRobo getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoDeRobo tipo) {
        this.tipo = tipo;
    }

    public int getTempoRestante() {
        return tempoRestante;
    }
    
    public void setTempoRestante(int tempoRestante) {
        this.tempoRestante = tempoRestante;
    }
}
