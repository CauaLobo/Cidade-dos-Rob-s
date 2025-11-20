package model;

import java.util.ArrayList;
import java.util.List;

public class predioResidencial extends Predio {

    private List<Robo> Robos;
    private final int maxRobos = 5;

    public predioResidencial(int x, int y){
        super(TipoPredio.RESIDENCIAL, 500, 300, x, y, 2 , 2);
        this.Robos = new ArrayList<>();
    }

    public boolean addRobo(Robo robo){
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
                robo.dormir();
            }
        }
    }
}
