// WelcomeScene.java
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;

public class WelcomeScene {
    private Scene scene;
    private KenoGame mainApp;
    private GameState gameState;

    private MenuBar menuBar;
    private Button startButton;
    private Label messageArea;
    private Label totalWinsLabel;
    private Label titleLabel;
    private VBox rootLayout;

    //color palette
    private final String DARK_PURPLE = "#450693";
    private final String BRIGHT_PURPLE = "#8C00FF";
    private final String PINK = "#FF3F7F";
    private final String GOLD = "#FFC400";
    private final String WHITE = "#FFFFFF";
    private final String BLACK = "#000000";

    public WelcomeScene(KenoGame mainApp, GameState gameState) {
        this.mainApp = mainApp;
        this.gameState = gameState;
        initialize();
    }

    private void initialize() {
        createMenuBar();
        createContent();
        setupLayout();
    }

    private void createMenuBar() {
        Menu menu = new Menu("Menu");
        menu.setStyle("-fx-background-color: " +GOLD + ";");
        MenuItem rulesMenuItem = new MenuItem("Rules");
        MenuItem oddsMenuItem = new MenuItem("Odds");
        MenuItem exitMenuItem = new MenuItem("Exit");

        rulesMenuItem.setOnAction(e -> showRules());
        oddsMenuItem.setOnAction(e -> showOdds());
        exitMenuItem.setOnAction(e -> System.exit(0));

        menu.getItems().addAll(rulesMenuItem, oddsMenuItem, exitMenuItem);
        menuBar = new MenuBar(menu);

        // Style the menu bar
        menuBar.setStyle("-fx-background-color: " + DARK_PURPLE + ";");
    }

    private void createContent() {
        // Title with your colors and Arial Bold
        titleLabel = new Label("KENO LOTTERY");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.web(GOLD));
        titleLabel.setStyle("-fx-effect: dropshadow(one-pass-box, " + BLACK + ", 2, 0, 1, 1);");

        // Welcome message
        messageArea = new Label("Welcome to Keno!\n\n" +
                "Select your spots (1, 4, 8, or 10), choose your numbers,\n" +
                "and watch the drawings. Match numbers to win!\n\n" +
                "Use Auto-pick for quick selection or choose your own lucky numbers!");
        messageArea.setWrapText(true);
        messageArea.setTextAlignment(TextAlignment.CENTER);
        messageArea.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        messageArea.setTextFill(Color.web(WHITE));

        // Start button
        startButton = new Button("START PLAYING");
        startButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        startButton.setStyle("-fx-padding: 12px 25px; " +
                "-fx-background-color: " + PINK + "; " +
                "-fx-text-fill: " + WHITE + "; " +
                "-fx-background-radius: 8;");
        startButton.setOnAction(e -> switchToGamePlay());

        // Hover effects for button
        startButton.setOnMouseEntered(e -> {
            startButton.setStyle("-fx-padding: 12px 25px; " +
                    "-fx-background-color: " + BRIGHT_PURPLE + "; " +
                    "-fx-text-fill: " + WHITE + "; " +
                    "-fx-background-radius: 8;");
        });

        startButton.setOnMouseExited(e -> {
            startButton.setStyle("-fx-padding: 12px 25px; " +
                    "-fx-background-color: " + PINK + "; " +
                    "-fx-text-fill: " + WHITE + "; " +
                    "-fx-background-radius: 8;");
        });

        // Total wins label
        totalWinsLabel = new Label("Total Won: $0.00");
        totalWinsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        totalWinsLabel.setTextFill(Color.web(GOLD));
    }

    private void setupLayout() {
        // Main content container
        VBox contentBox = new VBox(40);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(50));
        contentBox.setStyle("-fx-background-color: " + DARK_PURPLE + "; " +
                "-fx-border-color: " + BRIGHT_PURPLE + "; " +
                "-fx-border-width: 5; " +
                "-fx-border-radius:45; " +
                "-fx-background-radius: 10;");
        contentBox.setMaxWidth(500);
        contentBox.getChildren().addAll(titleLabel, messageArea, startButton, totalWinsLabel);

        // Main layout - simple and centered
        rootLayout = new VBox();
        rootLayout.setStyle("-fx-background-color: " + DARK_PURPLE + ";");
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setPadding(new Insets(20));
        rootLayout.getChildren().addAll(menuBar, contentBox);
    }

    private void showRules() {
        Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
        rulesAlert.setTitle("Keno Rules");
        rulesAlert.setHeaderText("How to Play Keno");
        rulesAlert.setContentText("1. Choose how many spots to play (1, 4, 8, or 10 numbers)\n" +
                "2. Select your numbers on the bet card or use Auto-pick\n" +
                "3. Choose how many drawings to play (1-4)\n" +
                "4. Watch as 20 numbers are drawn\n" +
                "5. Win based on how many numbers you match!\n\n" +
                "Payouts based on North Carolina State Lottery rules.");

        // Style the alert dialog with your colors
        DialogPane dialogPane = rulesAlert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + BRIGHT_PURPLE + "; " +
                "-fx-text-fill: " + WHITE + ";");
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

        // Style the alert dialog with your colors
        DialogPane dialogPane = oddsAlert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + BRIGHT_PURPLE + "; " +
                "-fx-text-fill: " + WHITE + ";");
        oddsAlert.showAndWait();
    }

    private void switchToGamePlay() {
        mainApp.switchToGamePlayScene();
    }

    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(rootLayout, 800, 600);
        }
        return scene;
    }

    public void updateTotalWins() {
        totalWinsLabel.setText(String.format("Total Won: $%.2f", gameState.getTotalWinnings()));
    }
}