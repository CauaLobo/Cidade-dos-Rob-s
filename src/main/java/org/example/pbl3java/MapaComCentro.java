package org.example.pbl3java;
import controller.PredioController;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.City;
import model.TipoPredio;
import model.Predio;

import java.awt.*;

public class MapaComCentro extends Application {

    // 1. Definições de Tamanho (Devem ser consistentes com a classe Cidade)
    private static final int GRID_SIZE = 30;
    private static final double CELL_SIZE = 40.0;
    private PredioController predioController = new PredioController();

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

        // 4. Configura a View com Scroll
        ScrollPane scrollPane = new ScrollPane(mapaCidade);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 800, 600);
        primaryStage.setTitle("Cidade dos Robôs - " + cidadeAtual.getNome());
        primaryStage.setScene(scene);
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

            // Tenta construir o Prédio Comercial no local clicado
            boolean sucesso = predioController.construirPredio(
                    TipoPredio.COMERCIAL,
                    cidadeAtual,
                    coluna,
                    linha
            );

            if (sucesso) {
                System.out.println("Prédio Comercial construído em: (" + coluna + ", " + linha + ")");
                redesenharMapa();
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
    }
    public static void main(String[] args) {
        // NOTE: Certifique-se de que todas as classes do Model estão no classpath.
        launch(args);
    }
}
