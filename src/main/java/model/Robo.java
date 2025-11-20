package model;

import java.util.UUID;

public class Robo {
    private String id;
    private TipoDeRobo tipo;
    private double energia;
    private double felicidade;
    private double integridade;
    private int TurnosDesdeAManutencao;
    private int posX;
    private int posY;

    public Robo(TipoDeRobo tipo, int x, int y){
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.energia = 100.0;
        this.felicidade = 100.0;
        this.integridade = 100.0;
        this.TurnosDesdeAManutencao = 0;
        this.posX = x;
        this.posY = y;
    }

    public Robo(){}

    public void trabalho(){
        double consumoBase = 10.0;

        if (this.tipo == TipoDeRobo.ENGENHEIRO) {
            consumoBase *= 1.5;
        }

        this.energia = Math.max(0, this.energia - consumoBase);
        this.integridade = Math.max(0, this.integridade - 5.0);


        if (this.energia <= 40.0 || this.integridade <= 40.0) {
            this.felicidade = Math.max(0, this.felicidade - 10.0);

        }

    }

    public void manutencao(){
        this.integridade = 100.0;
        this.TurnosDesdeAManutencao = 0;
        this.felicidade = Math.min(100.0, this.felicidade + 20.0);
    }

    public void dormir(){
        this.felicidade = Math.min(100.0, this.felicidade + 20.0);
        this.energia = Math.min(100.0, this.energia + 40.0);
    }

    public void apagao(){
        this.felicidade = Math.max(0, this.felicidade - 40.0);
        this.energia = Math.max(0, this.energia - 40.0);
    }

    public void consumoDiario() {
        this.energia = Math.max(0, this.energia - 10);
        this.integridade = Math.max(0, this.integridade - 5.0);


        if (this.energia <= 40.0 || this.integridade <= 40.0) {
            this.felicidade = Math.max(0, this.felicidade - 10.0);
        }
        TurnosDesdeAManutencao++;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TipoDeRobo getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeRobo tipo) {
        this.tipo = tipo;
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        this.energia = energia;
    }

    public double getFelicidade() {
        return felicidade;
    }

    public void setFelicidade(double felicidade) {
        this.felicidade = felicidade;
    }

    public double getIntegridade() {
        return integridade;
    }

    public void setIntegridade(double integridade) {
        this.integridade = integridade;
    }
}
