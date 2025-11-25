package controller;

import model.City;
import model.Robo;
import model.TipoDeRobo;
import model.Turno;

import java.util.Random;

public class EventController {
    private Random random = new Random();
    private final int ChanceDeEvento = 20;
    private RoboController roboController = new RoboController();

    public void verificarEventos(City cidade, Turno turnoAtual) {

        // Determina se um evento aleatório deve ocorrer
        if (random.nextInt(100) < ChanceDeEvento) {

            // Escolhe aleatoriamente um dos três tipos de evento
            int tipoEvento = random.nextInt(3);

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
        
        String mensagem = "Apagão de Energia! Robos foram gravemente afetados";
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

        for (Robo robo : city.getRobos()){
            robo.greve();
            
            // Aplica redução de impacto se houver seguranças
            if (qtdSegurancas > 0) {
                // Recupera parte da felicidade perdida
                double felicidadeRecuperada = 30.0 * reducaoImpacto;
                robo.setFelicidade(Math.min(100.0, robo.getFelicidade() + felicidadeRecuperada));
            }
        }
        
        String mensagem = "Greve dos Robôs! Os robôs estão insatisfeitos e pararam de trabalhar. Produção reduzida neste turno.";
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
        
        turno.registrarEvento(String.format("Descoberta de Peças Raras! +%d peças e +%.2f de dinheiro encontrados! Os robôs estão mais felizes.", 
                pecasDescobertas, dinheiroDescoberto));
    }
}
