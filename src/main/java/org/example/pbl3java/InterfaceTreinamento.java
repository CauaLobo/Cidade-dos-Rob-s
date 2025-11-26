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
import model.Centro;
import model.Predio;
import model.RoboEmTreinamento;
import model.TipoDeRobo;

import java.util.List;

/**
 * Interface dedicada para o treinamento de robôs.
 * Permite visualizar a fila de treinamento, iniciar novos treinamentos e ver informações detalhadas.
 */
public class InterfaceTreinamento {
    
    private Stage stage;
    private City cidade;
    private RoboController roboController;
    private Centro centro;
    
    // Componentes da interface
    private ComboBox<TipoDeRobo> comboBoxTipoRobo;
    private ListView<String> listaFilaTreinamento;
    private Label labelCustoDinheiro;
    private Label labelCustoPecas;
    private Label labelTempoTreinamento;
    private Label labelRecursosDisponiveis;
    
    public InterfaceTreinamento(City cidade, RoboController roboController) {
        this.cidade = cidade;
        this.roboController = roboController;
        this.centro = encontrarCentro();
        
        criarInterface();
    }
    
    private Centro encontrarCentro() {
        if (cidade == null) return null;
        
        for (Predio predio : cidade.getPredios()) {
            if (predio instanceof Centro) {
                return (Centro) predio;
            }
        }
        return null;
    }
    
    private void criarInterface() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Centro de Treinamento de Robôs");
        stage.setResizable(false);
        
        // Container principal
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2c3e50;");
        
