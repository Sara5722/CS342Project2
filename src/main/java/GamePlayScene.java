// GamePlayScene.java - Updated with Bet Card
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class GamePlayScene {
    private Scene scene;
    private KenoGame mainApp;
    private GameState gameState;

    private MenuBar menuBar;
    private ToggleGroup spotsToggleGroup;
    private ToggleGroup drawingsToggleGroup;
    private Button autoPickButton;
    private Button startDrawingButton;
    private Button resetButton;
    private BetCard betCard;
    private Label statusMessage;
    private Label selectedSpotsLabel;

    private VBox rootLayout;
    private HBox controlPanel;
    private VBox resultsPanel;

    public GamePlayScene(KenoGame mainApp, GameState gameState) {
        this.mainApp = mainApp;
        this.gameState = gameState;
        initialize();
    }

    private void initialize() {
        createMenuBar();
        createBetCard();
        createControlPanel();
        createResultsPanel();
        setupLayout();
    }

    private void createMenuBar() {
        Menu menu = new Menu("Menu");

        MenuItem rulesMenuItem = new MenuItem("Rules");
        MenuItem oddsMenuItem = new MenuItem("Odds");
        MenuItem newLookMenuItem = new MenuItem("New Look");
        MenuItem exitMenuItem = new MenuItem("Exit");

        rulesMenuItem.setOnAction(e -> showRules());
        oddsMenuItem.setOnAction(e -> showOdds());
        newLookMenuItem.setOnAction(e -> applyNewLook());
        exitMenuItem.setOnAction(e -> System.exit(0));

        menu.getItems().addAll(rulesMenuItem, oddsMenuItem, newLookMenuItem, exitMenuItem);
        menuBar = new MenuBar(menu);
    }

    private void createBetCard() {
        betCard = new BetCard();
    }

    private void createControlPanel() {
        // Spot selection
        Label spotsLabel = new Label("Select Spots:");
        spotsLabel.setFont(new Font(14));

        spotsToggleGroup = new ToggleGroup();
        ToggleButton spot1 = new ToggleButton("1");
        ToggleButton spot4 = new ToggleButton("4");
        ToggleButton spot8 = new ToggleButton("8");
        ToggleButton spot10 = new ToggleButton("10");

        spot1.setToggleGroup(spotsToggleGroup);
        spot4.setToggleGroup(spotsToggleGroup);
        spot8.setToggleGroup(spotsToggleGroup);
        spot10.setToggleGroup(spotsToggleGroup);

        spot1.setOnAction(e -> handleSpotSelection(1));
        spot4.setOnAction(e -> handleSpotSelection(4));
        spot8.setOnAction(e -> handleSpotSelection(8));
        spot10.setOnAction(e -> handleSpotSelection(10));

        HBox spotsBox = new HBox(10, spotsLabel, spot1, spot4, spot8, spot10);
        spotsBox.setAlignment(Pos.CENTER_LEFT);

        // Drawings selection
        Label drawingsLabel = new Label("Drawings:");
        drawingsLabel.setFont(new Font(14));

        drawingsToggleGroup = new ToggleGroup();
        ToggleButton draw1 = new ToggleButton("1");
        ToggleButton draw2 = new ToggleButton("2");
        ToggleButton draw3 = new ToggleButton("3");
        ToggleButton draw4 = new ToggleButton("4");

        draw1.setToggleGroup(drawingsToggleGroup);
        draw2.setToggleGroup(drawingsToggleGroup);
        draw3.setToggleGroup(drawingsToggleGroup);
        draw4.setToggleGroup(drawingsToggleGroup);

        HBox drawingsBox = new HBox(10, drawingsLabel, draw1, draw2, draw3, draw4);
        drawingsBox.setAlignment(Pos.CENTER_LEFT);

        // Action buttons
        autoPickButton = new Button("Auto-pick");
        autoPickButton.setOnAction(e -> handleAutoPick());

        startDrawingButton = new Button("Start Drawing");
        startDrawingButton.setOnAction(e -> handleStartDrawing());
        startDrawingButton.setDisable(true);

        resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetGame());

        HBox actionButtons = new HBox(10, autoPickButton, startDrawingButton, resetButton);
        actionButtons.setAlignment(Pos.CENTER);

        // Status message
        statusMessage = new Label("Please select number of spots to begin.");
        statusMessage.setFont(new Font(14));
        statusMessage.setStyle("-fx-text-fill: blue;");

        selectedSpotsLabel = new Label("Selected: 0/0");
        selectedSpotsLabel.setFont(new Font(12));

        // Combine controls
        VBox controls = new VBox(15);
        controls.getChildren().addAll(
                spotsBox, drawingsBox, actionButtons, statusMessage, selectedSpotsLabel
        );
        controls.setPadding(new Insets(15));
        controls.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-background-color: #f5f5f5;");

        controlPanel = new HBox(controls);
    }

    private void createResultsPanel() {
        Label resultsTitle = new Label("Drawing Results");
        resultsTitle.setFont(new Font(16));
        resultsTitle.setStyle("-fx-font-weight: bold;");

        // We'll add actual results display later
        Label placeholder = new Label("Results will appear here after drawings.");
        placeholder.setWrapText(true);

        resultsPanel = new VBox(10, resultsTitle, placeholder);
        resultsPanel.setPadding(new Insets(15));
        resultsPanel.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-background-color: #f0f0f0;");
        resultsPanel.setPrefWidth(250);
    }

    private void setupLayout() {
        // Main content area
        HBox mainContent = new HBox(20);
        mainContent.setPadding(new Insets(15));
        mainContent.getChildren().addAll(betCard.getGridPane(), resultsPanel);

        // Root layout
        rootLayout = new VBox();
        rootLayout.getChildren().addAll(menuBar, controlPanel, mainContent);
    }

    private void handleSpotSelection(int spots) {
        gameState.setPlayerSpots(spots);
        betCard.enableSelection(spots);
        updateStatusMessage("Please select " + spots + " numbers or click Auto-pick.");
        validateStartConditions();
    }

    private void handleAutoPick() {
        if (gameState.getPlayerSpots() == 0) {
            statusMessage.setText("Please select number of spots first.");
            return;
        }
        betCard.quickPick();
        updateStatusMessage("Auto-pick completed! " + betCard.getSelectedCount() + "/" + gameState.getPlayerSpots() + " numbers selected.");
        validateStartConditions();
    }

    private void handleStartDrawing() {
        // This will be implemented in the next step
        statusMessage.setText("Drawing functionality coming soon!");
    }

    private void resetGame() {
        betCard.reset();
        spotsToggleGroup.selectToggle(null);
        drawingsToggleGroup.selectToggle(null);
        gameState.setPlayerSpots(0);
        startDrawingButton.setDisable(true);
        statusMessage.setText("Game reset. Please select number of spots to begin.");
        selectedSpotsLabel.setText("Selected: 0/0");
    }

    private void validateStartConditions() {
        boolean spotsSelected = gameState.getPlayerSpots() > 0;
        boolean numbersSelected = betCard.isSelectionValid();
        boolean drawingsSelected = drawingsToggleGroup.getSelectedToggle() != null;

        selectedSpotsLabel.setText("Selected: " + betCard.getSelectedCount() + "/" + gameState.getPlayerSpots());

        if (spotsSelected && numbersSelected && drawingsSelected) {
            startDrawingButton.setDisable(false);
            statusMessage.setText("Ready to start drawing! Click 'Start Drawing'.");
        } else {
            startDrawingButton.setDisable(true);
        }
    }

    private void updateStatusMessage(String message) {
        statusMessage.setText(message);
    }

    // Existing methods (keep these the same)
    private void showRules() {
        Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
        rulesAlert.setTitle("Keno Rules");
        rulesAlert.setHeaderText("How to Play Keno");
        rulesAlert.setContentText("Game rules will be shown here.");
        rulesAlert.showAndWait();
    }

    private void showOdds() {
        Alert oddsAlert = new Alert(Alert.AlertType.INFORMATION);
        oddsAlert.setTitle("Winning Odds");
        oddsAlert.setHeaderText("Keno Payouts and Odds");
        oddsAlert.setContentText("Odds information will be shown here.");
        oddsAlert.showAndWait();
    }

    private void applyNewLook() {
        // Simple color change for now
        if (rootLayout.getStyle().contains("lightblue")) {
            rootLayout.setStyle("-fx-background-color: white;");
        } else {
            rootLayout.setStyle("-fx-background-color: lightblue;");
        }
    }

    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(rootLayout, 1000, 700);
        }
        return scene;
    }
}