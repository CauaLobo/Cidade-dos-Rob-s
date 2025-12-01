package model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.UUID;

/**
 * Classe abstrata que representa um robô na cidade.
 * Define os atributos e comportamentos básicos de todos os tipos de robôs.
 * 
 * <p>Os robôs possuem energia, felicidade e integridade que variam durante o jogo.
 * Podem trabalhar, entrar em manutenção, dormir e serem afetados por eventos aleatórios.
 * 
 * <p>Esta classe é serializada/deserializada usando Jackson com suporte a polimorfismo.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipo"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Trabalhador.class, name = "TRABALHADOR"),
    @JsonSubTypes.Type(value = Engenheiro.class, name = "ENGENHEIRO"),
    @JsonSubTypes.Type(value = Seguranca.class, name = "SEGURANCA")
})
public abstract class Robo {
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

    /**
     * Construtor principal para criar um robô.
     * 
     * @param tipo O tipo do robô (TRABALHADOR, ENGENHEIRO ou SEGURANCA)
     * @param x Posição X inicial do robô no mapa
     * @param y Posição Y inicial do robô no mapa
     */
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

    /**
     * Construtor padrão para deserialização JSON (Jackson).
     * Inicializa todos os campos com valores padrão.
     */
    public Robo(){
        this.id = UUID.randomUUID().toString();
        this.energia = 100.0;
        this.felicidade = 100.0;
        this.integridade = 100.0;
        this.TurnosDesdeAManutencao = 0;
        this.posX = 0;
        this.posY = 0;
        this.emManutencao = false;
        this.turnosRestantesManutencao = 0;
    }

    /**
     * Simula o trabalho do robô, consumindo energia e integridade.
     * Engenheiros consomem 50% mais energia que outros tipos.
     * Se energia ou integridade ficarem baixas (≤40), a felicidade diminui.
     */
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

    /**
     * Simula o descanso do robô, recuperando energia e felicidade.
     * Aumenta felicidade em 20% e energia em 40% (limitado a 100%).
     */
    public void dormir(){
        this.felicidade = Math.min(100.0, this.felicidade + 20.0);
        this.energia = Math.min(100.0, this.energia + 40.0);
    }

    /**
     * Aplica os efeitos de um apagão de energia no robô.
     * Reduz energia e felicidade em 40 pontos.
     */
    public void apagao(){
        this.felicidade = Math.max(0, this.felicidade - 40.0);
        this.energia = Math.max(0, this.energia - 40.0);
    }

    /**
     * Aplica os efeitos de uma greve no robô.
     * Reduz felicidade em 30 pontos. Durante a greve, os robôs não trabalham.
     */
    public void greve(){
        this.felicidade = Math.max(0, this.felicidade - 30.0);
        // Durante a greve, os robôs não trabalham, então não consomem energia
        // mas ficam insatisfeitos
    }

    /**
     * Aplica os efeitos de uma descoberta de peças raras.
     * Aumenta felicidade em 15 pontos (limitado a 100%).
     */
    public void descobertaPecasRaras(){
        this.felicidade = Math.min(100.0, this.felicidade + 15.0);
        // Os robôs ficam felizes com a descoberta de peças raras
    }

    /**
     * Processa o consumo diário de recursos do robô.
     * Consome energia e integridade a cada turno.
     * Se o robô está em manutenção, processa a manutenção ao invés de consumir recursos.
     */
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
