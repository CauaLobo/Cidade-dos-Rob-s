package controller;

import model.City;
import model.Predio;
import model.Robo;
import model.TipoPredio;
import model.Turno;
import java.io.IOException;

/**
 * Controller principal do jogo que gerencia o fluxo geral e coordena outros controllers.
 * 
 * <p>Responsável por:
 * <ul>
 *   <li>Gerenciar o estado atual da cidade</li>
 *   <li>Processar turnos do jogo</li>
 *   <li>Salvar e carregar cidades</li>
 *   <li>Coordenar ações entre PredioController, RoboController e EventController</li>
 * </ul>
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class JogoController {
    private City cidadeAtual;
    private PredioController predioController;
    private RoboController roboController;
    private EventController eventController;
    private Turno ultimoTurno;

    /**
     * Construtor do JogoController.
     * 
     * @param cidade A cidade inicial do jogo
     */
    public JogoController(City cidade){
        this.cidadeAtual = cidade;
        this.predioController = new PredioController();
        this.eventController = new EventController();
        this.roboController= new RoboController();
    }

    /**
     * Salva o estado atual da cidade em um arquivo JSON.
     * 
     * <p>Antes de salvar, atualiza a felicidade média e as posições dos robôs
     * para garantir que todos os dados estejam sincronizados.
     * 
     * @throws IOException Se houver erro ao salvar o arquivo
     */
    public void salvarCity() throws IOException {
        // Atualiza a felicidade média antes de salvar para garantir que está sincronizada
        cidadeAtual.felicidadeMedia();
        
        // Garante que as posições dos robôs estejam atualizadas
        // Se um robô está em um prédio, sua posição deve refletir isso
        atualizarPosicoesRobos();
        
        // Salva a cidade com todos os seus dados
        PersistenceController.salvarCidade(cidadeAtual, cidadeAtual.getNome());
    }
    
    /**
     * Atualiza as posições dos robôs baseado em onde eles estão (prédio ou mapa).
     */
    private void atualizarPosicoesRobos() {
        for (Robo robo : cidadeAtual.getRobos()) {
            Predio predioDoRobo = roboController.encontrarPredioDoRobo(robo, cidadeAtual);
            if (predioDoRobo != null) {
                // Robô está em um prédio - atualiza posição para a posição do prédio
                robo.setPosX(predioDoRobo.getPosX());
                robo.setPosY(predioDoRobo.getPosY());
            }
            // Se não está em prédio, mantém a posição atual do robô
        }
    }

    /**
     * Carrega uma cidade salva a partir de um arquivo JSON.
     * 
     * @param nomeCidade Nome da cidade a ser carregada
     * @return A cidade carregada
     * @throws IOException Se houver erro ao carregar o arquivo ou se a cidade não for encontrada
     */
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

    /**
     * Processa o próximo turno do jogo.
     * 
     * <p>Executa as seguintes ações em ordem:
     * <ol>
     *   <li>Incrementa o número do turno</li>
     *   <li>Aplica os efeitos de todos os prédios (trabalho, descanso, treinamento)</li>
     *   <li>Aplica consumo diário apenas para robôs que não estão em prédios residenciais</li>
     *   <li>Verifica e aplica eventos aleatórios</li>
     *   <li>Calcula a felicidade média</li>
     *   <li>Aplica bônus de felicidade dos robôs de segurança</li>
     * </ol>
     */
    public void proximoTurno(){
        cidadeAtual.incrementaTurno();
        Turno novoTurno = new Turno(cidadeAtual.getTurnoAtual());

        // Primeiro aplica os efeitos dos prédios (robôs em residenciais descansam, em comerciais trabalham)
        for (Predio predio: cidadeAtual.getPredios()){
            predio.efeito(cidadeAtual);
        }

        // Depois aplica consumo diário apenas para robôs que NÃO estão em prédios residenciais
        // (robôs em residenciais já recuperaram energia no efeito do prédio)
        for (Robo robo : cidadeAtual.getRobos()){
            // Verifica se o robô está em um prédio residencial
            Predio predioDoRobo = roboController.encontrarPredioDoRobo(robo, cidadeAtual);
            
            // Se não estiver em prédio residencial, aplica consumo diário
            // (robôs em comerciais já trabalharam, mas ainda precisam consumir energia de manutenção básica)
            if (predioDoRobo == null || predioDoRobo.getTipo() != TipoPredio.RESIDENCIAL) {
                robo.consumoDiario();
            }
        }

        eventController.verificarEventos(cidadeAtual, novoTurno);

        cidadeAtual.felicidadeMedia();
        
        // Aplica bônus de felicidade dos seguranças
        roboController.aplicarBonusFelicidadeSeguranca(cidadeAtual);
        
        // Armazena o último turno para acesso aos eventos
        this.ultimoTurno = novoTurno;
    }
    
    /**
     * Retorna o último turno processado.
     * @return O último turno ou null se nenhum turno foi processado ainda
     */
    public Turno getUltimoTurno() {
        return ultimoTurno;
    }
    
    /**
     * Retorna a cidade atual.
     * @return A cidade atual
     */
    public City getCidadeAtual() {
        return cidadeAtual;
    }

}
