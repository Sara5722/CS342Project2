import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.*;
import javafx.stage.Stage;

public class GamePlayScene {
    private Scene scene;
    private KenoGame mainApp;
    private GameState gameState;

    // Your color palette
    private final String DARK_PURPLE = "#450693";
    private final String BRIGHT_PURPLE = "#8C00FF";
    private final String PINK = "#FF3F7F";
    private final String GOLD = "#FFC400";
    private final String WHITE = "#FFFFFF";
    private final String BLACK = "#000000";

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
        // Main "Menu" dropdown
        Menu mainMenu = new Menu("Menu");
        mainMenu.setStyle("-fx-background-color:" + GOLD + ";");
        // Menu items
        MenuItem rulesMenuItem = new MenuItem("Rules");
        MenuItem oddsMenuItem = new MenuItem("Odds");
        MenuItem newLookMenuItem = new MenuItem("New Look");
        MenuItem exitMenuItem = new MenuItem("Exit");

        // Actions
        rulesMenuItem.setOnAction(e -> showRules());
        oddsMenuItem.setOnAction(e -> showOdds());
        newLookMenuItem.setOnAction(e -> applyNewLook()); // We'll fix this next
        exitMenuItem.setOnAction(e -> System.exit(0));

        // Organize Menu Items
        mainMenu.getItems().addAll(
                rulesMenuItem,
                oddsMenuItem,
                new SeparatorMenuItem(),
                newLookMenuItem,
                new SeparatorMenuItem(),
                exitMenuItem
        );

