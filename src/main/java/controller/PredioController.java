package controller;

import model.*;

import java.util.Map;

public class PredioController {

    private final Map<TipoPredio, CustoConstrucao> TABELA_CUSTOS = Map.of(
            TipoPredio.COMERCIAL, new CustoConstrucao(500.0, 300, 2, 2),
            TipoPredio.RESIDENCIAL, new CustoConstrucao(300.0, 150, 2, 2)
    );

    private record CustoConstrucao(double dinheiro, int pecas, int largura, int altura) {
    }

    private Predio criarInstanciaPredio(TipoPredio tipo, int x, int y, CustoConstrucao custo) {
        switch (tipo) {
            case COMERCIAL:
                return new predioComercial(
                        x, y
                );
            case RESIDENCIAL:
                return new predioResidencial(
                        x, y
                );
            default:
                throw new IllegalArgumentException("Tipo de prédio não mapeado para construção.");
        }
    }

    public boolean construirPredio(TipoPredio tipo, City city, int x, int y) {
        if (tipo == TipoPredio.CENTRO) {return false;}
        CustoConstrucao custo = TABELA_CUSTOS.get(tipo);

        if (!city.gastarDinheiro(custo.dinheiro()) || !city.gastarPecas(custo.pecas())) {return false;}

        Predio novo = criarInstanciaPredio(tipo, x, y, custo);
        city.addPredio(novo);
        return true;
    }

}

