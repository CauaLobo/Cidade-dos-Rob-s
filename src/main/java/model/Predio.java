package model;

import java.util.UUID;

public abstract class Predio {

    private String id;
    private TipoPredio tipo;
    private double custoDinheiro;
    private int custoPecas;
    private int posX;
    private int posY;
    private int largura;
    private int altura;

    public Predio(TipoPredio tipo, double dinheiro, int pecas, int x, int y, int largura, int altura){
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.custoDinheiro = dinheiro;
        this.custoPecas = pecas;
        this.posX = x;
        this.posY = y;
        this.largura = largura;
        this.altura = altura;
    }

    public abstract void efeito(City city);


    public TipoPredio getTipo() {
        return tipo;
    }

    public void setTipo(TipoPredio tipo) {
        this.tipo = tipo;
    }

    public double getCustoDinheiro() {
        return custoDinheiro;
    }

    public void setCustoDinheiro(double custoDinheiro) {
        this.custoDinheiro = custoDinheiro;
    }

    public int getCustoPecas() {
        return custoPecas;
    }

    public void setCustoPecas(int custoPecas) {
        this.custoPecas = custoPecas;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
