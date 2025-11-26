package org.example.pbl3java;
import controller.JogoController;
import controller.PredioController;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.City;
import model.TipoPredio;
import model.Predio;
import model.Robo;
import model.predioComercial;
import model.predioResidencial;
import controller.RoboController;

public class MapaComCentro extends Application {

    // 1. Definições de Tamanho (Devem ser consistentes com a classe Cidade)
    private static final int GRID_SIZE = 30;
    private static final double CELL_SIZE = 40.0;
    private PredioController predioController = new PredioController();
    private RoboController roboController = new RoboController();
    private JogoController jogoController;
    private PainelInformacoes painelInformacoes;

    private City cidadeAtual; // Simula o Model que está sendo exibido
    private GridPane mapaCidade;

    /**
     * Define a cidade a ser exibida no mapa.
     * @param cidade A cidade a ser exibida
     */
    public void setCidade(City cidade) {
        this.cidadeAtual = cidade;
    }

    @Override
    public void start(Stage primaryStage) {

        // Se não houver cidade definida, cria uma nova
        if (this.cidadeAtual == null) {
            this.cidadeAtual = new City("RoboCentral");
        }

        // Inicializa o JogoController
        this.jogoController = new JogoController(cidadeAtual);

        // Inicializa o GridPane e o ScrollPane
        mapaCidade = new GridPane();
        mapaCidade.setHgap(0);
        mapaCidade.setVgap(0);
        mapaCidade.setStyle("-fx-background-image: url('chao.png'); -fx-background-repeat: repeat;");

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {

                // CRIA e configura o visual da célula com o handler de clique
                Node cellVisual = criarCelulaMapa(i, j);

                // Adiciona o visual à grade na posição (i, j)
                mapaCidade.add(cellVisual, i, j);
            }
        }

        // 3. Desenha os Prédios a partir do Model
        desenharPredios(cidadeAtual);

        // 3.5. Desenha os Robôs no mapa
        desenharRobos(cidadeAtual);

        // 4. Configura a View com Scroll
        ScrollPane scrollPane = new ScrollPane(mapaCidade);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // 5. Cria o painel de informações
        painelInformacoes = new PainelInformacoes(cidadeAtual, jogoController);
        // Conecta o painel ao mapa para permitir atualizações
        painelInformacoes.setMapaComCentro(this);

        // 6. Layout principal com BorderPane (mapa à esquerda, painel à direita)
        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        root.setRight(painelInformacoes);

        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setTitle("Cidade dos Robôs - " + cidadeAtual.getNome());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    /**
     * Itera sobre o Model (Cidade) e desenha os Nodes (Prédios) na View.
     */
    private void desenharPredios(City cidade) {
        for (Predio predio : cidade.getPredios()) {

            // 1. Decisão de qual imagem usar com base no TIPO
            String imagePath;
            if (predio.getTipo() == TipoPredio.COMERCIAL) {
                imagePath = "/comercio.png";
            } else if (predio.getTipo() == TipoPredio.RESIDENCIAL) {
                imagePath = "/residencia.png";
            } else if (predio.getTipo() == TipoPredio.CENTRO) { // Sua Academia Robo
                imagePath = "/centro.png";
            } else {
                continue;
            }

            Image imagem = new Image(getClass().getResourceAsStream(imagePath));
            // 2. Cria o ImageView e o StackPane
            ImageView predioImageView = new ImageView(imagem);
            javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane(predioImageView);

            if(predio.getTipo() != TipoPredio.CENTRO){
            double tamanhoReduzido = (CELL_SIZE * 2) - 20;

            predioImageView.setFitWidth(tamanhoReduzido);
            predioImageView.setFitHeight(tamanhoReduzido);}

            // 3. Aplica o posicionamento e o Span (assumindo que largura e altura estão no Predio)
            GridPane.setColumnSpan(stack, 2);
            GridPane.setRowSpan(stack, 2);

            // 4. Adiciona à grade
            mapaCidade.add(stack, predio.getPosX(), predio.getPosY());
        }
    }

