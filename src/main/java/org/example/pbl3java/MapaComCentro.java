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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
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
    private Pane layerPredios; // Camada para prédios e robôs (permite sobreposição)

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
        // Define tamanho baseado no número de células e tamanho fixo da célula
        double tamanhoMapa = GRID_SIZE * CELL_SIZE;
        mapaCidade.setPrefSize(tamanhoMapa, tamanhoMapa);
        mapaCidade.setMinSize(tamanhoMapa, tamanhoMapa);
        mapaCidade.setMaxSize(tamanhoMapa, tamanhoMapa);
        mapaCidade.setStyle("-fx-background-color: transparent;"); // Transparente, o fundo vem do ScrollPane

        // Configura colunas e linhas com tamanho fixo
        for (int i = 0; i < GRID_SIZE; i++) {
            ColumnConstraints col = new ColumnConstraints(CELL_SIZE);
            col.setMinWidth(CELL_SIZE);
            col.setMaxWidth(CELL_SIZE);
            col.setHgrow(Priority.NEVER);
            mapaCidade.getColumnConstraints().add(col);
            
            RowConstraints row = new RowConstraints(CELL_SIZE);
            row.setMinHeight(CELL_SIZE);
            row.setMaxHeight(CELL_SIZE);
            row.setVgrow(Priority.NEVER);
            mapaCidade.getRowConstraints().add(row);
        }

        // Adiciona todas as células do chão
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                // CRIA e configura o visual da célula com o handler de clique
                Node cellVisual = criarCelulaMapa(i, j);
                // Adiciona o visual à grade na posição (i, j)
                mapaCidade.add(cellVisual, i, j);
            }
        }

        // 3. Cria uma camada Pane para prédios e robôs (permite sobreposição)
        layerPredios = new Pane();
        layerPredios.setPrefSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        layerPredios.setMinSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        layerPredios.setMaxSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        layerPredios.setStyle("-fx-background-color: transparent;"); // Transparente, não cinza
        layerPredios.setMouseTransparent(true); // Permite cliques passarem para o GridPane (importante!)
        
        // 4. Cria um StackPane para combinar o GridPane (chão) com o Pane (prédios/robôs)
        javafx.scene.layout.StackPane container = new javafx.scene.layout.StackPane();
        container.getChildren().addAll(mapaCidade, layerPredios);
        container.setStyle("-fx-background-color: transparent;"); // Remove qualquer fundo cinza
        // Garante que o container tenha o tamanho correto do mapa
        container.setPrefSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        container.setMinSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        container.setMaxSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);

        // 5. Desenha os Prédios na camada de prédios
        desenharPredios(cidadeAtual);

        // 6. Desenha os Robôs na camada de prédios
        desenharRobos(cidadeAtual);

        // 7. Configura a View com Scroll
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: #1a252f;"); // Fundo escuro para áreas fora do mapa

        // 5. Cria o painel de informações
        painelInformacoes = new PainelInformacoes(cidadeAtual, jogoController);
        // Conecta o painel ao mapa para permitir atualizações
        painelInformacoes.setMapaComCentro(this);
        // Passa a referência do Stage para permitir voltar ao menu
        painelInformacoes.setStagePrincipal(primaryStage);

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
     * 
     * <p>Desenha todos os prédios da cidade no mapa, exceto o CENTRO que não é visível.
     * Posiciona os prédios baseado em suas coordenadas (posX, posY) e dimensões (largura, altura).
     * 
     * @param cidade A cidade cujos prédios serão desenhados
     */
    private void desenharPredios(City cidade) {
        System.out.println("=== Desenhando prédios. Total: " + cidade.getPredios().size() + " ===");
        for (Predio predio : cidade.getPredios()) {

            // Pula o CENTRO - não desenha no mapa (mas mantém funcionalidade para treinamento)
            if (predio.getTipo() == TipoPredio.CENTRO) {
                System.out.println("Pulando CENTRO");
                continue;
            }
            
            System.out.println("Processando prédio: " + predio.getTipo() + " em (" + predio.getPosX() + ", " + predio.getPosY() + ")");

            // Valida se a posição está dentro dos limites do mapa
            int posX = predio.getPosX();
            int posY = predio.getPosY();
            int largura = predio.getLargura();
            int altura = predio.getAltura();
            
            if (posX < 0 || posY < 0 || posX + largura > GRID_SIZE || posY + altura > GRID_SIZE) {
                System.err.println("Prédio fora dos limites do mapa: " + predio.getTipo() + " em (" + posX + ", " + posY + ")");
                continue; // Pula prédios fora dos limites
            }

            // 1. Decisão de qual imagem usar com base no TIPO
            String imagePath;
            if (predio.getTipo() == TipoPredio.COMERCIAL) {
                imagePath = "/comercio 1.png";
            } else if (predio.getTipo() == TipoPredio.RESIDENCIAL) {
                imagePath = "/residencia 1.png";
            } else if (predio.getTipo() == TipoPredio.MONUMENTO) {
                imagePath = "/monumento.png";
            } else if (predio.getTipo() == TipoPredio.TORRE_COMUNICACAO) {
                imagePath = "/torre_comunicacao.png";
            } else if (predio.getTipo() == TipoPredio.ESTACAO_ENERGIA) {
                imagePath = "/estacao_energia.png";
            } else if (predio.getTipo() == TipoPredio.JARDIM_ZEN) {
                imagePath = "/jardim_zen.png";
            } else if (predio.getTipo() == TipoPredio.OBSERVATORIO) {
                imagePath = "/centro.png"; // Usa a imagem do centro
            } else {
                continue; // Tipo não suportado
            }

            // Tenta carregar a imagem
            Image imagem = null;
            try {
                // Tenta carregar a imagem principal
                java.io.InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream != null) {
                    imagem = new Image(imageStream);
                    imageStream.close(); // Fecha o stream
                    
                    if (!imagem.isError() && imagem.getWidth() > 0) {
                        System.out.println("✓ Imagem carregada: " + imagePath + " (" + (int)imagem.getWidth() + "x" + (int)imagem.getHeight() + ")");
                    } else {
                        System.err.println("✗ Erro na imagem: " + imagePath);
                        imagem = null; // Força tentar placeholder
                    }
                } else {
                    System.err.println("✗ Arquivo não encontrado: " + imagePath);
                }
                
                // Se falhou, tenta usar placeholder
                if (imagem == null || imagem.isError()) {
                    imageStream = getClass().getResourceAsStream("/centro.png");
                    if (imageStream != null) {
                        imagem = new Image(imageStream);
                        imageStream.close();
                        if (imagem.isError()) {
                            System.err.println("✗ Erro ao carregar placeholder também!");
                            continue;
                        }
                        System.out.println("⚠ Usando placeholder para: " + imagePath);
                    } else {
                        System.err.println("✗ Placeholder também não encontrado!");
                        continue;
                    }
                }
            } catch (Exception e) {
                System.err.println("✗ Exceção ao carregar imagem: " + imagePath + " - " + e.getMessage());
                e.printStackTrace();
                continue;
            }
            
            // 2. Cria o ImageView e o StackPane
            ImageView predioImageView = new ImageView(imagem);
            predioImageView.setVisible(true);
            predioImageView.setOpacity(1.0);
            
            javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane(predioImageView);
            
            // Remove fundo branco do StackPane
            stack.setBackground(null);
            stack.setStyle("-fx-background-color: transparent;");
            stack.setVisible(true);
            stack.setOpacity(1.0);

            // Define tamanho baseado nas dimensões do prédio
            // Prédios decorativos são maiores para melhor visualização
            double multiplicador = 1.0;
            if (predio.getTipo() == TipoPredio.MONUMENTO || 
                predio.getTipo() == TipoPredio.TORRE_COMUNICACAO ||
                predio.getTipo() == TipoPredio.ESTACAO_ENERGIA ||
                predio.getTipo() == TipoPredio.JARDIM_ZEN ||
                predio.getTipo() == TipoPredio.OBSERVATORIO) {
                multiplicador = 1.8; // 80% maior para prédios decorativos
            }
            
            double larguraPixel = largura * CELL_SIZE * multiplicador;
            double alturaPixel = altura * CELL_SIZE * multiplicador;

            predioImageView.setFitWidth(larguraPixel);
            predioImageView.setFitHeight(alturaPixel);
            predioImageView.setPreserveRatio(true); // Mantém proporção para prédios
            predioImageView.setSmooth(true);
            
            // Garante que o ImageView seja visível
            predioImageView.setVisible(true);
            predioImageView.setOpacity(1.0);

            // 3. Posiciona usando coordenadas absolutas no Pane
            // Ajusta a posição para centralizar prédios maiores
            double offsetX = (larguraPixel - (largura * CELL_SIZE)) / 2;
            double offsetY = (alturaPixel - (altura * CELL_SIZE)) / 2;
            double x = (posX * CELL_SIZE) - offsetX;
            double y = (posY * CELL_SIZE) - offsetY;
            
            stack.setLayoutX(x);
            stack.setLayoutY(y);
            stack.setPrefSize(larguraPixel, alturaPixel);

            // 4. Adiciona à camada de prédios
            if (layerPredios != null) {
                layerPredios.getChildren().add(stack);
                System.out.println("✓ Prédio " + predio.getTipo() + " adicionado ao layerPredios em (" + x + ", " + y + ")");
            } else {
                System.err.println("✗ ERRO: layerPredios é null!");
            }
        }
    }

    /**
     * Itera sobre os robôs da cidade e desenha-os no mapa.
     * Robôs em prédios são desenhados próximos ao prédio.
     * Robôs livres são desenhados em suas posições.
     */
    /**
     * Desenha todos os robôs da cidade no mapa.
     * 
     * <p>Posiciona os robôs ao redor dos prédios onde estão alocados,
     * distribuindo-os em posições circulares para evitar sobreposição.
     * Robôs livres no mapa são posicionados em suas coordenadas atuais.
     * 
     * @param cidade A cidade cujos robôs serão desenhados
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
            
            // Tamanho do robô (aumentado para melhor visibilidade)
            double tamanhoRobo = CELL_SIZE * 1.8; // 72px (aumentado de 48px para 72px)
            roboImageView.setFitWidth(tamanhoRobo);
            roboImageView.setFitHeight(tamanhoRobo);
            roboImageView.setPreserveRatio(true);
            roboImageView.setSmooth(true);

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

            // Cria um StackPane para o robô (sem fundo branco)
            javafx.scene.layout.StackPane roboStack = new javafx.scene.layout.StackPane(roboImageView);
            roboStack.setBackground(null);
            roboStack.setStyle("-fx-background-color: transparent;");
            
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

            // Posiciona o robô usando coordenadas absolutas no Pane
            double x = posX * CELL_SIZE;
            double y = posY * CELL_SIZE;
            
            roboStack.setLayoutX(x);
            roboStack.setLayoutY(y);

            // Adiciona o robô à camada de prédios
            layerPredios.getChildren().add(roboStack);
        }
    }

    /**
     * Cria uma célula do mapa na posição especificada.
     * 
     * <p>Cada célula representa uma unidade do grid do mapa e contém:
     * <ul>
     *   <li>Imagem do chão (chao.png)</li>
     *   <li>Manipulador de clique para construção de prédios</li>
     * </ul>
     * 
     * @param x Coordenada X da célula no grid
     * @param y Coordenada Y da célula no grid
     * @return Um Node (StackPane) representando a célula do mapa
     */
    private Node criarCelulaMapa(int x, int y) {

        // Carrega a imagem do chão corretamente
        Image terreno = new Image(getClass().getResourceAsStream("/chao.png"));
        if (terreno.isError() || terreno.getWidth() <= 0) {
            System.err.println("Erro ao carregar imagem do chão: /chao.png");
            // Cria uma célula vazia se a imagem não carregar (transparente)
            javafx.scene.layout.StackPane emptyCell = new javafx.scene.layout.StackPane();
            emptyCell.setPrefSize(CELL_SIZE, CELL_SIZE);
            emptyCell.setMinSize(CELL_SIZE, CELL_SIZE);
            emptyCell.setMaxSize(CELL_SIZE, CELL_SIZE);
            emptyCell.setStyle("-fx-background-color: transparent;");
            return emptyCell;
        }
        
        // Cria um StackPane para a célula (garante que o fundo seja visível)
        javafx.scene.layout.StackPane cellPane = new javafx.scene.layout.StackPane();
        cellPane.setPrefSize(CELL_SIZE, CELL_SIZE);
        cellPane.setMinSize(CELL_SIZE, CELL_SIZE);
        cellPane.setMaxSize(CELL_SIZE, CELL_SIZE);
        // Garante que a célula preencha completamente o espaço
        cellPane.setStyle("-fx-background-color: transparent;");
        
        ImageView cellVisual = new ImageView(terreno);
        // Usa bind para garantir que a imagem sempre preencha a célula
        cellVisual.fitWidthProperty().bind(cellPane.widthProperty());
        cellVisual.fitHeightProperty().bind(cellPane.heightProperty());
        cellVisual.setPreserveRatio(false); // Desabilita para preencher completamente sem espaços
        cellVisual.setSmooth(true);
        
        cellPane.getChildren().add(cellVisual);

        // 2. Adiciona o Manipulador de Clique na célula inteira
        cellPane.setOnMouseClicked(event -> {

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
                System.out.println("✓ Prédio construído com sucesso em: (" + coluna + ", " + linha + ")");
                redesenharMapa();
                // Atualiza o painel de informações após construir
                if (painelInformacoes != null) {
                    painelInformacoes.atualizarInformacoes();
                }
            } else {
                System.out.println("❌ Construção falhou em (" + coluna + ", " + linha + "). Verifique recursos ou posição.");
            }
        });

        return cellPane;
    }

    /**
     * Redesenha o mapa, atualizando prédios e robôs.
     * 
     * <p>Limpa a camada de prédios e robôs e redesenha todos os elementos
     * baseado no estado atual do modelo (cidadeAtual).
     */
    private void redesenharMapa() {
        // 1. Limpa apenas a camada de prédios e robôs (mantém o chão)
        if (layerPredios != null) {
            layerPredios.getChildren().clear();
        }

        // 2. Redesenha os prédios ATUAIS do Model
        desenharPredios(cidadeAtual);

        // 3. Redesenha os robôs ATUAIS do Model
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
