package model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.UUID;

/**
 * Classe abstrata que representa um prédio na cidade.
 * Define os atributos e comportamentos básicos de todos os tipos de prédios.
 * 
 * <p>Os prédios possuem posição, dimensões, custos de construção e um efeito
 * que é aplicado a cada turno na cidade.
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
    @JsonSubTypes.Type(value = Centro.class, name = "CENTRO"),
    @JsonSubTypes.Type(value = predioComercial.class, name = "COMERCIAL"),
    @JsonSubTypes.Type(value = predioResidencial.class, name = "RESIDENCIAL"),
    @JsonSubTypes.Type(value = PredioDecorativo.class, name = "MONUMENTO"),
    @JsonSubTypes.Type(value = PredioDecorativo.class, name = "TORRE_COMUNICACAO"),
    @JsonSubTypes.Type(value = PredioDecorativo.class, name = "ESTACAO_ENERGIA"),
    @JsonSubTypes.Type(value = PredioDecorativo.class, name = "JARDIM_ZEN"),
    @JsonSubTypes.Type(value = PredioDecorativo.class, name = "OBSERVATORIO")
})
public abstract class Predio {

    private String id;
    private TipoPredio tipo;
    private double custoDinheiro;
    private int custoPecas;
    private int posX;
    private int posY;
    private int largura;
    private int altura;

    /**
     * Construtor padrão para deserialização JSON (Jackson).
     * Gera um ID único para o prédio.
     */
    public Predio() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Construtor principal para criar um prédio.
     * 
     * @param tipo O tipo do prédio
     * @param dinheiro Custo em dinheiro para construir
     * @param pecas Custo em peças para construir
     * @param x Posição X no mapa
     * @param y Posição Y no mapa
     * @param largura Largura do prédio em células
     * @param altura Altura do prédio em células
     */
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

    /**
     * Método abstrato que define o efeito do prédio na cidade a cada turno.
     * Cada tipo de prédio implementa seu próprio efeito.
     * 
     * @param city A cidade onde o prédio está localizado
     */
    public abstract void efeito(City city);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
}