        // Título
        Label titulo = new Label("🎓 CENTRO DE TREINAMENTO");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.CYAN);
        titulo.setAlignment(Pos.CENTER);
        titulo.setMaxWidth(Double.MAX_VALUE);
        root.getChildren().add(titulo);
        
        // Separador
        Separator separador1 = new Separator();
        root.getChildren().add(separador1);
        
        // Seção de Informações
        criarSecaoInformacoes(root);
        
        // Separador
        Separator separador2 = new Separator();
        root.getChildren().add(separador2);
        
        // Seção de Novo Treinamento
        criarSecaoNovoTreinamento(root);
        
        // Separador
        Separator separador3 = new Separator();
        root.getChildren().add(separador3);
        
        // Seção de Fila de Treinamento
        criarSecaoFilaTreinamento(root);
        
        // Separador
        Separator separador4 = new Separator();
        root.getChildren().add(separador4);
        
        // Botões de ação
        criarBotoesAcao(root);
        
        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        
        // Atualiza informações iniciais
        atualizarInformacoes();
    }
    
    private void criarSecaoInformacoes(VBox root) {
        VBox secao = new VBox(10);
        secao.setPadding(new Insets(10));
        secao.setStyle("-fx-background-color: #34495e; -fx-background-radius: 5px;");
        
        Label tituloSecao = new Label("INFORMAÇÕES");
        tituloSecao.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        tituloSecao.setTextFill(Color.WHITE);
        secao.getChildren().add(tituloSecao);
        
        // Custo de treinamento
        HBox linhaCustoDinheiro = criarLinhaInfo("💰 Custo (Dinheiro):", labelCustoDinheiro = new Label());
        secao.getChildren().add(linhaCustoDinheiro);
        
        HBox linhaCustoPecas = criarLinhaInfo("🔧 Custo (Peças):", labelCustoPecas = new Label());
        secao.getChildren().add(linhaCustoPecas);
        
        HBox linhaTempo = criarLinhaInfo("⏱️ Tempo de Treinamento:", labelTempoTreinamento = new Label());
        secao.getChildren().add(linhaTempo);
        
        // Recursos disponíveis
        HBox linhaRecursos = criarLinhaInfo("💵 Recursos Disponíveis:", labelRecursosDisponiveis = new Label());
        secao.getChildren().add(linhaRecursos);
        
        root.getChildren().add(secao);
    }
    
    private void criarSecaoNovoTreinamento(VBox root) {
        VBox secao = new VBox(10);
        secao.setPadding(new Insets(10));
        secao.setStyle("-fx-background-color: #34495e; -fx-background-radius: 5px;");
        
        Label tituloSecao = new Label("NOVO TREINAMENTO");
        tituloSecao.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        tituloSecao.setTextFill(Color.WHITE);
        secao.getChildren().add(tituloSecao);
        
        // Seleção de tipo
        Label labelTipo = new Label("Tipo de Robô:");
        labelTipo.setFont(Font.font("Arial", 12));
        labelTipo.setTextFill(Color.WHITE);
        secao.getChildren().add(labelTipo);
        
        comboBoxTipoRobo = new ComboBox<>();
        comboBoxTipoRobo.getItems().addAll(TipoDeRobo.TRABALHADOR, TipoDeRobo.ENGENHEIRO, TipoDeRobo.SEGURANCA);
        comboBoxTipoRobo.setValue(TipoDeRobo.TRABALHADOR);
        comboBoxTipoRobo.setPrefWidth(Double.MAX_VALUE);
        comboBoxTipoRobo.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
        secao.getChildren().add(comboBoxTipoRobo);
        
        // Descrição dos tipos
        TextArea descricaoTipos = new TextArea();
        descricaoTipos.setEditable(false);
        descricaoTipos.setPrefRowCount(4);
        descricaoTipos.setWrapText(true);
        descricaoTipos.setStyle("-fx-background-color: #1a252f; -fx-text-fill: #ecf0f1; -fx-font-size: 10px;");
        descricaoTipos.setText(
            "👷 TRABALHADOR: Trabalha em prédios comerciais gerando recursos\n" +
            "🔨 ENGENHEIRO: Aumenta produção em prédios comerciais (+20%)\n" +
            "🛡️ SEGURANÇA: Aumenta felicidade e reduz impacto de eventos"
        );
        secao.getChildren().add(descricaoTipos);
        
        // Botão Treinar
        Button btnTreinar = new Button("🎓 Iniciar Treinamento");
        btnTreinar.setPrefWidth(Double.MAX_VALUE);
        btnTreinar.setPrefHeight(40);
        btnTreinar.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #9b59b6; -fx-text-fill: white;");
        btnTreinar.setOnAction(e -> iniciarTreinamento());
        secao.getChildren().add(btnTreinar);
        
        root.getChildren().add(secao);
    }
    
    private void criarSecaoFilaTreinamento(VBox root) {
        VBox secao = new VBox(10);
        secao.setPadding(new Insets(10));
        secao.setStyle("-fx-background-color: #34495e; -fx-background-radius: 5px;");
        
        Label tituloSecao = new Label("FILA DE TREINAMENTO");
        tituloSecao.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        tituloSecao.setTextFill(Color.WHITE);
        secao.getChildren().add(tituloSecao);
        
        // Lista da fila
        listaFilaTreinamento = new ListView<>();
        listaFilaTreinamento.setPrefHeight(150);
        listaFilaTreinamento.setStyle("-fx-background-color: #1a252f; -fx-text-fill: white;");
        secao.getChildren().add(listaFilaTreinamento);
        
        // Botão Atualizar
        Button btnAtualizar = new Button("🔄 Atualizar Fila");
        btnAtualizar.setPrefWidth(Double.MAX_VALUE);
        btnAtualizar.setStyle("-fx-font-size: 12px; -fx-background-color: #3498db; -fx-text-fill: white;");
        btnAtualizar.setOnAction(e -> atualizarFila());
        secao.getChildren().add(btnAtualizar);
        
        root.getChildren().add(secao);
    }
    
    private void criarBotoesAcao(VBox root) {
        HBox botoes = new HBox(10);
        botoes.setAlignment(Pos.CENTER);
        
        Button btnFechar = new Button("Fechar");
        btnFechar.setPrefWidth(150);
        btnFechar.setPrefHeight(35);
        btnFechar.setStyle("-fx-font-size: 12px; -fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnFechar.setOnAction(e -> stage.close());
        
        botoes.getChildren().add(btnFechar);
        root.getChildren().add(botoes);
    }
    
    private HBox criarLinhaInfo(String rotulo, Label valor) {
        HBox linha = new HBox(10);
        linha.setAlignment(Pos.CENTER_LEFT);
        
        Label label = new Label(rotulo);
        label.setFont(Font.font("Arial", 12));
        label.setTextFill(Color.WHITE);
        label.setMinWidth(180);
        
        valor.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        valor.setTextFill(Color.LIGHTYELLOW);
        HBox.setHgrow(valor, Priority.ALWAYS);
        
        linha.getChildren().addAll(label, valor);
        return linha;
    }
    
    private void atualizarInformacoes() {
        if (centro != null) {
            labelCustoDinheiro.setText(String.format("%.2f", centro.getCustoTreinamentoDinheiro()));
            labelCustoPecas.setText(String.valueOf(centro.getCustoTreinamentoPecas()));
            labelTempoTreinamento.setText("3 turnos");
            
            if (cidade != null) {
                String recursos = String.format("%.2f💰 / %d🔧", cidade.getDinheiro(), cidade.getPecas());
                labelRecursosDisponiveis.setText(recursos);
            }
        }
        
        atualizarFila();
    }
    
    private void atualizarFila() {
        if (centro != null && listaFilaTreinamento != null) {
            listaFilaTreinamento.getItems().clear();
            List<RoboEmTreinamento> fila = centro.getFilaDeTreinamento();
            
            if (fila.isEmpty()) {
                listaFilaTreinamento.getItems().add("Fila vazia - Nenhum robô em treinamento");
            } else {
                for (int i = 0; i < fila.size(); i++) {
                    RoboEmTreinamento treinamento = fila.get(i);
                    String info = String.format("%d. %s - %d turno(s) restante(s)", 
                        i + 1, 
                        treinamento.getTipo(), 
                        treinamento.getTempoRestante());
                    listaFilaTreinamento.getItems().add(info);
                }
            }
        }
    }
    
    private void iniciarTreinamento() {
        if (comboBoxTipoRobo.getValue() != null && cidade != null) {
            boolean sucesso = roboController.treinarRobo(cidade, comboBoxTipoRobo.getValue());
            
            if (sucesso) {
                atualizarInformacoes();
                
                Alert sucessoAlert = new Alert(Alert.AlertType.INFORMATION);
                sucessoAlert.setTitle("Treinamento Iniciado");
                sucessoAlert.setHeaderText(null);
                sucessoAlert.setContentText("Treinamento de " + comboBoxTipoRobo.getValue() + " iniciado com sucesso!\nO robô estará pronto em 3 turnos.");
                sucessoAlert.showAndWait();
            } else {
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro ao Treinar");
                erro.setHeaderText(null);
                
                if (centro == null) {
                    erro.setContentText("Centro não encontrado na cidade!");
                } else {
                    erro.setContentText("Recursos insuficientes!\nNecessário: " + 
                        centro.getCustoTreinamentoDinheiro() + "💰 e " + 
                        centro.getCustoTreinamentoPecas() + "🔧");
                }
                erro.showAndWait();
            }
        }
    }
    
    /**
     * Exibe a interface de treinamento.
     */
    public void mostrar() {
        if (stage != null) {
            atualizarInformacoes();
            stage.showAndWait();
        }
    }
    
    /**
     * Atualiza a cidade (útil quando a cidade é carregada ou modificada).
     */
    public void atualizarCidade(City novaCidade) {
        this.cidade = novaCidade;
        this.centro = encontrarCentro();
        atualizarInformacoes();
    }
}

