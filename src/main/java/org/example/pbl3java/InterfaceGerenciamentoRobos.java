package org.example.pbl3java;

import controller.RoboController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.City;
import model.Predio;
import model.Robo;
import model.TipoDeRobo;
import model.TipoPredio;
import model.predioComercial;
import model.predioResidencial;
import model.PredioDecorativo;

/**
 * Interface dedicada para o gerenciamento de robôs.
 * Permite visualizar todos os robôs, fazer manutenção, mover para prédios e remover de prédios.
 */
public class InterfaceGerenciamentoRobos {
    
    private Stage stage;
    private City cidade;
    private RoboController roboController;
    private MapaComCentro mapaComCentro; // Referência opcional ao mapa para atualização
    
    // Componentes da interface
    private ListView<Robo> listaTodosRobos;
    private Label labelTipoRobo;
    private Label labelEnergia;
    private Label labelFelicidade;
    private Label labelIntegridade;
    private Label labelLocalizacao;
    private Label labelStatus;
    private Label labelManutencao;
    private Button btnManutencao;
    private ComboBox<Predio> comboBoxPredios;
    private Button btnMoverParaPredio;
    private Button btnRemoverDePredio;
    private Button btnDeletarRobo;
    
    public InterfaceGerenciamentoRobos(City cidade, RoboController roboController) {
        this.cidade = cidade;
        this.roboController = roboController;
        
        criarInterface();
    }
    
    private void criarInterface() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Gerenciamento de Robôs");
        stage.setResizable(false);
        
        // Container principal
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2c3e50;");
        
        // Título
        Label titulo = new Label("🤖 GERENCIAMENTO DE ROBÔS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.CYAN);
        titulo.setAlignment(Pos.CENTER);
        titulo.setMaxWidth(Double.MAX_VALUE);
        root.setTop(titulo);
        
        // Container central com lista e informações
        HBox containerCentral = new HBox(15);
        
        // Painel esquerdo - Lista de robôs
        VBox painelLista = criarPainelListaRobos();
        containerCentral.getChildren().add(painelLista);
        
        // Painel direito - Informações e ações
        VBox painelInfo = criarPainelInformacoes();
        containerCentral.getChildren().add(painelInfo);
        
        root.setCenter(containerCentral);
        
        // Botões de ação na parte inferior
        HBox botoesAcao = criarBotoesAcao();
        root.setBottom(botoesAcao);
        
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        
        // Atualiza informações iniciais
        atualizarListaRobos();
    }
    
