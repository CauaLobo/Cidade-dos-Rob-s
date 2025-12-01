package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um prédio residencial.
 * 
 * <p>Prédios residenciais são onde robôs podem descansar e recuperar energia e felicidade.
 * Qualquer tipo de robô pode ficar em um prédio residencial.
 * 
 * <p>Suporta até 5 robôs simultaneamente. A cada turno, robôs aqui recuperam energia e felicidade.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class predioResidencial extends Predio {

    private List<Robo> Robos;
    private final int maxRobos = 5;

    /**
     * Construtor padrão para deserialização JSON (Jackson).
     */
    public predioResidencial() {
        super(TipoPredio.RESIDENCIAL, 500, 300, 0, 0, 2, 2);
        this.Robos = new ArrayList<>();
    }

    /**
     * Construtor para criar um prédio residencial em uma posição específica.
     * 
     * @param x Posição X no mapa
     * @param y Posição Y no mapa
     */
    public predioResidencial(int x, int y){
        super(TipoPredio.RESIDENCIAL, 500, 300, x, y, 2 , 2);
        this.Robos = new ArrayList<>();
    }

    /**
     * Adiciona um robô ao prédio residencial.
     * Qualquer tipo de robô pode ficar aqui.
     * 
     * @param robo O robô a ser adicionado
     * @return true se o robô foi adicionado com sucesso, false caso contrário
     */
    public boolean addRobo(Robo robo){
        if (!this.Robos.contains(robo) && this.Robos.size() < maxRobos){
            this.Robos.add(robo);
            return true;
        }
        return false;
    }

    /**
     * Remove um robô do prédio residencial.
     * 
     * @param robo O robô a ser removido
     */
    public void removeRobo(Robo robo){
        this.Robos.remove(robo);
    }

    public List<Robo> getRobos() {
        if (Robos == null) {
            Robos = new ArrayList<>();
        }
        // Retorna a lista original para Jackson poder serializar corretamente
        return Robos;
    }
    
    // Setter para Jackson deserializar
    public void setRobos(List<Robo> robos) {
        if (robos == null) {
            this.Robos = new ArrayList<>();
        } else {
            this.Robos = new ArrayList<>(robos);
        }
    }

    /**
     * Aplica o efeito do prédio residencial na cidade a cada turno.
     * 
     * <p>Robôs que estão no prédio residencial recuperam energia e felicidade
     * através do método dormir().
     * 
     * @param city A cidade onde o prédio está localizado (não utilizado diretamente)
     */
    @Override
    public void efeito(City city){
        if (!Robos.isEmpty()){
            for (Robo robo : Robos){
                robo.dormir();
            }
        }
    }
}
