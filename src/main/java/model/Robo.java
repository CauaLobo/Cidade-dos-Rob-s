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
    private boolean emManutencao;
    private int turnosRestantesManutencao;

    public Robo(TipoDeRobo tipo, int x, int y){
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.energia = 100.0;
        this.felicidade = 100.0;
        this.integridade = 100.0;
        this.TurnosDesdeAManutencao = 0;
        this.posX = x;
        this.posY = y;
        this.emManutencao = false;
        this.turnosRestantesManutencao = 0;
    }

    public Robo(){
        this.emManutencao = false;
        this.turnosRestantesManutencao = 0;
    }

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

    /**
     * Inicia a manutenção do robô. O robô ficará em manutenção por 2 turnos.
     */
    public void iniciarManutencao(){
        this.emManutencao = true;
        this.turnosRestantesManutencao = 2; // 2 turnos de manutenção
    }
    
    /**
     * Processa um turno de manutenção. Deve ser chamado a cada turno.
     * Quando a manutenção terminar, restaura integridade e felicidade.
     */
    public void processarManutencao(){
        if (emManutencao && turnosRestantesManutencao > 0) {
            turnosRestantesManutencao--;
            
            // Quando a manutenção terminar
            if (turnosRestantesManutencao <= 0) {
                this.integridade = 100.0;
                this.TurnosDesdeAManutencao = 0;
                this.felicidade = Math.min(100.0, this.felicidade + 20.0);
                this.emManutencao = false;
            }
        }
    }
    
    /**
     * Método legado mantido para compatibilidade.
     * Agora apenas inicia a manutenção.
     * @deprecated Use iniciarManutencao() e processarManutencao() separadamente
     */
    @Deprecated
    public void manutencao(){
        iniciarManutencao();
    }

    public void dormir(){
        this.felicidade = Math.min(100.0, this.felicidade + 20.0);
        this.energia = Math.min(100.0, this.energia + 40.0);
    }

    public void apagao(){
        this.felicidade = Math.max(0, this.felicidade - 40.0);
        this.energia = Math.max(0, this.energia - 40.0);
    }

    public void greve(){
        this.felicidade = Math.max(0, this.felicidade - 30.0);
        // Durante a greve, os robôs não trabalham, então não consomem energia
        // mas ficam insatisfeitos
    }

    public void descobertaPecasRaras(){
        this.felicidade = Math.min(100.0, this.felicidade + 15.0);
        // Os robôs ficam felizes com a descoberta de peças raras
    }

    public void consumoDiario() {
        // Se está em manutenção, processa a manutenção e não consome recursos
        if (emManutencao) {
            processarManutencao();
            return; // Robôs em manutenção não consomem energia/integridade
        }
        
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

    public int getTurnosDesdeAManutencao() {
        return TurnosDesdeAManutencao;
    }

    public void setTurnosDesdeAManutencao(int turnosDesdeAManutencao) {
        TurnosDesdeAManutencao = turnosDesdeAManutencao;
    }

    public boolean isEmManutencao() {
        return emManutencao;
    }

    public void setEmManutencao(boolean emManutencao) {
        this.emManutencao = emManutencao;
    }

    public int getTurnosRestantesManutencao() {
        return turnosRestantesManutencao;
    }

    public void setTurnosRestantesManutencao(int turnosRestantesManutencao) {
        this.turnosRestantesManutencao = turnosRestantesManutencao;
    }
}
