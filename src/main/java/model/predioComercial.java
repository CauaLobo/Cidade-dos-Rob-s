package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um prédio comercial.
 * 
 * <p>Prédios comerciais são onde robôs trabalhadores e engenheiros podem trabalhar
 * para gerar recursos. Trabalhadores geram dinheiro e engenheiros descobrem peças.
 * Engenheiros também fornecem bônus de produção para trabalhadores.
 * 
 * <p>Suporta até 5 robôs simultaneamente. Apenas trabalhadores e engenheiros podem trabalhar aqui.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class predioComercial extends Predio {

    private List<Robo> Robos;
    private double taxaDinheiro;
    private int taxaPecas;
    private final int maxRobos = 5;

    /**
     * Construtor padrão para deserialização JSON (Jackson).
     */
    public predioComercial() {
        super(TipoPredio.COMERCIAL, 500, 300, 0, 0, 2, 2);
        this.taxaDinheiro = 50.0;
        this.taxaPecas = 25;
        this.Robos = new ArrayList<>();
    }

    /**
     * Construtor para criar um prédio comercial em uma posição específica.
     * 
     * @param x Posição X no mapa
     * @param y Posição Y no mapa
     */
    public predioComercial(int x, int y){
        super(TipoPredio.COMERCIAL, 500, 300, x, y, 2, 2);
        this.taxaDinheiro = 50.0;
        this.Robos = new ArrayList<>();
        this.taxaPecas = 25;
    }

    /**
     * Adiciona um robô ao prédio comercial.
     * Apenas trabalhadores e engenheiros podem trabalhar aqui.
     * 
     * @param robo O robô a ser adicionado
     * @return true se o robô foi adicionado com sucesso, false caso contrário
     */
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

    /**
     * Remove um robô do prédio comercial.
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
    
    // Getters e setters para campos adicionais
    public double getTaxaDinheiro() {
        return taxaDinheiro;
    }
    
    public void setTaxaDinheiro(double taxaDinheiro) {
        this.taxaDinheiro = taxaDinheiro;
    }
    
    public int getTaxaPecas() {
        return taxaPecas;
    }
    
    public void setTaxaPecas(int taxaPecas) {
        this.taxaPecas = taxaPecas;
    }

    /**
     * Aplica o efeito do prédio comercial na cidade a cada turno.
     * 
     * <p>Gera recursos baseado nos robôs trabalhando:
     * <ul>
     *   <li>Trabalhadores geram dinheiro (taxaDinheiro por trabalhador)</li>
     *   <li>Engenheiros descobrem peças (taxaPecas por engenheiro)</li>
     *   <li>Engenheiros fornecem bônus de 20% na produção de dinheiro dos trabalhadores</li>
     * </ul>
     * 
     * <p>Robôs só trabalham se tiverem integridade > 30, energia > 30 e não estiverem em manutenção.
     * 
     * @param city A cidade onde o prédio está localizado
     */
    @Override
    public void efeito(City city){
        // Só gera recursos se houver robôs trabalhando no prédio
        if (Robos.isEmpty()){
            return; // Sem robôs, sem produção
        }
        
        int qtdEngenheiros = 0;
        int qtdTrabalhadores = 0;
        
        // Processa cada robô trabalhando
        for (Robo robo : Robos){
            // Só trabalha se estiver com energia e integridade suficientes
            if (robo.getIntegridade() > 30 && robo.getEnergia() > 30 && !robo.isEmManutencao()) {
                if (robo.getTipo() == TipoDeRobo.ENGENHEIRO) {
                    // Engenheiro descobre peças
                    qtdEngenheiros++;
                    city.addPecas(taxaPecas);
                } else if (robo.getTipo() == TipoDeRobo.TRABALHADOR) {
                    // Trabalhador gera dinheiro
                    qtdTrabalhadores++;
                    city.addDinheiro(taxaDinheiro);
                }
                robo.trabalho();
            }
        }
        
        // Bônus de engenheiros: aumenta a produção dos trabalhadores (máximo 1 engenheiro conta)
        if (qtdEngenheiros > 0 && qtdTrabalhadores > 0) {
            double bonusDinheiro = taxaDinheiro * qtdTrabalhadores * 0.20;
            city.addDinheiro(bonusDinheiro);
        }
    }
}
