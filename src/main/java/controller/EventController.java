package controller;

import model.City;
import model.Robo;
import model.TipoDeRobo;
import model.Turno;

import java.util.Random;

/**
 * Controller responsável por gerenciar eventos aleatórios do jogo.
 * 
 * <p>Eventos possíveis:
 * <ul>
 *   <li>Apagão: Reduz energia e felicidade de todos os robôs</li>
 *   <li>Greve: Reduz felicidade e impede trabalho (mais provável se houver robôs infelizes)</li>
 *   <li>Descoberta de Peças Raras: Aumenta felicidade de todos os robôs</li>
 * </ul>
 * 
 * <p>A chance de eventos é de 40% por turno. A chance de greve aumenta
 * significativamente se houver robôs infelizes (felicidade < 50).
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class EventController {
    private Random random = new Random();
    private final int ChanceDeEvento = 40; // Aumentado de 20% para 40%
    private RoboController roboController = new RoboController();

    /**
     * Verifica se um evento aleatório deve ocorrer neste turno e o aplica.
     * 
     * <p>A chance de um evento ocorrer é de 40%. Se ocorrer, escolhe aleatoriamente
     * entre os três tipos de eventos, com maior probabilidade de greve se houver
     * robôs infelizes na cidade.
     * 
     * @param cidade A cidade onde o evento pode ocorrer
     * @param turnoAtual O turno atual para registrar o evento
     */
    public void verificarEventos(City cidade, Turno turnoAtual) {

        // Determina se um evento aleatório deve ocorrer
        if (random.nextInt(100) < ChanceDeEvento) {

            // Calcula felicidade média para determinar chance de greve
            double felicidadeMedia = calcularFelicidadeMedia(cidade);
            int qtdRobosInfelizes = contarRobosInfelizes(cidade);
            
            // Escolhe o tipo de evento baseado nas condições da cidade
            int tipoEvento;
            
            // Se houver robôs infelizes, aumenta significativamente a chance de greve
            if (qtdRobosInfelizes > 0 && felicidadeMedia < 50) {
                // 60% de chance de greve se houver robôs infelizes
                if (random.nextInt(100) < 60) {
                    tipoEvento = 1; // Greve
                } else {
                    tipoEvento = random.nextInt(3); // Evento aleatório
                }
            } else {
                // Escolhe aleatoriamente entre os três tipos
                tipoEvento = random.nextInt(3);
            }

            switch (tipoEvento) {
                case 0:
                    aplicarApagao(cidade, turnoAtual);
                    break;
                case 1:
                    aplicarGreve(cidade, turnoAtual);
                    break;
                case 2:
                    aplicarDescobertaPecasRaras(cidade, turnoAtual);
                    break;
            }
        }
    }
    
    /**
     * Calcula a felicidade média dos robôs na cidade.
     */
    private double calcularFelicidadeMedia(City cidade) {
        if (cidade.getRobos().isEmpty()) {
            return 100.0;
        }
        
        double soma = 0;
        for (Robo robo : cidade.getRobos()) {
            soma += robo.getFelicidade();
        }
        return soma / cidade.getRobos().size();
    }
    
    /**
     * Conta quantos robôs estão infelizes (felicidade < 50).
     */
    private int contarRobosInfelizes(City cidade) {
        int count = 0;
        for (Robo robo : cidade.getRobos()) {
            if (robo.getFelicidade() < 50) {
                count++;
            }
        }
        return count;
    }

    private void aplicarApagao(City city, Turno turno){
        // Calcula quantos seguranças existem para reduzir o impacto
        int qtdSegurancas = roboController.listarRobosPorTipo(city, TipoDeRobo.SEGURANCA).size();
        double reducaoImpacto = Math.min(0.25, qtdSegurancas * 0.05); // Máximo 25% de redução
        
        for (Robo robo : city.getRobos()){
            robo.apagao();
            
            // Aplica redução de impacto se houver seguranças
            if (qtdSegurancas > 0) {
                // Recupera parte da energia e felicidade perdida
                double energiaRecuperada = 40.0 * reducaoImpacto;
                double felicidadeRecuperada = 40.0 * reducaoImpacto;
                robo.setEnergia(Math.min(100.0, robo.getEnergia() + energiaRecuperada));
                robo.setFelicidade(Math.min(100.0, robo.getFelicidade() + felicidadeRecuperada));
            }
        }
        
        String mensagem = "⚡ APAGÃO DE ENERGIA! Todos os robôs foram gravemente afetados (energia e felicidade -40)";
        if (qtdSegurancas > 0) {
            mensagem += String.format(" (Seguranças reduziram o impacto em %.0f%%)", reducaoImpacto * 100);
        }
        turno.registrarEvento(mensagem);
    }

    private void aplicarGreve(City city, Turno turno){
        if (city.getRobos().isEmpty()) {
            return; // Não há robôs para fazer greve
        }

        // Calcula quantos seguranças existem para reduzir o impacto
        int qtdSegurancas = roboController.listarRobosPorTipo(city, TipoDeRobo.SEGURANCA).size();
        double reducaoImpacto = Math.min(0.25, qtdSegurancas * 0.05); // Máximo 25% de redução
        
        // Verifica quantos robôs estão infelizes
        int qtdRobosInfelizes = contarRobosInfelizes(city);
        double felicidadeMedia = calcularFelicidadeMedia(city);

        for (Robo robo : city.getRobos()){
            robo.greve();
            
            // Aplica redução de impacto se houver seguranças
            if (qtdSegurancas > 0) {
                // Recupera parte da felicidade perdida
                double felicidadeRecuperada = 30.0 * reducaoImpacto;
                robo.setFelicidade(Math.min(100.0, robo.getFelicidade() + felicidadeRecuperada));
            }
        }
        
        String mensagem;
        if (qtdRobosInfelizes > 0 && felicidadeMedia < 50) {
            mensagem = String.format("⚠️ GREVE! %d robô(s) infeliz(es) (felicidade média: %.1f%%) iniciaram uma greve! Os robôs pararam de trabalhar.", 
                    qtdRobosInfelizes, felicidadeMedia);
        } else {
            mensagem = "⚠️ GREVE! Os robôs estão insatisfeitos e pararam de trabalhar. Produção reduzida neste turno.";
        }
        
        if (qtdSegurancas > 0) {
            mensagem += String.format(" (Seguranças reduziram o impacto em %.0f%%)", reducaoImpacto * 100);
        }
        turno.registrarEvento(mensagem);
    }

    private void aplicarDescobertaPecasRaras(City city, Turno turno){
        // Quantidade aleatória de peças raras descobertas (entre 200 e 500)
        int pecasDescobertas = 200 + random.nextInt(301);
        
        // Bônus de dinheiro também (entre 300 e 800)
        double dinheiroDescoberto = 300.0 + random.nextDouble() * 500.0;
        
        city.addPecas(pecasDescobertas);
        city.addDinheiro(dinheiroDescoberto);
        
        // Os robôs ficam felizes com a descoberta
        for (Robo robo : city.getRobos()){
            robo.descobertaPecasRaras();
        }
        
        turno.registrarEvento(String.format("💎 DESCOBERTA DE PEÇAS RARAS! +%d peças e +%.2f de dinheiro encontrados! Todos os robôs ficaram mais felizes (+15 felicidade).", 
                pecasDescobertas, dinheiroDescoberto));
    }
}
