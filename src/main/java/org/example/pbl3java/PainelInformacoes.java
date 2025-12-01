package org.example.pbl3java;

import controller.JogoController;
import controller.RoboController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.City;
import model.TipoDeRobo;
import model.TipoPredio;
import model.Predio;
import model.Centro;

import java.io.IOException;

/**
 * Painel lateral que exibe informações sobre a cidade e permite controlar o jogo.
 */
public class PainelInformacoes extends VBox {
    
    private City cidade;
    private JogoController jogoController;
    private RoboController roboController;
    private MapaComCentro mapaComCentro; // Referência opcional ao mapa para atualização
    
    // Labels para exibir informações
    private Label labelDinheiro;
    private Label labelPecas;
    private Label labelTurno;
    private Label labelFelicidade;
    private Label labelTrabalhadores;
    private Label labelEngenheiros;
    private Label labelSegurancas;
    private Label labelTotalPredios;
    
    // Botões de controle
    private Button btnProximoTurno;
    private Button btnSalvarCidade;
    private Button btnVoltarMenu;
    private Stage stagePrincipal; // Referência ao Stage principal do jogo
    
    // Área de eventos
    private TextArea areaEventos;
    
    // Controles de treinamento e construção
    private TipoPredio tipoPredioSelecionado = TipoPredio.COMERCIAL;
    private Label labelFilaTreinamento;
    private InterfaceTreinamento interfaceTreinamento;
    private Button btnSelecionarComercial;
    private Button btnSelecionarResidencial;
    private Label labelTipoSelecionado;
    private InterfaceGerenciamentoRobos interfaceGerenciamentoRobos;
    
    public PainelInformacoes(City cidade, JogoController jogoController) {
        this.cidade = cidade;
        this.jogoController = jogoController;
        this.roboController = new RoboController();
        this.interfaceTreinamento = new InterfaceTreinamento(cidade, roboController);
        this.interfaceGerenciamentoRobos = new InterfaceGerenciamentoRobos(cidade, roboController);
        
        configurarPainel();
        criarComponentes();
        atualizarInformacoes();
    }
    
    /**
     * Define o Stage principal do jogo para permitir fechar e voltar ao menu.
     * @param stage O Stage principal
     */
    public void setStagePrincipal(Stage stage) {
        this.stagePrincipal = stage;
    }
    
    private void configurarPainel() {
        setPrefWidth(300);
        setPadding(new Insets(8));
        setSpacing(6);
        setStyle("-fx-background-color: #34495e; -fx-border-color: #2c3e50; -fx-border-width: 2px;");
    }
    
    private void criarComponentes() {
        // Container principal com scroll
        VBox container = new VBox(6);
        container.setPadding(new Insets(0));
        
        // Título do painel (menor)
        Label titulo = new Label("INFORMAÇÕES");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titulo.setTextFill(Color.WHITE);
        titulo.setAlignment(Pos.CENTER);
        titulo.setMaxWidth(Double.MAX_VALUE);
        titulo.setPadding(new Insets(0, 0, 5, 0));
        container.getChildren().add(titulo);
        
        // Separador
        Separator separador1 = new Separator();
        container.getChildren().add(separador1);
        
        // Seção combinada: Recursos, Estatísticas, Robôs e Prédios
        criarSecaoInfoCombinada(container);
        
        // Separador
        Separator separador2 = new Separator();
        container.getChildren().add(separador2);
        
        // Seção de Construção
        criarSecaoConstrucao(container);
        
        // Separador
        Separator separador3 = new Separator();
        container.getChildren().add(separador3);
        
        // Seção de Treinamento
        criarSecaoTreinamento(container);
        
        // Separador
        Separator separador4 = new Separator();
        container.getChildren().add(separador4);
        
        // Seção de Gerenciamento de Robôs
        criarSecaoGerenciamentoRobos(container);
        
        // Separador
        Separator separador5 = new Separator();
        container.getChildren().add(separador5);
        
        // Área de Eventos (compacta)
        criarAreaEventos(container);
        
        // Separador
        Separator separador6 = new Separator();
        container.getChildren().add(separador6);
        
        // Botões de Controle
        criarBotoesControle(container);
        
        // Adiciona ScrollPane
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPadding(new Insets(0));
        
        getChildren().add(scrollPane);
    }
    
