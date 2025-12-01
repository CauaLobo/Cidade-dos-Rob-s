package controller;

import model.*;

import java.util.Map;

/**
 * Controller responsável por gerenciar a construção de prédios.
 * 
 * <p>Mantém uma tabela de custos para cada tipo de prédio e valida
 * recursos, posições e limites do mapa antes de construir.
 * 
 * <p>Prédios decorativos não podem ser construídos pelo jogador,
 * apenas prédios comerciais e residenciais.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class PredioController {

    private final Map<TipoPredio, CustoConstrucao> TABELA_CUSTOS = Map.of(
            TipoPredio.COMERCIAL, new CustoConstrucao(500.0, 300, 2, 2),
            TipoPredio.RESIDENCIAL, new CustoConstrucao(300.0, 150, 2, 2),
            TipoPredio.MONUMENTO, new CustoConstrucao(200.0, 100, 3, 3),
            TipoPredio.TORRE_COMUNICACAO, new CustoConstrucao(150.0, 80, 2, 3),
            TipoPredio.ESTACAO_ENERGIA, new CustoConstrucao(250.0, 150, 3, 3),
            TipoPredio.JARDIM_ZEN, new CustoConstrucao(100.0, 50, 3, 3),
            TipoPredio.OBSERVATORIO, new CustoConstrucao(180.0, 120, 2, 2)
    );

    private record CustoConstrucao(double dinheiro, int pecas, int largura, int altura) {
    }

    /**
     * Cria uma instância de prédio baseado no tipo especificado.
     * 
     * @param tipo O tipo de prédio a ser criado
     * @param x Posição X no mapa
     * @param y Posição Y no mapa
     * @param custo Informações de custo e dimensões
     * @return Uma nova instância do prédio
     * @throws IllegalArgumentException Se o tipo de prédio não for suportado
     */
    private Predio criarInstanciaPredio(TipoPredio tipo, int x, int y, CustoConstrucao custo) {
        switch (tipo) {
            case COMERCIAL:
                return new predioComercial(x, y);
            case RESIDENCIAL:
                return new predioResidencial(x, y);
            case MONUMENTO:
            case TORRE_COMUNICACAO:
            case ESTACAO_ENERGIA:
            case JARDIM_ZEN:
            case OBSERVATORIO:
                return new PredioDecorativo(tipo, x, y, custo.largura(), custo.altura());
            default:
                throw new IllegalArgumentException("Tipo de prédio não mapeado para construção.");
        }
    }

    /**
     * Constrói um prédio na cidade na posição especificada.
     * 
     * <p>Valida:
     * <ul>
     *   <li>Se o tipo de prédio pode ser construído (não permite CENTRO ou decorativos)</li>
     *   <li>Se a posição está dentro dos limites do mapa</li>
     *   <li>Se não há outro prédio na posição</li>
     *   <li>Se a cidade tem recursos suficientes</li>
     * </ul>
     * 
     * @param tipo O tipo de prédio a ser construído
     * @param city A cidade onde o prédio será construído
     * @param x Posição X no mapa
     * @param y Posição Y no mapa
     * @return true se o prédio foi construído com sucesso, false caso contrário
     */
    public boolean construirPredio(TipoPredio tipo, City city, int x, int y) {
        if (tipo == TipoPredio.CENTRO) {
            System.out.println("❌ Centro não pode ser construído");
            return false; // Centro não pode ser construído
        }
        
        // Prédios decorativos não podem ser construídos pelo jogador
        if (tipo == TipoPredio.MONUMENTO || 
            tipo == TipoPredio.TORRE_COMUNICACAO || 
            tipo == TipoPredio.ESTACAO_ENERGIA || 
            tipo == TipoPredio.JARDIM_ZEN || 
            tipo == TipoPredio.OBSERVATORIO) {
            System.out.println("❌ Prédios decorativos não podem ser construídos");
            return false; // Prédios decorativos são apenas decorativos, pré-colocados no mapa
        }
        
        CustoConstrucao custo = TABELA_CUSTOS.get(tipo);
        if (custo == null) {
            System.out.println("❌ Tipo de prédio não suportado: " + tipo);
            return false; // Tipo de prédio não suportado
        }

        // Valida limites do mapa (assumindo mapa 30x30)
        if (x < 0 || y < 0 || x + custo.largura() > 30 || y + custo.altura() > 30) {
            System.out.println("❌ Posição fora dos limites do mapa: (" + x + ", " + y + ") com tamanho " + custo.largura() + "x" + custo.altura());
            return false;
        }

        // Verifica se já existe um prédio na posição
        if (predioExisteNaPosicao(city, x, y, custo.largura(), custo.altura())) {
            System.out.println("❌ Já existe um prédio na posição (" + x + ", " + y + ")");
            return false; // Já existe um prédio nessa posição
        }

        // Verifica recursos
        if (!city.gastarDinheiro(custo.dinheiro())) {
            System.out.println("❌ Dinheiro insuficiente. Necessário: " + custo.dinheiro() + ", Disponível: " + city.getDinheiro());
            return false; // Recursos insuficientes
        }
        
        if (!city.gastarPecas(custo.pecas())) {
            System.out.println("❌ Peças insuficientes. Necessário: " + custo.pecas() + ", Disponível: " + city.getPecas());
            // Reverte o gasto de dinheiro
            city.addDinheiro(custo.dinheiro());
            return false; // Recursos insuficientes
        }

        Predio novo = criarInstanciaPredio(tipo, x, y, custo);
        city.addPredio(novo);
        System.out.println("✓ Prédio " + tipo + " construído com sucesso em (" + x + ", " + y + ")");
        return true;
    }
    
    /**
     * Verifica se já existe um prédio na posição especificada.
     * @param city A cidade
     * @param x Posição X
     * @param y Posição Y
     * @param largura Largura do prédio a ser construído
     * @param altura Altura do prédio a ser construído
     * @return true se já existe um prédio na posição
     */
    private boolean predioExisteNaPosicao(City city, int x, int y, int largura, int altura) {
        for (Predio predio : city.getPredios()) {
            // Ignora o CENTRO (não é visível no mapa e não deve bloquear construção)
            if (predio.getTipo() == TipoPredio.CENTRO) {
                continue;
            }
            
            // Verifica se há sobreposição
            if (predio.getPosX() < x + largura && 
                predio.getPosX() + predio.getLargura() > x &&
                predio.getPosY() < y + altura && 
                predio.getPosY() + predio.getAltura() > y) {
                return true; // Há sobreposição
            }
        }
        return false;
    }

}

