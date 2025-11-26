package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Centro extends Predio {

    private double custoTreinamentoDinheiro;
    private int custoTreinamentoPecas;
    private int tempoTreinamentoTurnos;
    private List<RoboEmTreinamento> filaDeTreinamento;

    public Centro(int x, int y) {
        super(TipoPredio.CENTRO, 0.0, 0, x, y, 3, 3);
        this.custoTreinamentoDinheiro = 200.0;
        this.custoTreinamentoPecas = 50;
        this.tempoTreinamentoTurnos = 3;
        this.filaDeTreinamento = new ArrayList<>();
    }

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
        return new ArrayList<>(filaDeTreinamento); // Retorna cópia
    }
}