    /**
     * Itera sobre os robôs da cidade e desenha-os no mapa.
     * Robôs em prédios são desenhados próximos ao prédio.
     * Robôs livres são desenhados em suas posições.
     */
    private void desenharRobos(City cidade) {
        for (Robo robo : cidade.getRobos()) {
            // Determina qual imagem usar baseado no tipo do robô
            String imagePath;
            switch (robo.getTipo()) {
                case TRABALHADOR:
                    imagePath = "/trabalhador.png";
                    break;
                case ENGENHEIRO:
                    imagePath = "/engenheiro.png";
                    break;
                case SEGURANCA:
                    imagePath = "/seguranca.png";
                    break;
                default:
                    continue; // Tipo desconhecido, pula
            }

            // Tenta carregar a imagem
            Image imagemRobo = new Image(getClass().getResourceAsStream(imagePath));
            if (imagemRobo.isError()) {
                System.err.println("Erro ao carregar imagem do robô: " + imagePath);
                continue;
            }

            ImageView roboImageView = new ImageView(imagemRobo);
            
            // Tamanho do robô (maior e mais visível)
            double tamanhoRobo = CELL_SIZE * 1.2; // 120% do tamanho da célula (48px)
            roboImageView.setFitWidth(tamanhoRobo);
            roboImageView.setFitHeight(tamanhoRobo);
            roboImageView.setPreserveRatio(true);

            // Verifica se o robô está em um prédio
            Predio predioDoRobo = roboController.encontrarPredioDoRobo(robo, cidade);
            
            int posX, posY;
            
            if (predioDoRobo != null) {
                // Robô está em um prédio - posiciona ao redor do prédio de forma organizada
                // Calcula offset baseado na posição do robô na lista do prédio
                int indiceNoPredio = 0;
                if (predioDoRobo instanceof predioComercial) {
                    predioComercial comercial = (predioComercial) predioDoRobo;
                    indiceNoPredio = comercial.getRobos().indexOf(robo);
                } else if (predioDoRobo instanceof predioResidencial) {
                    predioResidencial residencial = (predioResidencial) predioDoRobo;
                    indiceNoPredio = residencial.getRobos().indexOf(robo);
                }
                
                // Posiciona os robôs ao redor do prédio de forma mais espaçada
                // Usa uma distribuição circular ao redor do prédio
                int larguraPredio = predioDoRobo.getLargura();
                int alturaPredio = predioDoRobo.getAltura();
                
                // Calcula posições ao redor do prédio (até 8 posições)
                int offsetX = 0, offsetY = 0;
                int totalRobos = Math.min(indiceNoPredio, 7); // Máximo 8 posições
                
                // Distribuição em círculo ao redor do prédio
                switch (totalRobos % 8) {
                    case 0: // Esquerda do prédio (meio)
                        offsetX = -1;
                        offsetY = alturaPredio / 2;
                        break;
                    case 1: // Direita do prédio (meio)
                        offsetX = larguraPredio;
                        offsetY = alturaPredio / 2;
                        break;
                    case 2: // Acima do prédio (meio)
                        offsetX = larguraPredio / 2;
                        offsetY = -1;
                        break;
                    case 3: // Abaixo do prédio (meio)
                        offsetX = larguraPredio / 2;
                        offsetY = alturaPredio;
                        break;
                    case 4: // Canto superior esquerdo
                        offsetX = -1;
                        offsetY = -1;
                        break;
                    case 5: // Canto superior direito
                        offsetX = larguraPredio;
                        offsetY = -1;
                        break;
                    case 6: // Canto inferior esquerdo
                        offsetX = -1;
                        offsetY = alturaPredio;
                        break;
                    case 7: // Canto inferior direito
                        offsetX = larguraPredio;
                        offsetY = alturaPredio;
                        break;
                }
                
                posX = predioDoRobo.getPosX() + offsetX;
                posY = predioDoRobo.getPosY() + offsetY;
            } else {
                // Robô está livre no mapa - usa sua posição atual
                posX = robo.getPosX();
                posY = robo.getPosY();
            }

            // Valida se a posição está dentro dos limites do mapa
            if (posX < 0 || posX >= GRID_SIZE || posY < 0 || posY >= GRID_SIZE) {
                continue; // Posição inválida, pula este robô
            }

            // Cria um StackPane para o robô
            javafx.scene.layout.StackPane roboStack = new javafx.scene.layout.StackPane(roboImageView);
            
            // Adiciona tooltip com informações do robô
            String tooltipText = String.format(
                "Robô %s\nEnergia: %.1f%%\nFelicidade: %.1f%%\nIntegridade: %.1f%%",
                robo.getTipo().toString(),
                robo.getEnergia(),
                robo.getFelicidade(),
                robo.getIntegridade()
            );
            if (robo.isEmManutencao()) {
                tooltipText += String.format("\n🔧 Em Manutenção (%d turno(s) restante(s))", robo.getTurnosRestantesManutencao());
            }
            javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(tooltipText);
            javafx.scene.control.Tooltip.install(roboStack, tooltip);

            // Adiciona o robô ao mapa
            mapaCidade.add(roboStack, posX, posY);
        }
    }

    private Node criarCelulaMapa(int x, int y) { // Tipo de retorno correto (Node/ImageView)

        // Opcional: Se você quiser que o clique seja na imagem de fundo e não no retângulo
        Image terreno = new Image("chao.png");
        ImageView cellVisual = new ImageView(terreno);
        cellVisual.setFitWidth(CELL_SIZE);
        cellVisual.setFitHeight(CELL_SIZE);

        // 2. Adiciona o Manipulador de Clique
        cellVisual.setOnMouseClicked(event -> {

            // As variáveis x e y já são as coordenadas corretas
            int coluna = x;
            int linha = y;

            // Tenta construir o Prédio no local clicado (tipo selecionado no painel)
            TipoPredio tipoSelecionado = painelInformacoes != null ? 
                painelInformacoes.getTipoPredioSelecionado() : TipoPredio.COMERCIAL;
            
            boolean sucesso = predioController.construirPredio(
                    tipoSelecionado,
                    cidadeAtual,
                    coluna,
                    linha
            );

            if (sucesso) {
                System.out.println("Prédio Comercial construído em: (" + coluna + ", " + linha + ")");
                redesenharMapa();
                // Atualiza o painel de informações após construir
                if (painelInformacoes != null) {
                    painelInformacoes.atualizarInformacoes();
                }
            } else {
                System.out.println("Construção falhou! Recursos insuficientes ou posição inválida.");
            }
        });

        return cellVisual;
    }

    private void redesenharMapa() {
        // 1. Limpa todos os elementos gráficos que não são o ScrollPane
        mapaCidade.getChildren().clear();

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                // Recria a célula com o manipulador de clique!
                mapaCidade.add(criarCelulaMapa(i, j), i, j);
            }
        }

        // 3. Desenha os prédios ATUAIS do Model por cima do terreno
        desenharPredios(cidadeAtual);

        // 4. Desenha os robôs ATUAIS do Model
        desenharRobos(cidadeAtual);
    }

    /**
     * Método público para atualizar o mapa após mudanças nos robôs ou prédios.
     * Pode ser chamado externamente quando necessário.
     */
    public void atualizarMapa() {
        if (mapaCidade != null && cidadeAtual != null) {
            redesenharMapa();
        }
    }

    public static void main(String[] args) {
        // NOTE: Certifique-se de que todas as classes do Model estão no classpath.
        launch(args);
    }
}
