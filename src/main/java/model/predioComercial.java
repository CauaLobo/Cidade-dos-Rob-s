package model;

import java.util.ArrayList;
import java.util.List;

public class predioComercial extends Predio {

    private List<Robo> Robos;
    private double taxaDinheiro;
    private int taxaPecas;
    private final int maxRobos = 5;


    public predioComercial(int x, int y){
        super(TipoPredio.COMERCIAL, 500, 300, x, y, 2, 2);
        this.taxaDinheiro = 50.0;
        this.Robos = new ArrayList<>();
        this.taxaPecas = 25;
    }

    public boolean addRobo(Robo robo){
        if (robo.getTipo() != TipoDeRobo.TRABALHADOR){
            return false;
        }
        if (!this.Robos.contains(robo) && this.Robos.size() < maxRobos){
            this.Robos.add(robo);
            return true;
        }
        return false;
    }

    public void removeRobo(Robo robo){
        this.Robos.remove(robo);
    }

    @Override
    public void efeito(City city){
        if (!Robos.isEmpty()){
            for (Robo robo : Robos){
                if (robo.getIntegridade() > 30 && robo.getEnergia() > 30) {
                    city.addDinheiro(taxaDinheiro);
                    city.addPecas(taxaPecas);
                    robo.trabalho();
                }
            }
        }
        city.addPecas(15); // valores base caso nenhum Robo opere
        city.addDinheiro(50);
    }
}
