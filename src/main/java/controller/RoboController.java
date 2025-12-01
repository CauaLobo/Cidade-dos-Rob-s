package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsável por gerenciar todas as operações relacionadas aos robôs.
 * Inclui treinamento, manutenção, alocação em prédios e listagem de robôs.
 */
public class RoboController {
    
    /**
     * Inicia o treinamento de um novo robô no Centro.
     * @param city A cidade onde o treinamento será realizado
     * @param tipoRobo O tipo de robô a ser treinado (TRABALHADOR, ENGENHEIRO, SEGURANCA)
     * @return true se o treinamento foi iniciado com sucesso, false caso contrário
     */
    public boolean treinarRobo(City city, TipoDeRobo tipoRobo) {
        if (city == null || tipoRobo == null) {
            return false;
        }
        
        // Encontra o Centro na cidade
        Centro centro = encontrarCentro(city);
        if (centro == null) {
            return false; // Não há centro na cidade
        }
        
        // Verifica se a cidade tem recursos suficientes
        double custoDinheiro = centro.getCustoTreinamentoDinheiro();
        int custoPecas = centro.getCustoTreinamentoPecas();
        
        if (!city.gastarDinheiro(custoDinheiro) || !city.gastarPecas(custoPecas)) {
            return false; // Recursos insuficientes
        }
        
        // Inicia o treinamento
        centro.iniciarTreinamento(tipoRobo);
        return true;
    }
    
    /**
     * Inicia a manutenção em um robô específico.
     * O robô ficará em manutenção por 2 turnos.
     * @param robo O robô a ser mantido
     * @return true se a manutenção foi iniciada, false caso contrário
     */
    public boolean fazerManutencao(Robo robo) {
        if (robo == null) {
            return false;
        }
        
        // Se já está em manutenção, não pode iniciar outra
        if (robo.isEmManutencao()) {
            return false;
        }
        
        robo.iniciarManutencao();
        return true;
    }
    
    /**
     * Move um robô para um prédio específico.
     * Remove o robô do prédio atual (se estiver em algum) e adiciona ao novo.
     * @param robo O robô a ser movido
     * @param predio O prédio de destino
     * @param city A cidade onde o robô está
     * @return true se o robô foi movido com sucesso, false caso contrário
     */
    public boolean moverRoboParaPredio(Robo robo, Predio predio, City city) {
        if (robo == null || predio == null || city == null) {
            return false;
        }
        
        // Remove o robô do prédio atual (se estiver em algum)
        removerRoboDePredio(robo, city);
        
        // Adiciona o robô ao novo prédio baseado no tipo
        if (predio instanceof predioComercial) {
            predioComercial comercial = (predioComercial) predio;
            return comercial.addRobo(robo);
        } else if (predio instanceof predioResidencial) {
            predioResidencial residencial = (predioResidencial) predio;
            return residencial.addRobo(robo);
        } else if (predio instanceof PredioDecorativo) {
            // Prédios decorativos permitem robôs apenas para exploração
            PredioDecorativo decorativo = (PredioDecorativo) predio;
            return decorativo.addRobo(robo);
        }
        
        return false; // Tipo de prédio não suportado (ex: Centro)
    }
    
