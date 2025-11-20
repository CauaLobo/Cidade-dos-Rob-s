package controller;

import model.City;
import model.Robo;
import model.Turno;

import java.util.Random;

public class EventController {
    private Random random = new Random();
    private final int ChanceDeEvento = 20;

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
                    //aplicarGreve(cidade, turnoAtual);
                    break;
                case 2:
                   //aplicarBAVI(cidade, turnoAtual);
                    break;
            }
        }
    }

    private void aplicarApagao(City city, Turno turno){
        for (Robo robo : city.getRobos()){
            robo.apagao();
        }
        turno.registrarEvento("Apagão de Energia! Robos foram gravemente afetados");
    }
}
