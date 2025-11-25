package org.example.pbl3java;

import controller.PersistenceController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.City;

import java.io.IOException;
import java.util.List;

/**
 * Classe responsável pela interface do menu inicial do jogo.
 * Permite criar nova cidade, carregar cidade existente, deletar cidade e sair do jogo.
 */
public class MenuInicial extends Application {
    
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        criarMenu();
    }
    
    private void criarMenu() {
        // Container principal
        BorderPane root = new BorderPane();
        
        // Carrega o background
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/background_menu.png"));
            BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            // Se não conseguir carregar, usa cor sólida
            root.setStyle("-fx-background-color: #2c3e50;");
            System.err.println("Erro ao carregar background: " + e.getMessage());
        }
        
        // Container central com título e botões
        VBox containerCentral = new VBox(30);
        containerCentral.setAlignment(Pos.CENTER);
        containerCentral.setPadding(new Insets(50));
        containerCentral.setMinHeight(400); // Garante altura mínima
        
        // Título/Logo
        try {
            Image tituloImg = new Image(getClass().getResourceAsStream("/titulo.png"));
            ImageView tituloView = new ImageView(tituloImg);
            tituloView.setPreserveRatio(true);
            tituloView.setFitWidth(400);
            containerCentral.getChildren().add(tituloView);
        } catch (Exception e) {
            // Se não conseguir carregar, usa texto
            Label tituloTexto = new Label("CIDADE DOS ROBÔS");
            tituloTexto.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: white;");
            containerCentral.getChildren().add(tituloTexto);
            System.err.println("Erro ao carregar título: " + e.getMessage());
        }
        
        // Container de botões
        VBox botoesContainer = new VBox(15);
        botoesContainer.setAlignment(Pos.CENTER);
        botoesContainer.setPadding(new Insets(20));
        
        // Botão Nova Cidade
        Button btnNovaCidade = criarBotaoComImagem("/botao_nova_cidade.png", "Nova Cidade");
        btnNovaCidade.setOnAction(e -> criarNovaCidade());
        botoesContainer.getChildren().add(btnNovaCidade);
        
        // Botão Carregar Cidade
        Button btnCarregarCidade = criarBotaoComImagem("/botao_carregar_cidade.png", "Carregar Cidade");
        btnCarregarCidade.setOnAction(e -> carregarCidade());
        botoesContainer.getChildren().add(btnCarregarCidade);
        
        // Botão Deletar Cidade
        Button btnDeletarCidade = criarBotaoComImagem("/botao_deletar_cidade.png", "Deletar Cidade");
        btnDeletarCidade.setOnAction(e -> deletarCidade());
        botoesContainer.getChildren().add(btnDeletarCidade);
        
        // Botão Sair
        Button btnSair = criarBotaoComImagem("/botao_sair.png", "Sair");
        btnSair.setOnAction(e -> sair());
        botoesContainer.getChildren().add(btnSair);
        
        containerCentral.getChildren().add(botoesContainer);
        root.setCenter(containerCentral);
        
        // Cria a cena
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Cidade dos Robôs - Menu Principal");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true); // Permite redimensionar
        primaryStage.setMinWidth(800); // Largura mínima
        primaryStage.setMinHeight(600); // Altura mínima
        primaryStage.show();
    }
    
    /**
     * Cria um botão usando uma imagem PNG.
     * Se a imagem não for encontrada, cria um botão com texto.
     */
    private Button criarBotaoComImagem(String caminhoImagem, String textoAlternativo) {
        Button botao = new Button();
        final boolean[] imagemCarregada = {false};
        
        try {
            Image imgBotao = new Image(getClass().getResourceAsStream(caminhoImagem));
            if (imgBotao != null && !imgBotao.isError()) {
                ImageView imgView = new ImageView(imgBotao);
                imgView.setPreserveRatio(true);
                imgView.setFitWidth(250);
                botao.setGraphic(imgView);
                imagemCarregada[0] = true;
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem do botão " + caminhoImagem + ": " + e.getMessage());
        }
        
        // Se não conseguiu carregar a imagem, usa texto
        if (!imagemCarregada[0]) {
            botao.setText(textoAlternativo);
            botao.setStyle("-fx-font-size: 18px; -fx-padding: 10px 30px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5px;");
            botao.setPrefWidth(250);
            botao.setPrefHeight(50);
        } else {
            // Define tamanho mínimo mesmo com imagem
            botao.setMinWidth(250);
            botao.setMinHeight(50);
        }
        
        // Efeito hover
        botao.setOnMouseEntered(e -> {
            if (imagemCarregada[0]) {
                botao.setOpacity(0.8);
            } else {
                botao.setStyle("-fx-font-size: 18px; -fx-padding: 10px 30px; -fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 5px;");
            }
        });
        botao.setOnMouseExited(e -> {
            if (imagemCarregada[0]) {
                botao.setOpacity(1.0);
            } else {
                botao.setStyle("-fx-font-size: 18px; -fx-padding: 10px 30px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5px;");
            }
        });
        
        return botao;
    }
    
    /**
     * Abre diálogo para criar uma nova cidade.
     */
    private void criarNovaCidade() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nova Cidade");
        dialog.setHeaderText("Criar Nova Cidade");
        dialog.setContentText("Digite o nome da cidade:");
        
        dialog.showAndWait().ifPresent(nomeCidade -> {
            if (nomeCidade != null && !nomeCidade.trim().isEmpty()) {
                try {
                    City novaCidade = new City(nomeCidade.trim());
                    abrirJogo(novaCidade);
                } catch (Exception e) {
                    mostrarErro("Erro ao criar cidade", "Não foi possível criar a nova cidade: " + e.getMessage());
                }
            } else {
                mostrarErro("Nome inválido", "Por favor, digite um nome válido para a cidade.");
            }
        });
    }
    
    /**
     * Abre diálogo para carregar uma cidade existente.
     */
    private void carregarCidade() {
        List<String> cidadesSalvas = PersistenceController.listarCidadesSalvas();
        
        if (cidadesSalvas.isEmpty()) {
            mostrarAviso("Nenhuma cidade salva", "Não há cidades salvas para carregar.");
            return;
        }
        
        ChoiceDialog<String> dialog = new ChoiceDialog<>(cidadesSalvas.get(0), cidadesSalvas);
        dialog.setTitle("Carregar Cidade");
        dialog.setHeaderText("Selecione a cidade para carregar:");
        dialog.setContentText("Cidade:");
        
        dialog.showAndWait().ifPresent(nomeCidade -> {
            if (nomeCidade != null && !nomeCidade.trim().isEmpty()) {
                try {
                    City cidadeCarregada = PersistenceController.carregarCidade(nomeCidade.trim());
                    abrirJogo(cidadeCarregada);
                } catch (IOException e) {
                    mostrarErro("Erro ao carregar", "Não foi possível carregar a cidade: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * Abre diálogo para deletar uma cidade.
     */
    private void deletarCidade() {
        List<String> cidadesSalvas = PersistenceController.listarCidadesSalvas();
        
        if (cidadesSalvas.isEmpty()) {
            mostrarAviso("Nenhuma cidade salva", "Não há cidades salvas para deletar.");
            return;
        }
        
        ChoiceDialog<String> dialog = new ChoiceDialog<>(cidadesSalvas.get(0), cidadesSalvas);
        dialog.setTitle("Deletar Cidade");
        dialog.setHeaderText("Selecione a cidade para deletar:");
        dialog.setContentText("Cidade:");
        
        dialog.showAndWait().ifPresent(nomeCidade -> {
            if (nomeCidade != null && !nomeCidade.trim().isEmpty()) {
                // Confirmação
                Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacao.setTitle("Confirmar Exclusão");
                confirmacao.setHeaderText("Deletar Cidade");
                confirmacao.setContentText("Tem certeza que deseja deletar a cidade \"" + nomeCidade + "\"?\nEsta ação não pode ser desfeita.");
                
                confirmacao.showAndWait().ifPresent(resultado -> {
                    if (resultado == ButtonType.OK) {
                        if (PersistenceController.deletarCidade(nomeCidade.trim())) {
                            mostrarSucesso("Cidade deletada", "A cidade \"" + nomeCidade + "\" foi deletada com sucesso.");
                        } else {
                            mostrarErro("Erro ao deletar", "Não foi possível deletar a cidade.");
                        }
                    }
                });
            }
        });
    }
    
    /**
     * Abre a tela do jogo com a cidade fornecida.
     */
    private void abrirJogo(City cidade) {
        try {
            // Cria a tela do jogo
            MapaComCentro jogo = new MapaComCentro();
            jogo.setCidade(cidade);
            
            // Cria nova janela para o jogo
            Stage jogoStage = new Stage();
            jogo.start(jogoStage);
            
            // Fecha o menu
            primaryStage.close();
        } catch (Exception e) {
            mostrarErro("Erro ao iniciar jogo", "Não foi possível iniciar o jogo: " + e.getMessage());
        }
    }
    
    /**
     * Fecha o aplicativo.
     */
    private void sair() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Sair");
        confirmacao.setHeaderText("Sair do Jogo");
        confirmacao.setContentText("Tem certeza que deseja sair?");
        
        confirmacao.showAndWait().ifPresent(resultado -> {
            if (resultado == ButtonType.OK) {
                primaryStage.close();
            }
        });
    }
    
    /**
     * Mostra uma mensagem de erro.
     */
    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    /**
     * Mostra uma mensagem de aviso.
     */
    private void mostrarAviso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    /**
     * Mostra uma mensagem de sucesso.
     */
    private void mostrarSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