    /**
     * Remove um robô de qualquer prédio onde ele esteja.
     * @param robo O robô a ser removido
     * @param city A cidade onde buscar os prédios
     * @return true se o robô foi removido, false caso contrário
     */
    public boolean removerRoboDePredio(Robo robo, City city) {
        if (robo == null || city == null) {
            return false;
        }
        
        // Busca em todos os prédios da cidade
        for (Predio predio : city.getPredios()) {
            if (predio instanceof predioComercial) {
                predioComercial comercial = (predioComercial) predio;
                if (comercial.getRobos().contains(robo)) {
                    comercial.removeRobo(robo);
                    return true;
                }
            } else if (predio instanceof predioResidencial) {
                predioResidencial residencial = (predioResidencial) predio;
                if (residencial.getRobos().contains(robo)) {
                    residencial.removeRobo(robo);
                    return true;
                }
            } else if (predio instanceof PredioDecorativo) {
                PredioDecorativo decorativo = (PredioDecorativo) predio;
                if (decorativo.getRobos().contains(robo)) {
                    decorativo.removeRobo(robo);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Encontra o Centro da cidade.
     * @param city A cidade
     * @return O Centro ou null se não encontrado
     */
    private Centro encontrarCentro(City city) {
        if (city == null) {
            return null;
        }
        
        for (Predio predio : city.getPredios()) {
            if (predio instanceof Centro) {
                return (Centro) predio;
            }
        }
        return null;
    }
    
    /**
     * Lista todos os robôs disponíveis (não alocados em prédios).
     * @param city A cidade
     * @return Lista de robôs disponíveis
     */
    public List<Robo> listarRobosDisponiveis(City city) {
        if (city == null) {
            return new ArrayList<>();
        }
        
        List<Robo> disponiveis = new ArrayList<>(city.getRobos());
        
        // Remove robôs que estão em prédios (incluindo decorativos)
        for (Predio predio : city.getPredios()) {
            if (predio instanceof predioComercial) {
                predioComercial comercial = (predioComercial) predio;
                disponiveis.removeAll(comercial.getRobos());
            } else if (predio instanceof predioResidencial) {
                predioResidencial residencial = (predioResidencial) predio;
                disponiveis.removeAll(residencial.getRobos());
            } else if (predio instanceof PredioDecorativo) {
                PredioDecorativo decorativo = (PredioDecorativo) predio;
                disponiveis.removeAll(decorativo.getRobos());
            }
        }
        
        return disponiveis;
    }
    
    /**
     * Encontra em qual prédio um robô está alocado.
     * @param robo O robô a ser procurado
     * @param city A cidade
     * @return O prédio onde o robô está, ou null se não estiver em nenhum
     */
    public Predio encontrarPredioDoRobo(Robo robo, City city) {
        if (robo == null || city == null) {
            return null;
        }
        
        for (Predio predio : city.getPredios()) {
            if (predio instanceof predioComercial) {
                predioComercial comercial = (predioComercial) predio;
                if (comercial.getRobos().contains(robo)) {
                    return predio;
                }
            } else if (predio instanceof predioResidencial) {
                predioResidencial residencial = (predioResidencial) predio;
                if (residencial.getRobos().contains(robo)) {
                    return predio;
                }
            } else if (predio instanceof PredioDecorativo) {
                PredioDecorativo decorativo = (PredioDecorativo) predio;
                if (decorativo.getRobos().contains(robo)) {
                    return predio;
                }
            }
        }
        return null;
    }
    
    /**
     * Lista todos os robôs de um tipo específico na cidade.
     * @param city A cidade
     * @param tipoRobo O tipo de robô
     * @return Lista de robôs do tipo especificado
     */
    public List<Robo> listarRobosPorTipo(City city, TipoDeRobo tipoRobo) {
        if (city == null || tipoRobo == null) {
            return new ArrayList<>();
        }
        
        List<Robo> robosDoTipo = new ArrayList<>();
        for (Robo robo : city.getRobos()) {
            if (robo.getTipo() == tipoRobo) {
                robosDoTipo.add(robo);
            }
        }
        
        return robosDoTipo;
    }
    
    /**
     * Calcula o bônus de felicidade proporcionado pelos robôs de segurança.
     * Cada segurança adiciona +3 pontos à felicidade média (máximo +15 com 5 seguranças).
     * @param city A cidade
     * @return O bônus de felicidade dos seguranças
     */
    public double calcularBonusFelicidadeSeguranca(City city) {
        if (city == null) {
            return 0.0;
        }
        
        List<Robo> segurancas = listarRobosPorTipo(city, TipoDeRobo.SEGURANCA);
        int qtdSegurancas = Math.min(segurancas.size(), 5); // Máximo de 5 seguranças contam
        return qtdSegurancas * 3.0; // +3 pontos por segurança
    }
    
    /**
     * Aplica o bônus de felicidade dos seguranças à cidade.
     * Este método deve ser chamado após calcular a felicidade média.
     * @param city A cidade
     */
    public void aplicarBonusFelicidadeSeguranca(City city) {
        if (city == null) {
            return;
        }
        
        double bonus = calcularBonusFelicidadeSeguranca(city);
        double felicidadeAtual = city.getFelicidadeMedia();
        city.setFelicidadeMedia(Math.min(100.0, felicidadeAtual + bonus));
    }
    
    /**
     * Deleta um robô da cidade.
     * Remove o robô de qualquer prédio onde esteja e da lista de robôs da cidade.
     * @param robo O robô a ser deletado
     * @param city A cidade onde o robô está
     * @return true se o robô foi deletado com sucesso, false caso contrário
     */
    public boolean deletarRobo(Robo robo, City city) {
        if (robo == null || city == null) {
            return false;
        }
        
        // Primeiro remove o robô de qualquer prédio onde esteja
        removerRoboDePredio(robo, city);
        
        // Remove o robô da lista de robôs da cidade
        return city.getRobos().remove(robo);
    }
}
