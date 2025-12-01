package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um turno do jogo.
 * 
 * <p>Armazena informações sobre um turno específico, incluindo seu número,
 * eventos que ocorreram durante o turno e decisões do jogador.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class Turno {
    private int nTurno;
    private List<String> eventosOcorridos;
    private String decisaoJogador;

    /**
     * Construtor para criar um novo turno.
     * 
     * @param numero O número do turno
     */
    public Turno(int numero) {
        this.nTurno = numero;
        this.eventosOcorridos = new ArrayList<>();
        this.decisaoJogador = "Nenhuma"; // Valor inicial
    }

    /**
     * Registra um evento que ocorreu durante este turno.
     * 
     * @param descricaoEvento Descrição do evento ocorrido
     */
    public void registrarEvento(String descricaoEvento) {
        this.eventosOcorridos.add(descricaoEvento);
    }



    public int getnTurno() {
        return nTurno;
    }

    public void setnTurno(int nTurno) {
        this.nTurno = nTurno;
    }

    public List<String> getEventosOcorridos() {
        return eventosOcorridos;
    }

    public void setEventosOcorridos(List<String> eventosOcorridos) {
        this.eventosOcorridos = eventosOcorridos;
    }

    public String getDecisaoJogador() {
        return decisaoJogador;
    }

    public void setDecisaoJogador(String decisaoJogador) {
        this.decisaoJogador = decisaoJogador;
    }
}
