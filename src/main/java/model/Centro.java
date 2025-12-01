package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe que representa o prédio central (Centro de Treinamento).
 * 
 * <p>O Centro é responsável por treinar novos robôs. Mantém uma fila de treinamento
 * e processa o treinamento a cada turno. Diferentes tipos de robôs têm tempos de
 * treinamento diferentes:
 * <ul>
 *   <li>Trabalhador: 2 turnos</li>
 *   <li>Engenheiro: 4 turnos</li>
 *   <li>Segurança: 3 turnos</li>
 * </ul>
 * 
 * <p>O Centro não é visível no mapa, mas sua funcionalidade permanece ativa.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class Centro extends Predio {

    private double custoTreinamentoDinheiro;
    private int custoTreinamentoPecas;
    private int tempoTreinamentoTurnos;
    private List<RoboEmTreinamento> filaDeTreinamento;

    /**
     * Construtor padrão para deserialização JSON (Jackson).
     */
    public Centro() {
        super(TipoPredio.CENTRO, 0.0, 0, 0, 0, 3, 3);
        this.custoTreinamentoDinheiro = 200.0;
        this.custoTreinamentoPecas = 50;
        this.tempoTreinamentoTurnos = 3;
        this.filaDeTreinamento = new ArrayList<>();
    }
    
    /**
     * Construtor para criar o Centro em uma posição específica.
     * 
     * @param x Posição X no mapa
     * @param y Posição Y no mapa
     */
    public Centro(int x, int y) {
        super(TipoPredio.CENTRO, 0.0, 0, x, y, 3, 3);
        this.custoTreinamentoDinheiro = 200.0;
        this.custoTreinamentoPecas = 50;
        this.tempoTreinamentoTurnos = 3;
        this.filaDeTreinamento = new ArrayList<>();
    }

    /**
     * Cria uma instância de robô baseado no tipo especificado.
     * 
     * @param tipo O tipo de robô a ser criado
     * @param x Posição X onde o robô será criado
     * @param y Posição Y onde o robô será criado
     * @return Uma nova instância do robô do tipo especificado
     * @throws IllegalArgumentException Se o tipo de robô for inválido
     */
    private Robo criarRoboPorTipo(TipoDeRobo tipo, int x, int y) {
        // Assume que todos os robôs são criados na mesma coordenada da Academia
        switch (tipo) {
            case TRABALHADOR:
                return new Trabalhador(x, y);
            case ENGENHEIRO:
                return new Engenheiro(x, y);
            case SEGURANCA:
                return new Seguranca(x, y);
            default:
                // Nunca deve acontecer se o Enum estiver completo
                throw new IllegalArgumentException("Tipo de robô inválido para treinamento.");
        }
    }

    /**
     * Aplica o efeito do Centro na cidade a cada turno.
     * 
     * <p>Processa a fila de treinamento, reduzindo o tempo restante de cada robô em treinamento.
     * Quando o treinamento é concluído, cria o robô e o adiciona à cidade.
     * 
     * @param city A cidade onde o Centro está localizado
     */
    @Override
    public void efeito(City city){
        if (this.filaDeTreinamento.isEmpty()) {
            return; // Nada para fazer
        }

        List<Robo> robosProntos = new ArrayList<>();

        // Usar Iterator é crucial para remover itens da lista durante a iteração
        Iterator<RoboEmTreinamento> iterador = this.filaDeTreinamento.iterator();

        while (iterador.hasNext()) {
            RoboEmTreinamento item = iterador.next();

            // 1. Reduz o tempo restante em 1 turno
            item.reduzirTempoRestante();

            // 2. Verifica se o treinamento foi concluído
            if (item.getTempoRestante() <= 0) {

                // 3. Instancia o robô na posição da Academia
                Robo novoRobo = criarRoboPorTipo(
                        item.getTipo(),
                        this.getPosX(),
                        this.getPosY()
                );

                robosProntos.add(novoRobo);

                // 4. Remove o item concluído da fila
                iterador.remove();
            }
        }

        // 5. Adiciona os novos robôs à cidade
        for (Robo robo : robosProntos) {
            city.addRobo(robo);
            // O Controller pode ser notificado aqui para atualizar a View com o novo robô.
        }
    }

    /**
     * Inicia o treinamento de um novo robô.
     * 
     * <p>Adiciona o robô à fila de treinamento com o tempo apropriado:
     * <ul>
     *   <li>Trabalhador: 2 turnos</li>
     *   <li>Engenheiro: 4 turnos</li>
     *   <li>Segurança: 3 turnos</li>
     * </ul>
     * 
     * @param tipo O tipo de robô a ser treinado
     */
    public void iniciarTreinamento(TipoDeRobo tipo) {
        // Tempos diferentes de treinamento por tipo de robô
        int tempoTreinamento;
        switch (tipo) {
            case TRABALHADOR:
                tempoTreinamento = 2; // Trabalhadores são mais rápidos de treinar
                break;
            case ENGENHEIRO:
                tempoTreinamento = 4; // Engenheiros precisam de mais tempo
                break;
            case SEGURANCA:
                tempoTreinamento = 3; // Seguranças têm tempo médio
                break;
            default:
                tempoTreinamento = this.tempoTreinamentoTurnos; // Usa o padrão
        }
        this.filaDeTreinamento.add(new RoboEmTreinamento(tipo, tempoTreinamento));
    }

    public double getCustoTreinamentoDinheiro() {
        return custoTreinamentoDinheiro;
    }

    public int getCustoTreinamentoPecas() {
        return custoTreinamentoPecas;
    }

    public List<RoboEmTreinamento> getFilaDeTreinamento() {
        if (filaDeTreinamento == null) {
            filaDeTreinamento = new ArrayList<>();
        }
        // Retorna a lista original para Jackson poder serializar corretamente
        return filaDeTreinamento;
    }
    
    // Setter para Jackson deserializar
    public void setFilaDeTreinamento(List<RoboEmTreinamento> fila) {
        if (fila == null) {
            this.filaDeTreinamento = new ArrayList<>();
        } else {
            this.filaDeTreinamento = new ArrayList<>(fila);
        }
    }
    
    // Setters adicionais para campos do Centro
    public void setCustoTreinamentoDinheiro(double valor) {
        this.custoTreinamentoDinheiro = valor;
    }
    
    public void setCustoTreinamentoPecas(int valor) {
        this.custoTreinamentoPecas = valor;
    }
    
    public void setTempoTreinamentoTurnos(int tempo) {
        this.tempoTreinamentoTurnos = tempo;
    }
    
    public int getTempoTreinamentoTurnos() {
        return tempoTreinamentoTurnos;
    }
}
