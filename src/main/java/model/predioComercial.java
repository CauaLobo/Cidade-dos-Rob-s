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
        // Aceita TRABALHADOR e ENGENHEIRO (engenheiros dão bônus de produção)
        if (robo.getTipo() != TipoDeRobo.TRABALHADOR && robo.getTipo() != TipoDeRobo.ENGENHEIRO){
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

    public List<Robo> getRobos() {
        return new ArrayList<>(Robos); // Retorna cópia para evitar modificações externas
    }

    @Override
    public void efeito(City city){
        int qtdEngenheiros = 0;
        int qtdTrabalhadores = 0;
        
        if (!Robos.isEmpty()){
            for (Robo robo : Robos){
                if (robo.getIntegridade() > 30 && robo.getEnergia() > 30) {
                    if (robo.getTipo() == TipoDeRobo.ENGENHEIRO) {
                        qtdEngenheiros++;
                    } else if (robo.getTipo() == TipoDeRobo.TRABALHADOR) {
                        qtdTrabalhadores++;
                        city.addDinheiro(taxaDinheiro);
                        city.addPecas(taxaPecas);
                    }
                    robo.trabalho();
                }
            }
        }
        
        // Bônus de engenheiros: +20% de dinheiro e +15% de peças (máximo 1 engenheiro conta)
        if (qtdEngenheiros > 0) {
            double bonusDinheiro = taxaDinheiro * qtdTrabalhadores * 0.20;
            int bonusPecas = (int)(taxaPecas * qtdTrabalhadores * 0.15);
            city.addDinheiro(bonusDinheiro);
            city.addPecas(bonusPecas);
        }
        
        city.addPecas(15); // valores base caso nenhum Robo opere
        city.addDinheiro(50);
    }
}
