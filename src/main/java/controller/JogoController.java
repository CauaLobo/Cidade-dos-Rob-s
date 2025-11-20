package controller;

import model.City;
import model.Predio;
import model.Robo;
import model.Turno;
import java.io.IOException;

public class JogoController {
    private City cidadeAtual;
    private PredioController predioController;
    private RoboController roboController;
    private EventController eventController;

    public JogoController(City cidade){
        this.cidadeAtual = cidade;
        this.predioController = new PredioController();
        this.eventController = new EventController();
        this.roboController= new RoboController();
    }

    public void salvarCity() throws IOException {
        PersistenceController.salvarCidade(cidadeAtual, cidadeAtual.getNome());
    }

    public City carregarCidade(String nomeCidade) throws IOException {
        try {
            City cidadeCarregada = PersistenceController.carregarCidade(nomeCidade);

            this.cidadeAtual = cidadeCarregada;

            return cidadeCarregada;

        } catch (IOException e) {
            // Trata a exceção e pode enviar uma mensagem de erro para a View
            throw new IOException("Erro ao carregar a cidade: " + nomeCidade, e);
        }
    }

    public void proximoTurno(){
        cidadeAtual.incrementaTurno();
        Turno novoTurno = new Turno(cidadeAtual.getTurnoAtual());

        for (Predio predio: cidadeAtual.getPredios()){
            predio.efeito(cidadeAtual);
        }

        for (Robo robo : cidadeAtual.getRobos()){
            robo.consumoDiario();
        }

        eventController.verificarEventos(cidadeAtual, novoTurno);

        cidadeAtual.felicidadeMedia();
    }

}