    private void criarSecaoInfoCombinada(VBox container) {
        VBox secaoCombinada = new VBox(4);
        secaoCombinada.setPadding(new Insets(5));
        
        Label tituloSecao = new Label("ESTADO DA CIDADE");
        tituloSecao.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        tituloSecao.setTextFill(Color.LIGHTBLUE);
        secaoCombinada.getChildren().add(tituloSecao);
        
        // Recursos
        HBox linhaDinheiro = criarLinhaInfo("💰 Dinheiro:", labelDinheiro = new Label());
        secaoCombinada.getChildren().add(linhaDinheiro);
        HBox linhaPecas = criarLinhaInfo("🔧 Peças:", labelPecas = new Label());
        secaoCombinada.getChildren().add(linhaPecas);
        
        // Estatísticas
        HBox linhaTurno = criarLinhaInfo("📅 Turno:", labelTurno = new Label());
        secaoCombinada.getChildren().add(linhaTurno);
        HBox linhaFelicidade = criarLinhaInfo("😊 Felicidade:", labelFelicidade = new Label());
        secaoCombinada.getChildren().add(linhaFelicidade);
        
        // Robôs
        HBox linhaTrabalhadores = criarLinhaInfo("👷 Trabalhadores:", labelTrabalhadores = new Label());
        secaoCombinada.getChildren().add(linhaTrabalhadores);
        HBox linhaEngenheiros = criarLinhaInfo("🔨 Engenheiros:", labelEngenheiros = new Label());
        secaoCombinada.getChildren().add(linhaEngenheiros);
        HBox linhaSegurancas = criarLinhaInfo("🛡️ Seguranças:", labelSegurancas = new Label());
        secaoCombinada.getChildren().add(linhaSegurancas);
        
        // Prédios
        HBox linhaPredios = criarLinhaInfo("🏢 Prédios:", labelTotalPredios = new Label());
        secaoCombinada.getChildren().add(linhaPredios);
        
        container.getChildren().add(secaoCombinada);
    }
    
    private void criarAreaEventos(VBox container) {
        VBox secaoEventos = new VBox(4);
        secaoEventos.setPadding(new Insets(5));
        
        Label tituloSecao = new Label("EVENTOS");
        tituloSecao.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        tituloSecao.setTextFill(Color.YELLOW);
        secaoEventos.getChildren().add(tituloSecao);
        
        areaEventos = new TextArea();
        areaEventos.setPrefRowCount(4);
        areaEventos.setEditable(false);
        areaEventos.setWrapText(true);
        areaEventos.setStyle("-fx-background-color: #1a252f; -fx-text-fill: #2ecc71; -fx-font-size: 11px; -fx-font-weight: bold;");
        secaoEventos.getChildren().add(areaEventos);
        
        container.getChildren().add(secaoEventos);
    }
    
    private void criarSecaoTreinamento(VBox container) {
        VBox secaoTreinamento = new VBox(4);
        secaoTreinamento.setPadding(new Insets(5));
        
        Label tituloSecao = new Label("TREINAR ROBÔS");
        tituloSecao.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        tituloSecao.setTextFill(Color.CYAN);
        secaoTreinamento.getChildren().add(tituloSecao);
        
        // Informações de custo (menor)
        Label labelCusto = new Label("Custo: 200💰 + 50🔧");
        labelCusto.setFont(Font.font("Arial", 10));
        labelCusto.setTextFill(Color.WHITE);
        secaoTreinamento.getChildren().add(labelCusto);
        
        // Botão para abrir interface de treinamento (menor)
        Button btnAbrirTreinamento = new Button("🎓 Abrir Centro");
        btnAbrirTreinamento.setPrefWidth(Double.MAX_VALUE);
        btnAbrirTreinamento.setPrefHeight(32);
        btnAbrirTreinamento.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-background-color: #9b59b6; -fx-text-fill: white;");
        btnAbrirTreinamento.setOnAction(e -> abrirInterfaceTreinamento());
        secaoTreinamento.getChildren().add(btnAbrirTreinamento);
        
        // Fila de treinamento (resumo)
        labelFilaTreinamento = new Label("Fila: 0");
        labelFilaTreinamento.setFont(Font.font("Arial", 9));
        labelFilaTreinamento.setTextFill(Color.LIGHTGRAY);
        secaoTreinamento.getChildren().add(labelFilaTreinamento);
        
        container.getChildren().add(secaoTreinamento);
    }
    
