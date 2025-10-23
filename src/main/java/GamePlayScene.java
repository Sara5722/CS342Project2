// GamePlayScene.java - Complete with Drawing Logic and Results
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.util.*;

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
    private Button nextDrawingButton;
    private BetCard betCard;
    private Label statusMessage;
    private Label selectedSpotsLabel;
    private Label drawingProgressLabel;

    // Results display components
    private Label drawnNumbersDisplay;
    private Label matchesDisplay;
    private Label winsDisplay;
    private Label totalWinsDisplay;

    private VBox rootLayout;
    private HBox controlPanel;
    private VBox resultsPanel;

    private boolean drawingInProgress = false;

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

    private void handleDrawingSelection() {
        // This method is called whenever a drawings toggle button is clicked
        validateStartConditions();

        // Update status message to show drawing selection
        if (drawingsToggleGroup.getSelectedToggle() != null) {
            ToggleButton selectedDrawing = (ToggleButton) drawingsToggleGroup.getSelectedToggle();
            int drawingsCount = Integer.parseInt(selectedDrawing.getText());
            updateStatusMessage("Selected " + drawingsCount + " drawing(s). " +
                    (betCard.isSelectionValid() ? "Ready to start!" : "Please select your numbers."));
        }
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

        // Drawings selection - FIXED VERSION
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

        // Add event handlers for drawing selection
        draw1.setOnAction(e -> handleDrawingSelection());
        draw2.setOnAction(e -> handleDrawingSelection());
        draw3.setOnAction(e -> handleDrawingSelection());
        draw4.setOnAction(e -> handleDrawingSelection());

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

        nextDrawingButton = new Button("Next Drawing");
        nextDrawingButton.setOnAction(e -> handleNextDrawing());
        nextDrawingButton.setDisable(true);

        HBox actionButtons = new HBox(10, autoPickButton, startDrawingButton, resetButton, nextDrawingButton);
        actionButtons.setAlignment(Pos.CENTER);

        // Status messages
        statusMessage = new Label("Please select number of spots to begin.");
        statusMessage.setFont(new Font(14));
        statusMessage.setStyle("-fx-text-fill: blue;");

        selectedSpotsLabel = new Label("Selected: 0/0");
        selectedSpotsLabel.setFont(new Font(12));

        drawingProgressLabel = new Label("");
        drawingProgressLabel.setFont(new Font(12));
        drawingProgressLabel.setStyle("-fx-text-fill: darkgreen;");

        // Combine controls
        VBox controls = new VBox(10);
        controls.getChildren().addAll(
                spotsBox, drawingsBox, actionButtons, statusMessage, selectedSpotsLabel, drawingProgressLabel
        );
        controls.setPadding(new Insets(15));
        controls.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-background-color: #f5f5f5;");

        controlPanel = new HBox(controls);
    }

    private void createResultsPanel() {
        Label resultsTitle = new Label("Drawing Results");
        resultsTitle.setFont(new Font(16));
        resultsTitle.setStyle("-fx-font-weight: bold;");

        drawnNumbersDisplay = new Label("Drawn Numbers: ");
        drawnNumbersDisplay.setWrapText(true);
        drawnNumbersDisplay.setPrefWidth(200);

        matchesDisplay = new Label("Matches: ");
        matchesDisplay.setWrapText(true);

        winsDisplay = new Label("This Drawing: $0.00");
        winsDisplay.setStyle("-fx-font-weight: bold;");

        totalWinsDisplay = new Label("Total Won: $0.00");
        totalWinsDisplay.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");

        VBox resultsContent = new VBox(8, resultsTitle, drawnNumbersDisplay, matchesDisplay, winsDisplay, totalWinsDisplay);
        resultsContent.setPadding(new Insets(15));

        resultsPanel = new VBox(resultsContent);
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
        gameState.setPlayerNumbers(betCard.getSelectedNumbers()); // This line was missing!
        updateStatusMessage("Auto-pick completed! " + betCard.getSelectedCount() + "/" + gameState.getPlayerSpots() + " numbers selected.");
        validateStartConditions();
    }

    private void handleStartDrawing() {
        if (drawingInProgress) return;

        // Get selected drawings count
        ToggleButton selectedDrawing = (ToggleButton) drawingsToggleGroup.getSelectedToggle();
        int drawingsCount = Integer.parseInt(selectedDrawing.getText());

        // Set up game state
        gameState.startNewDrawingSession(drawingsCount);
        gameState.setPlayerNumbers(betCard.getSelectedNumbers());

        // Disable controls during drawing
        setControlsDisabled(true);
        drawingInProgress = true;

        // Start the first drawing
        startNextDrawing();
    }

    private void startNextDrawing() {
        drawingProgressLabel.setText("Drawing " + gameState.getCurrentDrawingNumber() + " of " + gameState.getTotalDrawings());

        // Clear previous results
        drawnNumbersDisplay.setText("Drawn Numbers: Drawing...");
        matchesDisplay.setText("Matches: ");
        winsDisplay.setText("This Drawing: $0.00");

        // Run drawing with animation
        animateDrawing();
    }

    private void animateDrawing() {
        Set<Integer> drawnNumbers = gameState.runDrawing();
        List<Integer> drawnList = new ArrayList<>(drawnNumbers);
        Collections.shuffle(drawnList); // Randomize display order

        // Display numbers one by one with pauses
        new Thread(() -> {
            StringBuilder displayedNumbers = new StringBuilder("Drawn Numbers: ");

            for (int i = 0; i < drawnList.size(); i++) {
                int number = drawnList.get(i);
                final int currentIndex = i;
                final String currentDisplay = displayedNumbers.toString() + number;

                // Update UI on JavaFX thread
                Platform.runLater(() -> {
                    drawnNumbersDisplay.setText(currentDisplay);
                });

                // Pause between numbers
                try {
                    Thread.sleep(500); // 500ms pause between numbers
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (i < drawnList.size() - 1) {
                    displayedNumbers.append(number).append(", ");
                }
            }

            // After all numbers are drawn, show results
            Platform.runLater(() -> {
                showDrawingResults();
            });
        }).start();
    }

    private void showDrawingResults() {
        Set<Integer> matches = gameState.getMatches();
        double winnings = gameState.calculateWinnings(matches.size());

        // Highlight matches on bet card
        betCard.highlightMatches(gameState.getCurrentDrawnNumbers());

        // Update results display
        matchesDisplay.setText("Matches: " + matches.size() + " (" + matches + ")");
        winsDisplay.setText("This Drawing: $" + String.format("%.2f", winnings));
        totalWinsDisplay.setText("Total Won: $" + String.format("%.2f", gameState.getTotalWinnings()));

        // Update status message
        if (matches.size() > 0) {
            updateStatusMessage("Congratulations! You matched " + matches.size() + " numbers and won $" + String.format("%.2f", winnings) + "!");
        } else {
            updateStatusMessage("No matches this drawing. Better luck next time!");
        }

        // Enable next drawing button if there are more drawings
        if (gameState.hasMoreDrawings()) {
            nextDrawingButton.setDisable(false);
            drawingProgressLabel.setText("Ready for next drawing. Click 'Next Drawing'.");
        } else {
            // All drawings complete
            drawingProgressLabel.setText("All drawings complete! Total winnings: $" + String.format("%.2f", gameState.getTotalWinnings()));
            nextDrawingButton.setDisable(true);
            startDrawingButton.setDisable(true);
            updateStatusMessage("Game over! Click 'Reset' to play again.");
        }

        drawingInProgress = false;
    }

    private void handleNextDrawing() {
        if (gameState.hasMoreDrawings()) {
            nextDrawingButton.setDisable(true);
            startNextDrawing();
        }
    }

    private void resetGame() {
        betCard.reset();
        spotsToggleGroup.selectToggle(null);
        drawingsToggleGroup.selectToggle(null);
        gameState.resetForNewGame();
        startDrawingButton.setDisable(true);
        nextDrawingButton.setDisable(true);
        setControlsDisabled(false);
        drawingInProgress = false;

        // Clear results display
        drawnNumbersDisplay.setText("Drawn Numbers: ");
        matchesDisplay.setText("Matches: ");
        winsDisplay.setText("This Drawing: $0.00");
        totalWinsDisplay.setText("Total Won: $0.00");
        drawingProgressLabel.setText("");

        updateStatusMessage("Game reset. Please select number of spots to begin.");
        selectedSpotsLabel.setText("Selected: 0/0");
    }

    private void setControlsDisabled(boolean disabled) {
        for (Toggle toggle : spotsToggleGroup.getToggles()) {
            ((ToggleButton) toggle).setDisable(disabled);
        }
        for (Toggle toggle : drawingsToggleGroup.getToggles()) {
            ((ToggleButton) toggle).setDisable(disabled);
        }
        autoPickButton.setDisable(disabled);
        startDrawingButton.setDisable(disabled);
        betCard.disableSelection();
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

            // Provide helpful message about what's missing
            if (!spotsSelected) {
                statusMessage.setText("Please select number of spots to begin.");
            } else if (!numbersSelected) {
                statusMessage.setText("Please select " + gameState.getPlayerSpots() + " numbers or click Auto-pick.");
            } else if (!drawingsSelected) {
                statusMessage.setText("Please select number of drawings.");
            }
        }
    }

    private void updateStatusMessage(String message) {
        statusMessage.setText(message);
    }

    private void showRules() {
        Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
        rulesAlert.setTitle("Keno Rules");
        rulesAlert.setHeaderText("How to Play Keno");
        rulesAlert.setContentText("1. Choose how many spots to play (1, 4, 8, or 10 numbers)\n" +
                "2. Select your numbers on the bet card or use Auto-pick\n" +
                "3. Choose how many drawings to play (1-4)\n" +
                "4. Watch as 20 numbers are drawn one by one\n" +
                "5. Win based on how many numbers you match!\n\n" +
                "Payouts based on North Carolina State Lottery rules.");
        rulesAlert.showAndWait();
    }

    private void showOdds() {
        Alert oddsAlert = new Alert(Alert.AlertType.INFORMATION);
        oddsAlert.setTitle("Winning Odds");
        oddsAlert.setHeaderText("Keno Payouts and Odds");
        oddsAlert.setContentText("Spot 1:\n" +
                "• Match 1: $2\n\n" +
                "Spot 4:\n" +
                "• Match 2: $1\n" +
                "• Match 3: $5\n" +
                "• Match 4: $75\n\n" +
                "Spot 8:\n" +
                "• Match 4: $2\n" +
                "• Match 5: $12\n" +
                "• Match 6: $50\n" +
                "• Match 7: $750\n" +
                "• Match 8: $10,000\n\n" +
                "Spot 10:\n" +
                "• Match 0: $5\n" +
                "• Match 5: $2\n" +
                "• Match 6: $15\n" +
                "• Match 7: $100\n" +
                "• Match 8: $500\n" +
                "• Match 9: $5,000\n" +
                "• Match 10: $25,000");
        oddsAlert.showAndWait();
    }

    private void applyNewLook() {
        // Simple color change for now - can be enhanced
        if (rootLayout.getStyle().contains("lightblue")) {
            rootLayout.setStyle("-fx-background-color: white;");
            resultsPanel.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-background-color: #f0f0f0;");
            controlPanel.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-background-color: #f5f5f5;");
        } else {
            rootLayout.setStyle("-fx-background-color: lightblue;");
            resultsPanel.setStyle("-fx-border-color: darkblue; -fx-border-width: 2; -fx-background-color: #e6f3ff;");
            controlPanel.setStyle("-fx-border-color: darkblue; -fx-border-width: 2; -fx-background-color: #e6f3ff;");
        }
    }

    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(rootLayout, 1000, 700);
        }
        return scene;
    }
}