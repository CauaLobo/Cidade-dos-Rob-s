package model;

public class RoboEmTreinamento {

    private TipoDeRobo tipo;
    private int tempoRestante;

    public RoboEmTreinamento(TipoDeRobo tipo, int tempoTotal) {
        this.tipo = tipo;
        this.tempoRestante = tempoTotal;
    }

    public void reduzirTempoRestante() {
        if (tempoRestante > 0) {
            tempoRestante--;
        }
    }

    // --- Getters ---
    public TipoDeRobo getTipo() {
        return tipo;
    }

    public int getTempoRestante() {
        return tempoRestante;
    }
}