    private void criarSecaoConstrucao(VBox container) {
        VBox secaoConstrucao = new VBox(6);
        secaoConstrucao.setPadding(new Insets(5));
        
        Label tituloSecao = new Label("CONSTRUIR PRÉDIO");
        tituloSecao.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        tituloSecao.setTextFill(Color.GOLD);
        secaoConstrucao.getChildren().add(tituloSecao);
        
        // Label mostrando tipo selecionado (menor)
        labelTipoSelecionado = new Label("Tipo: COMERCIAL");
        labelTipoSelecionado.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        labelTipoSelecionado.setTextFill(Color.LIGHTYELLOW);
        secaoConstrucao.getChildren().add(labelTipoSelecionado);
        
        // Botões visuais para seleção (menores)
        HBox botoesSelecao = new HBox(5);
        botoesSelecao.setAlignment(Pos.CENTER);
        
        // Botão Comercial
        btnSelecionarComercial = new Button();
        btnSelecionarComercial.setPrefWidth(135);
        btnSelecionarComercial.setPrefHeight(70);
        btnSelecionarComercial.setStyle(
            "-fx-font-size: 9px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: #27ae60; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5px; " +
            "-fx-border-color: #2ecc71; " +
            "-fx-border-width: 3px; " +
            "-fx-border-radius: 5px;"
        );
        btnSelecionarComercial.setText("🏢 COMERCIAL\n💰500 🔧300\nProduz");
        btnSelecionarComercial.setContentDisplay(ContentDisplay.CENTER);
        btnSelecionarComercial.setAlignment(Pos.CENTER);
        btnSelecionarComercial.setOnAction(e -> {
            tipoPredioSelecionado = TipoPredio.COMERCIAL;
            atualizarSelecaoTipoPredio();
        });
        
        // Botão Residencial
        btnSelecionarResidencial = new Button();
        btnSelecionarResidencial.setPrefWidth(135);
        btnSelecionarResidencial.setPrefHeight(70);
        btnSelecionarResidencial.setStyle(
            "-fx-font-size: 9px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: #34495e; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5px; " +
            "-fx-border-color: #7f8c8d; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px;"
        );
        btnSelecionarResidencial.setText("🏠 RESIDENCIAL\n💰500 🔧300\nRecupera");
        btnSelecionarResidencial.setContentDisplay(ContentDisplay.CENTER);
        btnSelecionarResidencial.setAlignment(Pos.CENTER);
        btnSelecionarResidencial.setOnAction(e -> {
            tipoPredioSelecionado = TipoPredio.RESIDENCIAL;
            atualizarSelecaoTipoPredio();
        });
        
        botoesSelecao.getChildren().addAll(btnSelecionarComercial, btnSelecionarResidencial);
        secaoConstrucao.getChildren().add(botoesSelecao);
        
        // Instrução (menor)
        Label instrucao = new Label("Clique no mapa para construir");
        instrucao.setFont(Font.font("Arial", 9));
        instrucao.setTextFill(Color.LIGHTGRAY);
        instrucao.setPadding(new Insets(5, 0, 0, 0));
        secaoConstrucao.getChildren().add(instrucao);
        
        // Inicializa a seleção visual
        atualizarSelecaoTipoPredio();
        
        container.getChildren().add(secaoConstrucao);
    }
    
