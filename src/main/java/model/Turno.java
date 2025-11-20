package model;

import java.util.ArrayList;
import java.util.List;

public class Turno {
    private int nTurno;
    private List<String> eventosOcorridos;
    private String decisaoJogador;

    public Turno(int numero) {
        this.nTurno = numero;
        this.eventosOcorridos = new ArrayList<>();
        this.decisaoJogador = "Nenhuma"; // Valor inicial
    }

    // --- Métodos de Gestão ---

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