        // Create MenuBar
        menuBar = new MenuBar(mainMenu);
        menuBar.setStyle(
                "-fx-background-color: " + DARK_PURPLE + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 5px;" +
                        "-fx-selection-bar: " + BRIGHT_PURPLE + ";" +
                        "-fx-text-fill: white;"
        );
    }


    private void createBetCard() {
        betCard = new BetCard();
    }

    private void handleDrawingSelection() {
        validateStartConditions();
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
        spotsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        spotsLabel.setTextFill(Color.web(WHITE));

        spotsToggleGroup = new ToggleGroup();
        ToggleButton spot1 = createToggleButton("1");
        ToggleButton spot4 = createToggleButton("4");
        ToggleButton spot8 = createToggleButton("8");
        ToggleButton spot10 = createToggleButton("10");

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
        drawingsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        drawingsLabel.setTextFill(Color.web(WHITE));

        drawingsToggleGroup = new ToggleGroup();
        ToggleButton draw1 = createToggleButton("1");
        ToggleButton draw2 = createToggleButton("2");
        ToggleButton draw3 = createToggleButton("3");
        ToggleButton draw4 = createToggleButton("4");

        draw1.setToggleGroup(drawingsToggleGroup);
        draw2.setToggleGroup(drawingsToggleGroup);
        draw3.setToggleGroup(drawingsToggleGroup);
        draw4.setToggleGroup(drawingsToggleGroup);

        draw1.setOnAction(e -> handleDrawingSelection());
        draw2.setOnAction(e -> handleDrawingSelection());
        draw3.setOnAction(e -> handleDrawingSelection());
        draw4.setOnAction(e -> handleDrawingSelection());

        HBox drawingsBox = new HBox(10, drawingsLabel, draw1, draw2, draw3, draw4);
        drawingsBox.setAlignment(Pos.CENTER_LEFT);

        // Action buttons
        autoPickButton = createActionButton("Auto-pick");
        autoPickButton.setOnAction(e -> handleAutoPick());

        startDrawingButton = createActionButton("Start Drawing");
        startDrawingButton.setOnAction(e -> handleStartDrawing());
        startDrawingButton.setDisable(true);

        resetButton = createActionButton("Reset");
        resetButton.setOnAction(e -> resetGame());

        nextDrawingButton = createActionButton("Next Drawing");
        nextDrawingButton.setOnAction(e -> handleNextDrawing());
        nextDrawingButton.setDisable(true);

        HBox actionButtons = new HBox(10, autoPickButton, startDrawingButton, resetButton, nextDrawingButton);
        actionButtons.setAlignment(Pos.CENTER);

        // Status messages
        statusMessage = new Label("Please select number of spots to begin.");
        statusMessage.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statusMessage.setTextFill(Color.web(GOLD));

        selectedSpotsLabel = new Label("Selected: 0/0");
        selectedSpotsLabel.setFont(Font.font("Arial", 12));
        selectedSpotsLabel.setTextFill(Color.web(WHITE));

        drawingProgressLabel = new Label("");
        drawingProgressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        drawingProgressLabel.setTextFill(Color.web(GOLD));

        // Combine controls
        VBox controls = new VBox(12); // Increased spacing
        controls.setPadding(new Insets(20));
        controls.setAlignment(Pos.CENTER); // Center everything
        controls.setStyle("-fx-background-color: " + DARK_PURPLE + "; " +
                "-fx-border-color: " + BRIGHT_PURPLE + "; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8;");
        controls.getChildren().addAll(
                spotsBox, drawingsBox, actionButtons, statusMessage, selectedSpotsLabel, drawingProgressLabel
        );

        // Make control panel take full width
        controlPanel = new HBox(controls);
        controlPanel.setAlignment(Pos.CENTER);
        HBox.setHgrow(controls, Priority.ALWAYS);
    }

    private ToggleButton createToggleButton(String text) {
        ToggleButton button = new ToggleButton(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        button.setStyle("-fx-background-color: " + DARK_PURPLE + "; " +
                "-fx-text-fill: " + WHITE + "; " +
                "-fx-border-color: " + BRIGHT_PURPLE + "; " +
                "-fx-border-width: 2; " +
                "-fx-padding: 5px 10px;");

        // Change style when selected
        button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                button.setStyle("-fx-background-color: " + PINK + "; " +
                        "-fx-text-fill: " + WHITE + "; " +
                        "-fx-border-color: " + GOLD + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-padding: 5px 10px;");
            } else {
                button.setStyle("-fx-background-color: " + DARK_PURPLE + "; " +
                        "-fx-text-fill: " + WHITE + "; " +
                        "-fx-border-color: " + BRIGHT_PURPLE + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-padding: 5px 10px;");
            }
        });

        return button;
    }

    private Button createActionButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        button.setStyle("-fx-background-color: " + PINK + "; " +
                "-fx-text-fill: " + WHITE + "; " +
                "-fx-padding: 8px 15px;");

        // Style for disabled state
        button.disabledProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                button.setStyle("-fx-background-color: #666666; " +
                        "-fx-text-fill: #999999; " +
                        "-fx-padding: 8px 15px;");
            } else {
                button.setStyle("-fx-background-color: " + PINK + "; " +
                        "-fx-text-fill: " + WHITE + "; " +
                        "-fx-padding: 8px 15px;");
            }
        });

        return button;
    }

    private void createResultsPanel() {
        Label resultsTitle = new Label("Drawing Results");
        resultsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resultsTitle.setTextFill(Color.web(GOLD));
        resultsTitle.setAlignment(Pos.CENTER);

        drawnNumbersDisplay = new Label("Drawn Numbers: ");
        drawnNumbersDisplay.setFont(Font.font("Arial", 12));
        drawnNumbersDisplay.setTextFill(Color.web(WHITE));
        drawnNumbersDisplay.setWrapText(true);
        drawnNumbersDisplay.setPrefWidth(220);
        drawnNumbersDisplay.setAlignment(Pos.TOP_LEFT);

        matchesDisplay = new Label("Matches: ");
        matchesDisplay.setFont(Font.font("Arial", 12));
        matchesDisplay.setTextFill(Color.web(WHITE));
        matchesDisplay.setWrapText(true);
        matchesDisplay.setAlignment(Pos.TOP_LEFT);

        winsDisplay = new Label("This Drawing: $0.00");
        winsDisplay.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        winsDisplay.setTextFill(Color.web(GOLD));
        winsDisplay.setAlignment(Pos.CENTER);

        totalWinsDisplay = new Label("Total Won: $0.00");
        totalWinsDisplay.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalWinsDisplay.setTextFill(Color.web(GOLD));
        totalWinsDisplay.setAlignment(Pos.CENTER);

        VBox resultsContent = new VBox(10, resultsTitle, drawnNumbersDisplay, matchesDisplay, winsDisplay, totalWinsDisplay);
        resultsContent.setPadding(new Insets(15));
        resultsContent.setAlignment(Pos.TOP_CENTER);

        resultsPanel = new VBox(resultsContent);
        resultsPanel.setStyle("-fx-background-color: " + DARK_PURPLE + "; " +
                "-fx-border-color: " + BRIGHT_PURPLE + "; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8;");
        resultsPanel.setPrefWidth(250);
        resultsPanel.setAlignment(Pos.TOP_CENTER);
    }

    private void setupLayout() {
        // Main content area - use BorderPane for better layout control
        BorderPane mainContent = new BorderPane();
        mainContent.setPadding(new Insets(10));

        // Left: Bet Card
        VBox betCardContainer = new VBox();
        betCardContainer.setAlignment(Pos.TOP_CENTER);
        betCardContainer.setPadding(new Insets(10));
        betCardContainer.getChildren().add(betCard.getGridPane());

        // Right: Results Panel
        VBox resultsContainer = new VBox();
        resultsContainer.setAlignment(Pos.TOP_CENTER);
        resultsContainer.setPadding(new Insets(10));
        resultsContainer.getChildren().add(resultsPanel);

        // Center the main content
        HBox centerContent = new HBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.getChildren().addAll(betCardContainer, resultsContainer);
        mainContent.setCenter(centerContent);

        // Top: Control Panel
        VBox topContainer = new VBox();
        topContainer.setAlignment(Pos.CENTER);
        topContainer.getChildren().add(controlPanel);
        mainContent.setTop(topContainer);

        // Root layout
        rootLayout = new VBox();
        rootLayout.setStyle("-fx-background-color: " + DARK_PURPLE + ";");
        rootLayout.getChildren().addAll(menuBar, mainContent);
    }

    //game logic
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
        gameState.setPlayerNumbers(betCard.getSelectedNumbers());
        updateStatusMessage("Auto-pick completed! " + betCard.getSelectedCount() + "/" + gameState.getPlayerSpots() + " numbers selected.");
        validateStartConditions();
    }

    private void handleStartDrawing() {
        if (drawingInProgress) return;

        ToggleButton selectedDrawing = (ToggleButton) drawingsToggleGroup.getSelectedToggle();
        int drawingsCount = Integer.parseInt(selectedDrawing.getText());

        gameState.startNewDrawingSession(drawingsCount);
        gameState.setPlayerNumbers(betCard.getSelectedNumbers());

        setControlsDisabled(true);
        drawingInProgress = true;
        startNextDrawing();
    }

    private void startNextDrawing() {
        drawingProgressLabel.setText("Drawing " + gameState.getCurrentDrawingNumber() + " of " + gameState.getTotalDrawings());
        drawnNumbersDisplay.setText("Drawn Numbers: Drawing...");
        matchesDisplay.setText("Matches: ");
        winsDisplay.setText("This Drawing: $0.00");
        animateDrawing();
    }

    private void animateDrawing() {
        Set<Integer> drawnNumbers = gameState.runDrawing();
        List<Integer> drawnList = new ArrayList<>(drawnNumbers);
        Collections.shuffle(drawnList);

        new Thread(() -> {
            StringBuilder displayedNumbers = new StringBuilder("Drawn Numbers: ");

            for (int i = 0; i < drawnList.size(); i++) {
                int number = drawnList.get(i);
                final String currentDisplay = displayedNumbers.toString() + number;

                Platform.runLater(() -> {
                    drawnNumbersDisplay.setText(currentDisplay);
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (i < drawnList.size() - 1) {
                    displayedNumbers.append(number).append(", ");
                }
            }

            Platform.runLater(() -> {
                showDrawingResults();
            });
        }).start();
    }

    private void showDrawingResults() {
        Set<Integer> matches = gameState.getMatches();
        double winnings = gameState.calculateWinnings(matches.size());

        betCard.highlightMatches(gameState.getCurrentDrawnNumbers());

        matchesDisplay.setText("Matches: " + matches.size() + " (" + matches + ")");
        winsDisplay.setText("This Drawing: $" + String.format("%.2f", winnings));
        totalWinsDisplay.setText("Total Won: $" + String.format("%.2f", gameState.getTotalWinnings()));

        if (matches.size() > 0) {
            updateStatusMessage("Congratulations! You matched " + matches.size() + " numbers and won $" + String.format("%.2f", winnings) + "!");
        } else {
            updateStatusMessage("No matches this drawing. Better luck next time!");
        }

        if (gameState.hasMoreDrawings()) {
            nextDrawingButton.setDisable(false);
            drawingProgressLabel.setText("Ready for next drawing. Click 'Next Drawing'.");
        } else {
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
        Stage rulesWindow = new Stage();
        rulesWindow.setTitle("Keno Rules");

        Label header = new Label("How to Play Keno");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        header.setTextFill(Color.web(GOLD));

        TextArea rulesText = new TextArea(
                "1. Choose how many spots to play (1, 4, 8, or 10 numbers)\n" +
                        "2. Select your numbers or use Auto-pick\n" +
                        "3. Choose how many drawings to play (1-4)\n" +
                        "4. Watch as 20 numbers are drawn\n" +
                        "5. Win based on how many numbers you match\n"
        );
        rulesText.setWrapText(true);
        rulesText.setEditable(false);
        rulesText.setStyle("-fx-control-inner-background: " + DARK_PURPLE + ";" +
                "-fx-text-fill: white; -fx-font-size: 14px;");

        Button closeButton = new Button("Close");
        closeButton.setStyle(
                "-fx-background-color: " + PINK + ";" +
                        "-fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;"
        );
        closeButton.setOnAction(e -> rulesWindow.close());

        VBox layout = new VBox(15, header, rulesText, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle(
                "-fx-background-color: " + BRIGHT_PURPLE + ";" +
                        "-fx-border-color: " + GOLD + "; -fx-border-width: 4px;"
        );

        Scene scene = new Scene(layout, 450, 350);
        rulesWindow.setScene(scene);
        rulesWindow.show(); // Floating window
    }

    private void showOdds() {
        Stage oddsWindow = new Stage();
        oddsWindow.setTitle("Keno Odds & Payouts");

        Label header = new Label("Keno Winning Odds & Payouts");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        header.setTextFill(Color.web(GOLD));

        TextArea oddsText = new TextArea(
                "SPOT 1:\n" +
                        "  Match 1 → $2\n\n" +

                        "SPOT 4:\n" +
                        "  Match 2 → $1\n" +
                        "  Match 3 → $5\n" +
                        "  Match 4 → $75\n\n" +

                        "SPOT 8:\n" +
                        "  Match 4 → $2\n" +
                        "  Match 5 → $12\n" +
                        "  Match 6 → $50\n" +
                        "  Match 7 → $750\n" +
                        "  Match 8 → $10,000\n\n" +

                        "SPOT 10:\n" +
                        "  Match 0 → $5\n" +
                        "  Match 5 → $2\n" +
                        "  Match 6 → $15\n" +
                        "  Match 7 → $100\n" +
                        "  Match 8 → $500\n" +
                        "  Match 9 → $5,000\n" +
                        "  Match 10 → $25,000"
        );
        oddsText.setWrapText(true);
        oddsText.setEditable(false);
        oddsText.setStyle(
                "-fx-control-inner-background: " + DARK_PURPLE + ";" +
                        "-fx-text-fill: white; -fx-font-size: 14px;"
        );

        Button closeButton = new Button("Close");
        closeButton.setStyle(
                "-fx-background-color: " + PINK + ";" +
                        "-fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;"
        );
        closeButton.setOnAction(e -> oddsWindow.close());

        VBox layout = new VBox(15, header, oddsText, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle(
                "-fx-background-color: " + BRIGHT_PURPLE + ";" +
                        "-fx-border-color: " + GOLD + "; -fx-border-width: 4px;"
        );

        Scene scene = new Scene(layout, 450, 450);
        oddsWindow.setScene(scene);
        oddsWindow.show();
    }


    private Integer currentColorScheme = 0;

    private void applyNewLook() {
        if (currentColorScheme == null) {
            currentColorScheme = 0;
        }

        currentColorScheme = (currentColorScheme + 1) % 4; // Cycle through 4 schemes

        switch (currentColorScheme) {
            case 0: // Original Purple
                setBackgroundColors(DARK_PURPLE, BRIGHT_PURPLE);
                break;
            case 1: // Blue
                setBackgroundColors("#1a1a2e", "#0f3460");
                break;
            case 2: // Green
                setBackgroundColors("#1b4332", "#2d6a4f");
                break;
            case 3: // Warm
                setBackgroundColors("#3d348b", "#7678ed");
                break;
        }
    }

    private void setBackgroundColors(String bgColor, String borderColor) {
        // Root background
        rootLayout.setStyle("-fx-background-color: " + bgColor + ";");

        // Control Panel background
        if (controlPanel.getChildren().size() > 0) {
            VBox controls = (VBox) controlPanel.getChildren().get(0);
            controls.setStyle("-fx-background-color: " + bgColor + "; " +
                    "-fx-border-color: " + borderColor + "; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 8;");
        }

        // Results Panel background
        resultsPanel.setStyle("-fx-background-color: " + bgColor + "; " +
                "-fx-border-color: " + borderColor + "; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8;");
    }

    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(rootLayout, 1000, 700);
        }
        return scene;
    }
}