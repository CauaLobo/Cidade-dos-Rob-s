package model;

import java.util.ArrayList;
import java.util.List;

public class City {
    private double dinheiro;
    private int pecas;
    private String nome;
    private int turnoAtual;
    private List<Robo> robos;
    private List<Predio> Predios;
    private double felicidadeMedia;
    private static final int MAP_size = 30;

    public City(String nome){
        this.dinheiro = 15000.0;
        this.pecas = 10000;
        this.nome = nome;
        this.turnoAtual = 0;
        this.robos = new ArrayList<>();
        this.Predios = new ArrayList<>();
        this.felicidadeMedia = 100.0;

        this.Predios.add(new Centro(MAP_size/2, MAP_size/2));
    }

    public City(){}

    public boolean gastarDinheiro(double valor){
        if (this.dinheiro >= valor){
            this.dinheiro -= valor;
            return true;
        }
        return false;
    }

    public boolean gastarPecas(int valor){
        if (this.pecas >= valor){
            this.pecas -= valor;
            return true;
        }
        return false;
    }

    public void addDinheiro(double valor){
        this.dinheiro += valor;
    }

    public void addPecas(int valor){
        this.pecas += valor;
    }

    public void felicidadeMedia(){
        if (!robos.isEmpty()){
            double media = 0;
            for (Robo robo : robos){
                media += robo.getFelicidade();
                }
            this.felicidadeMedia = media / robos.size();
        }
    }

    public void addRobo(Robo robo){
        robos.add(robo);
    }

    public void addPredio(Predio predio){
        this.Predios.add(predio);
    }

    public void incrementaTurno(){
        this.turnoAtual ++;
    }

    public double getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(double dinheiro) {
        this.dinheiro = dinheiro;
    }

    public int getPecas() {
        return pecas;
    }

    public void setPecas(int pecas) {
        this.pecas = pecas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTurnoAtual() {
        return turnoAtual;
    }

    public void setTurnoAtual(int turnoAtual) {
        this.turnoAtual = turnoAtual;
    }

    public List<Robo> getRobos() {
        return robos;
    }

    public void setRobos(List<Robo> Robos) {
        this.robos = Robos;
    }

    public List<Predio> getPredios() {
        return Predios;
    }

    public void setPredios(List<Predio> Predios) {
        this.Predios = Predios;
    }

    public double getFelicidadeMedia() {
        return felicidadeMedia;
    }

    public void setFelicidadeMedia(double felicidadeMedia) {
        this.felicidadeMedia = felicidadeMedia;
    }
}