    /**
     * Atualiza a aparência visual dos botões de seleção de tipo de prédio.
     */
    private void atualizarSelecaoTipoPredio() {
        // Reseta todos os botões funcionais
        String estiloNaoSelecionado = "-fx-font-size: 9px; -fx-font-weight: bold; -fx-background-color: #34495e; -fx-text-fill: white; -fx-background-radius: 5px; -fx-border-color: #7f8c8d; -fx-border-width: 2px; -fx-border-radius: 5px;";
        
        if (tipoPredioSelecionado == TipoPredio.COMERCIAL) {
            btnSelecionarComercial.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 5px; -fx-border-color: #2ecc71; -fx-border-width: 3px; -fx-border-radius: 5px;");
            btnSelecionarResidencial.setStyle(estiloNaoSelecionado);
            labelTipoSelecionado.setText("Tipo: 🏢 COMERCIAL");
            labelTipoSelecionado.setTextFill(Color.LIGHTGREEN);
        } else if (tipoPredioSelecionado == TipoPredio.RESIDENCIAL) {
            btnSelecionarResidencial.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5px; -fx-border-color: #5dade2; -fx-border-width: 3px; -fx-border-radius: 5px;");
            btnSelecionarComercial.setStyle(estiloNaoSelecionado);
            labelTipoSelecionado.setText("Tipo: 🏠 RESIDENCIAL");
            labelTipoSelecionado.setTextFill(Color.LIGHTBLUE);
        } else {
            // Nenhum tipo selecionado ou tipo não suportado - volta para COMERCIAL
            tipoPredioSelecionado = TipoPredio.COMERCIAL;
            atualizarSelecaoTipoPredio();
        }
    }
    
    
    private void criarSecaoGerenciamentoRobos(VBox container) {
        VBox secaoGerenciamento = new VBox(4);
        secaoGerenciamento.setPadding(new Insets(5));
        
        Label tituloSecao = new Label("GERENCIAR ROBÔS");
        tituloSecao.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        tituloSecao.setTextFill(Color.SALMON);
        secaoGerenciamento.getChildren().add(tituloSecao);
        
        // Botão para abrir interface completa (menor)
        Button btnAbrirGerenciamento = new Button("🤖 Abrir Gerenciamento");
        btnAbrirGerenciamento.setPrefWidth(Double.MAX_VALUE);
        btnAbrirGerenciamento.setPrefHeight(32);
        btnAbrirGerenciamento.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-background-color: #e67e22; -fx-text-fill: white;");
        btnAbrirGerenciamento.setOnAction(e -> abrirInterfaceGerenciamento());
        secaoGerenciamento.getChildren().add(btnAbrirGerenciamento);
        
        container.getChildren().add(secaoGerenciamento);
    }
    
