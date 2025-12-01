package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para prédios decorativos que não têm função no jogo.
 * Robôs podem ser movidos para esses prédios para explorar o mapa,
 * mas eles não produzem recursos ou têm efeitos especiais.
 */
public class PredioDecorativo extends Predio {

    private List<Robo> Robos;
    private final int maxRobos;

    // Construtor padrão para Jackson
    public PredioDecorativo() {
        super();
        this.Robos = new ArrayList<>();
        this.maxRobos = 3; // Permite até 3 robôs explorando
    }

    /**
     * Cria um prédio decorativo.
     * @param tipo O tipo de prédio decorativo
     * @param x Posição X no mapa
     * @param y Posição Y no mapa
     * @param largura Largura do prédio
     * @param altura Altura do prédio
     */
    public PredioDecorativo(TipoPredio tipo, int x, int y, int largura, int altura) {
        super(tipo, 0.0, 0, x, y, largura, altura);
        this.Robos = new ArrayList<>();
        // Define maxRobos baseado no tipo (alguns podem ser maiores)
        if (tipo == TipoPredio.MONUMENTO || tipo == TipoPredio.JARDIM_ZEN) {
            this.maxRobos = 5; // Monumentos e jardins podem ter mais robôs
        } else {
            this.maxRobos = 3; // Outros prédios menores
        }
    }

    /**
     * Adiciona um robô ao prédio decorativo (apenas para exploração).
     * @param robo O robô a ser adicionado
     * @return true se o robô foi adicionado com sucesso
     */
    public boolean addRobo(Robo robo) {
        if (robo == null) {
            return false;
        }
        if (!this.Robos.contains(robo) && this.Robos.size() < maxRobos) {
            this.Robos.add(robo);
            return true;
        }
        return false;
    }

    /**
     * Remove um robô do prédio decorativo.
     * @param robo O robô a ser removido
     */
    public void removeRobo(Robo robo) {
        this.Robos.remove(robo);
    }

    /**
     * Retorna a lista de robôs no prédio decorativo.
     * @return Lista de robôs
     */
    public List<Robo> getRobos() {
        if (Robos == null) {
            Robos = new ArrayList<>();
        }
        return Robos;
    }

    /**
     * Define a lista de robôs (para deserialização JSON).
     * @param robos Lista de robôs
     */
    public void setRobos(List<Robo> robos) {
        if (robos == null) {
            this.Robos = new ArrayList<>();
        } else {
            this.Robos = new ArrayList<>(robos);
        }
    }

    /**
     * Prédios decorativos não têm efeito no jogo.
     * Robôs apenas exploram, mas não recebem benefícios.
     * @param city A cidade (não utilizado)
     */
    @Override
    public void efeito(City city) {
        // Prédios decorativos não têm função
        // Robôs podem estar aqui apenas para exploração visual
    }
}