    private VBox criarPainelListaRobos() {
        VBox painel = new VBox(10);
        painel.setPrefWidth(350);
        painel.setPadding(new Insets(10));
        painel.setStyle("-fx-background-color: #34495e; -fx-background-radius: 5px;");
        
        Label titulo = new Label("LISTA DE ROBÔS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titulo.setTextFill(Color.WHITE);
        painel.getChildren().add(titulo);
        
        // Lista de TODOS os robôs (disponíveis e alocados)
        listaTodosRobos = new ListView<>();
        listaTodosRobos.setPrefHeight(400);
        listaTodosRobos.setStyle("-fx-background-color: #1a252f; -fx-text-fill: white;");
        listaTodosRobos.setCellFactory(param -> new ListCell<Robo>() {
            @Override
            protected void updateItem(Robo robo, boolean empty) {
                super.updateItem(robo, empty);
                if (empty || robo == null) {
                    setText(null);
                    setStyle("");
                } else {
                    String status = obterStatusRobo(robo);
                    
                    // Verifica se o robô está em um prédio
                    Predio predioDoRobo = roboController.encontrarPredioDoRobo(robo, cidade);
                    String localizacao = "";
                    if (predioDoRobo != null) {
                        String tipoPredio = predioDoRobo.getTipo() == TipoPredio.COMERCIAL ? "Comercial" : "Residencial";
                        localizacao = String.format(" [📍 %s (%d,%d)]", tipoPredio, predioDoRobo.getPosX(), predioDoRobo.getPosY());
                    } else {
                        localizacao = " [📍 Disponível]";
                    }
                    
                    String info = String.format("%s - %s%s", robo.getTipo(), status, localizacao);
                    if (robo.isEmManutencao()) {
                        info += String.format(" [🔧 Manutenção: %d turno(s)]", robo.getTurnosRestantesManutencao());
                    }
                    setText(info);
                    
                    // Cores baseadas no status
                    if (robo.getEnergia() < 30 || robo.getIntegridade() < 30) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else if (robo.getEnergia() < 50 || robo.getIntegridade() < 50) {
                        setStyle("-fx-text-fill: #f39c12;");
                    } else {
                        setStyle("-fx-text-fill: #2ecc71;");
                    }
                }
            }
        });
        listaTodosRobos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                atualizarInformacoesRobo(newVal);
            }
        });
        
        painel.getChildren().add(listaTodosRobos);
        
        // Botão atualizar
        Button btnAtualizar = new Button("🔄 Atualizar Lista");
        btnAtualizar.setPrefWidth(Double.MAX_VALUE);
        btnAtualizar.setStyle("-fx-font-size: 12px; -fx-background-color: #3498db; -fx-text-fill: white;");
        btnAtualizar.setOnAction(e -> atualizarListaRobos());
        painel.getChildren().add(btnAtualizar);
        
        return painel;
    }
    
    private VBox criarPainelInformacoes() {
        VBox painel = new VBox(15);
        painel.setPrefWidth(400);
        painel.setPadding(new Insets(10));
        painel.setStyle("-fx-background-color: #34495e; -fx-background-radius: 5px;");
        
        Label titulo = new Label("INFORMAÇÕES DO ROBÔ");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titulo.setTextFill(Color.WHITE);
        painel.getChildren().add(titulo);
        
        // Informações do robô selecionado
        VBox secaoInfo = new VBox(8);
        secaoInfo.setPadding(new Insets(10));
        secaoInfo.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 5px;");
        
        HBox linhaTipo = criarLinhaInfo("Tipo:", labelTipoRobo = new Label("Nenhum selecionado"));
        secaoInfo.getChildren().add(linhaTipo);
        
        HBox linhaEnergia = criarLinhaInfo("⚡ Energia:", labelEnergia = new Label("-"));
        secaoInfo.getChildren().add(linhaEnergia);
        
        HBox linhaFelicidade = criarLinhaInfo("😊 Felicidade:", labelFelicidade = new Label("-"));
        secaoInfo.getChildren().add(linhaFelicidade);
        
        HBox linhaIntegridade = criarLinhaInfo("🔧 Integridade:", labelIntegridade = new Label("-"));
        secaoInfo.getChildren().add(linhaIntegridade);
        
        HBox linhaLocalizacao = criarLinhaInfo("📍 Localização:", labelLocalizacao = new Label("-"));
        secaoInfo.getChildren().add(linhaLocalizacao);
        
        HBox linhaStatus = criarLinhaInfo("📊 Status:", labelStatus = new Label("-"));
        secaoInfo.getChildren().add(linhaStatus);
        
        HBox linhaManutencao = criarLinhaInfo("🔧 Manutenção:", labelManutencao = new Label("-"));
        secaoInfo.getChildren().add(linhaManutencao);
        
        painel.getChildren().add(secaoInfo);
        
        // Separador
        Separator separador = new Separator();
        painel.getChildren().add(separador);
        
        // Seção de ações
        criarSecaoAcoes(painel);
        
        return painel;
    }
    
    private void criarSecaoAcoes(VBox painel) {
        VBox secaoAcoes = new VBox(10);
        secaoAcoes.setPadding(new Insets(10));
        
        Label titulo = new Label("AÇÕES");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titulo.setTextFill(Color.WHITE);
        secaoAcoes.getChildren().add(titulo);
        
        // Botão Manutenção
        btnManutencao = new Button("🔧 Fazer Manutenção");
        btnManutencao.setPrefWidth(Double.MAX_VALUE);
        btnManutencao.setPrefHeight(40);
        btnManutencao.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #e67e22; -fx-text-fill: white;");
        btnManutencao.setOnAction(e -> fazerManutencao());
        secaoAcoes.getChildren().add(btnManutencao);
        
        // Separador
        Separator separador1 = new Separator();
        secaoAcoes.getChildren().add(separador1);
        
        // Mover para prédio
        Label labelMover = new Label("Mover para Prédio:");
        labelMover.setFont(Font.font("Arial", 12));
        labelMover.setTextFill(Color.WHITE);
        secaoAcoes.getChildren().add(labelMover);
        
        comboBoxPredios = new ComboBox<>();
        comboBoxPredios.setPrefWidth(Double.MAX_VALUE);
        comboBoxPredios.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-font-size: 12px;");
        atualizarListaPredios();
        secaoAcoes.getChildren().add(comboBoxPredios);
        
        btnMoverParaPredio = new Button("➡️ Mover para Prédio");
        btnMoverParaPredio.setPrefWidth(Double.MAX_VALUE);
        btnMoverParaPredio.setStyle("-fx-font-size: 12px; -fx-background-color: #27ae60; -fx-text-fill: white;");
        btnMoverParaPredio.setOnAction(e -> moverParaPredio());
        secaoAcoes.getChildren().add(btnMoverParaPredio);
        
        // Separador
        Separator separador2 = new Separator();
        secaoAcoes.getChildren().add(separador2);
        
        // Remover de prédio
        btnRemoverDePredio = new Button("⬅️ Remover de Prédio");
        btnRemoverDePredio.setPrefWidth(Double.MAX_VALUE);
        btnRemoverDePredio.setStyle("-fx-font-size: 12px; -fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnRemoverDePredio.setOnAction(e -> removerDePredio());
        secaoAcoes.getChildren().add(btnRemoverDePredio);
        
        // Separador
        Separator separador3 = new Separator();
        secaoAcoes.getChildren().add(separador3);
        
        // Deletar robô
        btnDeletarRobo = new Button("🗑️ Deletar Robô");
        btnDeletarRobo.setPrefWidth(Double.MAX_VALUE);
        btnDeletarRobo.setPrefHeight(40);
        btnDeletarRobo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #8b0000; -fx-text-fill: white;");
        btnDeletarRobo.setOnAction(e -> deletarRobo());
        secaoAcoes.getChildren().add(btnDeletarRobo);
        
        painel.getChildren().add(secaoAcoes);
    }
    
    private HBox criarBotoesAcao() {
        HBox botoes = new HBox(10);
        botoes.setAlignment(Pos.CENTER);
        botoes.setPadding(new Insets(10));
        
        Button btnFechar = new Button("Fechar");
        btnFechar.setPrefWidth(150);
        btnFechar.setPrefHeight(35);
        btnFechar.setStyle("-fx-font-size: 12px; -fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnFechar.setOnAction(e -> stage.close());
        
        botoes.getChildren().add(btnFechar);
        return botoes;
    }
    
    private HBox criarLinhaInfo(String rotulo, Label valor) {
        HBox linha = new HBox(10);
        linha.setAlignment(Pos.CENTER_LEFT);
        
        Label label = new Label(rotulo);
        label.setFont(Font.font("Arial", 12));
        label.setTextFill(Color.WHITE);
        label.setMinWidth(100);
        
        valor.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        valor.setTextFill(Color.LIGHTYELLOW);
        HBox.setHgrow(valor, Priority.ALWAYS);
        
        linha.getChildren().addAll(label, valor);
        return linha;
    }
    
    private String obterStatusRobo(Robo robo) {
        if (robo.getEnergia() < 30 || robo.getIntegridade() < 30) {
            return "CRÍTICO";
        } else if (robo.getEnergia() < 50 || robo.getIntegridade() < 50) {
            return "ATENÇÃO";
        } else {
            return "OK";
        }
    }
    
    private void atualizarListaRobos() {
        if (cidade != null && listaTodosRobos != null) {
            listaTodosRobos.getItems().clear();
            // Mostra TODOS os robôs da cidade, não apenas os disponíveis
            listaTodosRobos.getItems().addAll(cidade.getRobos());
        }
    }
    
    private void atualizarListaPredios() {
        if (cidade != null && comboBoxPredios != null) {
            comboBoxPredios.getItems().clear();
            
            for (Predio predio : cidade.getPredios()) {
                // Inclui prédios funcionais e decorativos (exceto Centro)
                if (predio.getTipo() != TipoPredio.CENTRO) {
                    comboBoxPredios.getItems().add(predio);
                }
            }
            
            // Define o formato de exibição
            comboBoxPredios.setCellFactory(param -> new ListCell<Predio>() {
                @Override
                protected void updateItem(Predio predio, boolean empty) {
                    super.updateItem(predio, empty);
                    if (empty || predio == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        String tipo = obterNomeTipoPredio(predio);
                        int qtdRobos = obterQuantidadeRobos(predio);
                        int maxRobos = obterMaxRobos(predio);
                        setText(String.format("%s (%d/%d robôs) - Pos: (%d, %d)", 
                            tipo, qtdRobos, maxRobos, predio.getPosX(), predio.getPosY()));
                        setStyle("-fx-text-fill: #2c3e50; -fx-background-color: #ecf0f1; -fx-font-size: 12px;");
                    }
                }
            });
            
            comboBoxPredios.setButtonCell(new ListCell<Predio>() {
                @Override
                protected void updateItem(Predio predio, boolean empty) {
                    super.updateItem(predio, empty);
                    if (empty || predio == null) {
                        setText("Selecione um prédio");
                        setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
                    } else {
                        String tipo = predio.getTipo() == TipoPredio.COMERCIAL ? "Comercial" : "Residencial";
                        setText(tipo + " - Pos: (" + predio.getPosX() + ", " + predio.getPosY() + ")");
                        setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 12px;");
                    }
                }
            });
        }
    }
    
    private void atualizarInformacoesRobo(Robo robo) {
        if (robo == null) {
            labelTipoRobo.setText("Nenhum selecionado");
            labelEnergia.setText("-");
            labelFelicidade.setText("-");
            labelIntegridade.setText("-");
            labelLocalizacao.setText("-");
            labelStatus.setText("-");
            labelManutencao.setText("-");
            btnManutencao.setDisable(true);
            return;
        }
        
        labelTipoRobo.setText(robo.getTipo().toString());
        labelEnergia.setText(String.format("%.1f%%", robo.getEnergia()));
        labelFelicidade.setText(String.format("%.1f%%", robo.getFelicidade()));
        labelIntegridade.setText(String.format("%.1f%%", robo.getIntegridade()));
        labelLocalizacao.setText(String.format("(%d, %d)", robo.getPosX(), robo.getPosY()));
        labelStatus.setText(obterStatusRobo(robo));
        
        // Atualiza informações de manutenção
        if (robo.isEmManutencao()) {
            labelManutencao.setText(String.format("Em manutenção (%d turno(s) restante(s))", robo.getTurnosRestantesManutencao()));
            labelManutencao.setTextFill(Color.ORANGE);
            btnManutencao.setDisable(true);
            btnManutencao.setText("🔧 Em Manutenção...");
        } else {
            labelManutencao.setText("Disponível");
            labelManutencao.setTextFill(Color.GREEN);
            btnManutencao.setDisable(false);
            btnManutencao.setText("🔧 Fazer Manutenção");
        }
        
        // Atualiza cores baseadas no status
        if (robo.getEnergia() < 30 || robo.getIntegridade() < 30) {
            labelStatus.setTextFill(Color.RED);
        } else if (robo.getEnergia() < 50 || robo.getIntegridade() < 50) {
            labelStatus.setTextFill(Color.ORANGE);
        } else {
            labelStatus.setTextFill(Color.GREEN);
        }
        
        // Verifica se o robô está em algum prédio
        Predio predioDoRobo = roboController.encontrarPredioDoRobo(robo, cidade);
        if (predioDoRobo != null) {
            String tipoPredio = obterNomeTipoPredio(predioDoRobo);
            labelLocalizacao.setText(String.format("Prédio %s (%d, %d)", tipoPredio, predioDoRobo.getPosX(), predioDoRobo.getPosY()));
            btnMoverParaPredio.setDisable(false);
            btnRemoverDePredio.setDisable(false);
        } else {
            btnMoverParaPredio.setDisable(false);
            btnRemoverDePredio.setDisable(true);
        }
    }
    
    private void fazerManutencao() {
        Robo roboSelecionado = listaTodosRobos.getSelectionModel().getSelectedItem();
        if (roboSelecionado != null) {
            roboController.fazerManutencao(roboSelecionado);
            atualizarInformacoesRobo(roboSelecionado);
            atualizarListaRobos();
            
            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setTitle("Manutenção Iniciada");
            sucesso.setHeaderText(null);
            sucesso.setContentText("Manutenção iniciada com sucesso!\nO robô ficará em manutenção por 2 turnos.\nApós a conclusão, integridade e felicidade serão restauradas.");
            sucesso.showAndWait();
        } else {
            Alert aviso = new Alert(Alert.AlertType.WARNING);
            aviso.setTitle("Aviso");
            aviso.setHeaderText(null);
            aviso.setContentText("Selecione um robô da lista!");
            aviso.showAndWait();
        }
    }
    
    private void moverParaPredio() {
        Robo roboSelecionado = listaTodosRobos.getSelectionModel().getSelectedItem();
        Predio predioSelecionado = comboBoxPredios.getValue();
        
        if (roboSelecionado == null) {
            Alert aviso = new Alert(Alert.AlertType.WARNING);
            aviso.setTitle("Aviso");
            aviso.setHeaderText(null);
            aviso.setContentText("Selecione um robô da lista!");
            aviso.showAndWait();
            return;
        }
        
        if (predioSelecionado == null) {
            Alert aviso = new Alert(Alert.AlertType.WARNING);
            aviso.setTitle("Aviso");
            aviso.setHeaderText(null);
            aviso.setContentText("Selecione um prédio!");
            aviso.showAndWait();
            return;
        }
        
        // Validação de tipo de robô para prédio comercial
        if (predioSelecionado.getTipo() == TipoPredio.COMERCIAL) {
            if (roboSelecionado.getTipo() != TipoDeRobo.TRABALHADOR && 
                roboSelecionado.getTipo() != TipoDeRobo.ENGENHEIRO) {
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro");
                erro.setHeaderText(null);
                erro.setContentText("Prédios comerciais só aceitam Trabalhadores ou Engenheiros!");
                erro.showAndWait();
                return;
            }
        }
        
        boolean sucesso = roboController.moverRoboParaPredio(roboSelecionado, predioSelecionado, cidade);
        
        if (sucesso) {
            atualizarListaRobos();
            atualizarListaPredios();
            atualizarInformacoesRobo(roboSelecionado);
            
            Alert sucessoAlert = new Alert(Alert.AlertType.INFORMATION);
            sucessoAlert.setTitle("Robô Movido");
            sucessoAlert.setHeaderText(null);
            sucessoAlert.setContentText("Robô movido para o prédio com sucesso!");
            sucessoAlert.showAndWait();
        } else {
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setHeaderText(null);
            String mensagem = "Não foi possível mover o robô!\n";
            if (predioSelecionado instanceof PredioDecorativo) {
                int max = obterMaxRobos(predioSelecionado);
                mensagem += "Verifique se o prédio não está cheio (máx " + max + " robôs).";
            } else {
                mensagem += "Verifique se o prédio não está cheio (máx 5 robôs).";
            }
            erro.setContentText(mensagem);
            erro.showAndWait();
        }
    }
    
    private void removerDePredio() {
        Robo roboSelecionado = listaTodosRobos.getSelectionModel().getSelectedItem();
        
        if (roboSelecionado == null) {
            Alert aviso = new Alert(Alert.AlertType.WARNING);
            aviso.setTitle("Aviso");
            aviso.setHeaderText(null);
            aviso.setContentText("Selecione um robô da lista!");
            aviso.showAndWait();
            return;
        }
        
        // Verifica se o robô está realmente em um prédio
        Predio predioDoRobo = roboController.encontrarPredioDoRobo(roboSelecionado, cidade);
        if (predioDoRobo == null) {
            Alert aviso = new Alert(Alert.AlertType.INFORMATION);
            aviso.setTitle("Informação");
            aviso.setHeaderText(null);
            aviso.setContentText("Este robô não está em nenhum prédio!");
            aviso.showAndWait();
            return;
        }
        
        boolean sucesso = roboController.removerRoboDePredio(roboSelecionado, cidade);
        
        if (sucesso) {
            atualizarListaRobos();
            atualizarListaPredios();
            atualizarInformacoesRobo(roboSelecionado);
            
            Alert sucessoAlert = new Alert(Alert.AlertType.INFORMATION);
            sucessoAlert.setTitle("Robô Removido");
            sucessoAlert.setHeaderText(null);
            sucessoAlert.setContentText("Robô removido do prédio com sucesso!\nO robô agora está disponível.");
            sucessoAlert.showAndWait();
        } else {
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setHeaderText(null);
            erro.setContentText("Não foi possível remover o robô do prédio!");
            erro.showAndWait();
        }
    }
    
    /**
     * Exibe a interface de gerenciamento.
     */
    public void mostrar() {
        if (stage != null) {
            atualizarListaRobos();
            atualizarListaPredios();
            stage.showAndWait();
        }
    }
    
    /**
     * Deleta o robô selecionado após confirmação.
     */
    private void deletarRobo() {
        Robo roboSelecionado = listaTodosRobos.getSelectionModel().getSelectedItem();
        
        if (roboSelecionado == null) {
            Alert aviso = new Alert(Alert.AlertType.WARNING);
            aviso.setTitle("Aviso");
            aviso.setHeaderText(null);
            aviso.setContentText("Selecione um robô da lista!");
            aviso.showAndWait();
            return;
        }
        
        // Confirmação antes de deletar
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deletar Robô");
        confirmacao.setContentText(String.format(
            "Tem certeza que deseja deletar o robô %s?\n\n" +
            "Esta ação não pode ser desfeita.\n" +
            "O robô será removido permanentemente da cidade.",
            roboSelecionado.getTipo()
        ));
        
        confirmacao.showAndWait().ifPresent(resultado -> {
            if (resultado == ButtonType.OK) {
                boolean sucesso = roboController.deletarRobo(roboSelecionado, cidade);
                
                if (sucesso) {
                    // Limpa a seleção e atualiza a lista
                    listaTodosRobos.getSelectionModel().clearSelection();
                    atualizarListaRobos();
                    atualizarListaPredios();
                    atualizarInformacoesRobo(null); // Limpa as informações exibidas
                    
                    // Atualiza o mapa se disponível
                    if (mapaComCentro != null) {
                        mapaComCentro.atualizarMapa();
                    }
                    
                    Alert sucessoAlert = new Alert(Alert.AlertType.INFORMATION);
                    sucessoAlert.setTitle("Robô Deletado");
                    sucessoAlert.setHeaderText(null);
                    sucessoAlert.setContentText("Robô deletado com sucesso da cidade!");
                    sucessoAlert.showAndWait();
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro");
                    erro.setHeaderText(null);
                    erro.setContentText("Não foi possível deletar o robô!");
                    erro.showAndWait();
                }
            }
        });
    }
    
    /**
     * Define a referência ao mapa para permitir atualizações.
     * @param mapaComCentro A referência ao mapa
     */
    public void setMapaComCentro(MapaComCentro mapaComCentro) {
        this.mapaComCentro = mapaComCentro;
    }
    
    /**
     * Atualiza a cidade (útil quando a cidade é carregada ou modificada).
     */
    public void atualizarCidade(City novaCidade) {
        this.cidade = novaCidade;
        atualizarListaRobos();
        atualizarListaPredios();
    }
    
    /**
     * Retorna o nome do tipo de prédio para exibição.
     */
    private String obterNomeTipoPredio(Predio predio) {
        switch (predio.getTipo()) {
            case COMERCIAL:
                return "Comercial";
            case RESIDENCIAL:
                return "Residencial";
            case MONUMENTO:
                return "Monumento";
            case TORRE_COMUNICACAO:
                return "Torre de Comunicação";
            case ESTACAO_ENERGIA:
                return "Estação de Energia";
            case JARDIM_ZEN:
                return "Jardim Zen";
            case OBSERVATORIO:
                return "Observatório";
            default:
                return "Desconhecido";
        }
    }
    
    /**
     * Retorna a quantidade de robôs em um prédio.
     */
    private int obterQuantidadeRobos(Predio predio) {
        if (predio instanceof predioComercial) {
            return ((predioComercial) predio).getRobos().size();
        } else if (predio instanceof predioResidencial) {
            return ((predioResidencial) predio).getRobos().size();
        } else if (predio instanceof PredioDecorativo) {
            return ((PredioDecorativo) predio).getRobos().size();
        }
        return 0;
    }
    
    /**
     * Retorna o máximo de robôs permitidos em um prédio.
     */
    private int obterMaxRobos(Predio predio) {
        if (predio instanceof predioComercial || predio instanceof predioResidencial) {
            return 5;
        } else if (predio instanceof PredioDecorativo) {
            TipoPredio tipo = predio.getTipo();
            if (tipo == TipoPredio.MONUMENTO || tipo == TipoPredio.JARDIM_ZEN) {
                return 5;
            } else {
                return 3;
            }
        }
        return 0;
    }
}

