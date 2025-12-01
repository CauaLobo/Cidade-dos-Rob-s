package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma cidade no jogo Cidade dos Robôs.
 * 
 * <p>Gerencia todos os recursos, prédios, robôs e estado geral da cidade.
 * Mantém controle sobre dinheiro, peças, turno atual e felicidade média dos robôs.
 * 
 * <p>A cidade é inicializada com recursos iniciais e prédios decorativos pré-colocados.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class City {
    private double dinheiro;
    private int pecas;
    private String nome;
    private int turnoAtual;
    private List<Robo> robos;
    private List<Predio> Predios;
    private double felicidadeMedia;
    private static final int MAP_size = 30;

    /**
     * Construtor principal para criar uma nova cidade.
     * 
     * <p>Inicializa a cidade com:
     * <ul>
     *   <li>15000 de dinheiro inicial</li>
     *   <li>10000 peças iniciais</li>
     *   <li>Felicidade média em 100%</li>
     *   <li>Um prédio CENTRO no centro do mapa</li>
     *   <li>6 prédios decorativos pré-colocados</li>
     * </ul>
     * 
     * @param nome Nome da cidade
     */
    public City(String nome){
        this.dinheiro = 15000.0;
        this.pecas = 10000;
        this.nome = nome;
        this.turnoAtual = 0;
        this.robos = new ArrayList<>();
        this.Predios = new ArrayList<>();
        this.felicidadeMedia = 100.0;

        this.Predios.add(new Centro(MAP_size/2, MAP_size/2));
        
        // Adiciona prédios decorativos pré-colocados no mapa
        // Monumento 1
        this.Predios.add(new PredioDecorativo(TipoPredio.MONUMENTO, 5, 5, 3, 3));
        // Monumento 2
        this.Predios.add(new PredioDecorativo(TipoPredio.MONUMENTO, 22, 8, 3, 3));
        // Torre de Comunicação
        this.Predios.add(new PredioDecorativo(TipoPredio.TORRE_COMUNICACAO, 15, 3, 2, 3));
        // Estação de Energia
        this.Predios.add(new PredioDecorativo(TipoPredio.ESTACAO_ENERGIA, 8, 20, 3, 3));
        // Jardim Zen
        this.Predios.add(new PredioDecorativo(TipoPredio.JARDIM_ZEN, 20, 20, 3, 3));
        // Observatório
        this.Predios.add(new PredioDecorativo(TipoPredio.OBSERVATORIO, 3, 15, 2, 2));
    }

    /**
     * Construtor padrão para deserialização JSON (Jackson).
     * Inicializa listas vazias. Prédios decorativos são adicionados apenas no construtor com nome.
     */
    public City(){
        this.robos = new ArrayList<>();
        this.Predios = new ArrayList<>();
        // Prédios decorativos são adicionados apenas no construtor com nome
        // Quando carregado do JSON, eles já vêm salvos
    }

    /**
     * Tenta gastar dinheiro da cidade.
     * 
     * @param valor Valor a ser gasto
     * @return true se havia dinheiro suficiente e foi gasto, false caso contrário
     */
    public boolean gastarDinheiro(double valor){
        if (this.dinheiro >= valor){
            this.dinheiro -= valor;
            return true;
        }
        return false;
    }

    /**
     * Tenta gastar peças da cidade.
     * 
     * @param valor Quantidade de peças a ser gasta
     * @return true se havia peças suficientes e foram gastas, false caso contrário
     */
    public boolean gastarPecas(int valor){
        if (this.pecas >= valor){
            this.pecas -= valor;
            return true;
        }
        return false;
    }

    /**
     * Adiciona dinheiro à cidade.
     * 
     * @param valor Valor a ser adicionado
     */
    public void addDinheiro(double valor){
        this.dinheiro += valor;
    }

    /**
     * Adiciona peças à cidade.
     * 
     * @param valor Quantidade de peças a ser adicionada
     */
    public void addPecas(int valor){
        this.pecas += valor;
    }

    /**
     * Calcula e atualiza a felicidade média de todos os robôs da cidade.
     * Se não houver robôs, mantém o valor atual.
     */
    public void felicidadeMedia(){
        if (!robos.isEmpty()){
            double media = 0;
            for (Robo robo : robos){
                media += robo.getFelicidade();
                }
            this.felicidadeMedia = media / robos.size();
        }
    }

    /**
     * Adiciona um robô à cidade.
     * 
     * @param robo O robô a ser adicionado
     */
    public void addRobo(Robo robo){
        robos.add(robo);
    }

    /**
     * Adiciona um prédio à cidade.
     * 
     * @param predio O prédio a ser adicionado
     */
    public void addPredio(Predio predio){
        this.Predios.add(predio);
    }

    /**
     * Incrementa o turno atual da cidade.
     */
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