    private void criarBotoesControle(VBox container) {
        VBox secaoBotoes = new VBox(5);
        secaoBotoes.setPadding(new Insets(5));
        
        // Botão Próximo Turno (menor)
        btnProximoTurno = new Button("▶ Próximo Turno");
        btnProximoTurno.setPrefWidth(Double.MAX_VALUE);
        btnProximoTurno.setPrefHeight(35);
        btnProximoTurno.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-color: #27ae60; -fx-text-fill: white;");
        btnProximoTurno.setOnAction(e -> avancarTurno());
        secaoBotoes.getChildren().add(btnProximoTurno);
        
        // Botão Salvar Cidade (menor)
        btnSalvarCidade = new Button("💾 Salvar Cidade");
        btnSalvarCidade.setPrefWidth(Double.MAX_VALUE);
        btnSalvarCidade.setPrefHeight(30);
        btnSalvarCidade.setStyle("-fx-font-size: 11px; -fx-background-color: #3498db; -fx-text-fill: white;");
        btnSalvarCidade.setOnAction(e -> salvarCidade());
        secaoBotoes.getChildren().add(btnSalvarCidade);
        
        // Botão Voltar ao Menu
        btnVoltarMenu = new Button("🏠 Voltar ao Menu");
        btnVoltarMenu.setPrefWidth(Double.MAX_VALUE);
        btnVoltarMenu.setPrefHeight(30);
        btnVoltarMenu.setStyle("-fx-font-size: 11px; -fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnVoltarMenu.setOnAction(e -> voltarAoMenu());
        secaoBotoes.getChildren().add(btnVoltarMenu);
        
        container.getChildren().add(secaoBotoes);
    }
    
    private HBox criarLinhaInfo(String rotulo, Label valor) {
        HBox linha = new HBox(8);
        linha.setAlignment(Pos.CENTER_LEFT);
        
        Label label = new Label(rotulo);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        label.setTextFill(Color.WHITE);
        label.setMinWidth(110);
        
        valor.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        valor.setTextFill(Color.LIGHTYELLOW);
        valor.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(valor, Priority.ALWAYS);
        
        linha.getChildren().addAll(label, valor);
        return linha;
    }
    
    /**
     * Atualiza todas as informações exibidas no painel.
     */
    public void atualizarInformacoes() {
        if (cidade == null) return;
        
        // Recursos
        labelDinheiro.setText(String.format("%.2f", cidade.getDinheiro()));
        labelPecas.setText(String.valueOf(cidade.getPecas()));
        
        // Estatísticas
        labelTurno.setText(String.valueOf(cidade.getTurnoAtual()));
        labelFelicidade.setText(String.format("%.1f%%", cidade.getFelicidadeMedia()));
        
        // Robôs por tipo
        int qtdTrabalhadores = roboController.listarRobosPorTipo(cidade, TipoDeRobo.TRABALHADOR).size();
        int qtdEngenheiros = roboController.listarRobosPorTipo(cidade, TipoDeRobo.ENGENHEIRO).size();
        int qtdSegurancas = roboController.listarRobosPorTipo(cidade, TipoDeRobo.SEGURANCA).size();
        
        labelTrabalhadores.setText(String.valueOf(qtdTrabalhadores));
        labelEngenheiros.setText(String.valueOf(qtdEngenheiros));
        labelSegurancas.setText(String.valueOf(qtdSegurancas));
        
        // Prédios
        labelTotalPredios.setText(String.valueOf(cidade.getPredios().size()));
        
        // Atualiza controles
        atualizarFilaTreinamento();
        atualizarListaRobos();
    }
    
    /**
     * Abre a interface de treinamento.
     */
    private void abrirInterfaceTreinamento() {
        if (interfaceTreinamento != null) {
            interfaceTreinamento.atualizarCidade(cidade);
            interfaceTreinamento.mostrar();
            // Atualiza informações após fechar a interface
            atualizarInformacoes();
            // Atualiza o mapa (novos robôs podem ter sido treinados)
            if (mapaComCentro != null) {
                mapaComCentro.atualizarMapa();
            }
        }
    }
    
    /**
     * Atualiza a fila de treinamento exibida.
     */
    private void atualizarFilaTreinamento() {
        if (cidade != null && labelFilaTreinamento != null) {
            // Encontra o Centro
            for (Predio predio : cidade.getPredios()) {
                if (predio instanceof Centro) {
                    Centro centro = (Centro) predio;
                    int tamanhoFila = centro.getFilaDeTreinamento().size();
                    labelFilaTreinamento.setText("Fila: " + tamanhoFila);
                    break;
                }
            }
        }
    }
    
    /**
     * Atualiza a lista de robôs disponíveis.
     * (Método mantido para compatibilidade, mas lista foi removida para economizar espaço)
     */
    private void atualizarListaRobos() {
        // Lista removida para economizar espaço no painel
    }
    
    /**
     * Abre a interface de gerenciamento de robôs.
     */
    private void abrirInterfaceGerenciamento() {
        if (interfaceGerenciamentoRobos != null) {
            interfaceGerenciamentoRobos.atualizarCidade(cidade);
            interfaceGerenciamentoRobos.mostrar();
            // Atualiza informações após fechar a interface
            atualizarInformacoes();
            // Atualiza o mapa (robôs podem ter sido movidos)
            if (mapaComCentro != null) {
                mapaComCentro.atualizarMapa();
            }
        }
    }
    
    /**
     * Retorna o tipo de prédio selecionado para construção.
     * @return O tipo de prédio selecionado
     */
    public TipoPredio getTipoPredioSelecionado() {
        return tipoPredioSelecionado;
    }

    /**
     * Define a referência ao mapa para permitir atualizações.
     * @param mapaComCentro A referência ao mapa
     */
    public void setMapaComCentro(MapaComCentro mapaComCentro) {
        this.mapaComCentro = mapaComCentro;
        // Passa a referência do mapa para a interface de gerenciamento
        if (interfaceGerenciamentoRobos != null) {
            interfaceGerenciamentoRobos.setMapaComCentro(mapaComCentro);
        }
    }
    
    /**
     * Adiciona um evento à área de eventos.
     */
    public void adicionarEvento(String evento) {
        if (areaEventos != null) {
            String textoAtual = areaEventos.getText();
            String novoTexto = "Turno " + cidade.getTurnoAtual() + ": " + evento;
            if (!textoAtual.isEmpty()) {
                areaEventos.setText(novoTexto + "\n" + textoAtual);
            } else {
                areaEventos.setText(novoTexto);
            }
            
            // Limita a 10 eventos mais recentes
            String[] linhas = areaEventos.getText().split("\n");
            if (linhas.length > 10) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    if (i > 0) sb.append("\n");
                    sb.append(linhas[i]);
                }
                areaEventos.setText(sb.toString());
            }
        }
    }
    
    /**
     * Avança para o próximo turno.
     */
    private void avancarTurno() {
        if (jogoController != null) {
            jogoController.proximoTurno();
            atualizarInformacoes();
            
            // Atualiza o mapa após avançar o turno (novos robôs podem ter sido criados)
            if (mapaComCentro != null) {
                mapaComCentro.atualizarMapa();
            }
            
            // Adiciona eventos do turno à área de eventos
            if (jogoController.getUltimoTurno() != null) {
                var eventos = jogoController.getUltimoTurno().getEventosOcorridos();
                if (!eventos.isEmpty()) {
                    for (String evento : eventos) {
                        adicionarEvento(evento);
                    }
                }
                // Não mostra mensagem se não houver eventos - é normal não ter eventos em alguns turnos
            }
        }
    }
    
    /**
     * Salva a cidade atual.
     */
    private void salvarCidade() {
        if (jogoController != null && cidade != null) {
            try {
                jogoController.salvarCity();
                Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                sucesso.setTitle("Cidade Salva");
                sucesso.setHeaderText(null);
                sucesso.setContentText("A cidade \"" + cidade.getNome() + "\" foi salva com sucesso!");
                sucesso.showAndWait();
            } catch (IOException e) {
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro ao Salvar");
                erro.setHeaderText(null);
                erro.setContentText("Não foi possível salvar a cidade: " + e.getMessage());
                erro.showAndWait();
            }
        }
    }
    
    /**
     * Volta para o menu principal, perguntando se deseja salvar antes.
     */
    private void voltarAoMenu() {
        if (stagePrincipal == null) {
            return;
        }
        
        // Pergunta se deseja salvar antes de sair
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Voltar ao Menu");
        confirmacao.setHeaderText("Voltar ao Menu Principal");
        confirmacao.setContentText("Deseja salvar a cidade antes de voltar ao menu?");
        
        ButtonType btnSalvar = new ButtonType("Salvar e Sair");
        ButtonType btnSair = new ButtonType("Sair sem Salvar");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        confirmacao.getButtonTypes().setAll(btnSalvar, btnSair, btnCancelar);
        
        confirmacao.showAndWait().ifPresent(resultado -> {
            if (resultado == btnSalvar) {
                // Salva e depois fecha
                try {
                    if (jogoController != null && cidade != null) {
                        jogoController.salvarCity();
                    }
                } catch (IOException e) {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro ao Salvar");
                    erro.setHeaderText(null);
                    erro.setContentText("Não foi possível salvar a cidade: " + e.getMessage());
                    erro.showAndWait();
                    return; // Se deu erro, não fecha
                }
                fecharJogoEAbrirMenu();
            } else if (resultado == btnSair) {
                // Fecha sem salvar
                fecharJogoEAbrirMenu();
            }
            // Se cancelar, não faz nada
        });
    }
    
    /**
     * Fecha a janela do jogo e abre o menu principal novamente.
     */
    private void fecharJogoEAbrirMenu() {
        if (stagePrincipal != null) {
            stagePrincipal.close();
        }
        
        // Abre o menu novamente
        try {
            MenuInicial menu = new MenuInicial();
            Stage menuStage = new Stage();
            menu.start(menuStage);
        } catch (Exception e) {
            System.err.println("Erro ao abrir menu: " + e.getMessage());
        }
    }
    
    /**
     * Define a cidade atual (útil para atualizar após carregar).
     */
    public void setCidade(City cidade) {
        this.cidade = cidade;
        if (interfaceTreinamento != null) {
            interfaceTreinamento.atualizarCidade(cidade);
        }
        if (interfaceGerenciamentoRobos != null) {
            interfaceGerenciamentoRobos.atualizarCidade(cidade);
        }
        atualizarInformacoes();
    }
}

