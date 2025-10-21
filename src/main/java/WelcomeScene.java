// WelcomeScene.java
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class WelcomeScene {
    private Scene scene;
    private KenoGame mainApp;
    private GameState gameState;

    private MenuBar menuBar;
    private Button startButton;
    private Label messageArea;
    private Label totalWinsLabel;
    private VBox rootLayout;

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

        MenuItem rulesMenuItem = new MenuItem("Rules");
        MenuItem oddsMenuItem = new MenuItem("Odds");
        MenuItem exitMenuItem = new MenuItem("Exit");

        rulesMenuItem.setOnAction(e -> showRules());
        oddsMenuItem.setOnAction(e -> showOdds());
        exitMenuItem.setOnAction(e -> System.exit(0));

        menu.getItems().addAll(rulesMenuItem, oddsMenuItem, exitMenuItem);
        menuBar = new MenuBar(menu);
    }

    private void createContent() {
        startButton = new Button("START PLAYING");
        startButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        startButton.setOnAction(e -> switchToGamePlay());

        messageArea = new Label("Welcome to Keno!\n\nSelect your spots (1, 4, 8, or 10), choose your numbers, " +
                "and watch the drawings. Match numbers to win! Use Auto-pick for quick selection.");
        messageArea.setWrapText(true);
        messageArea.setTextAlignment(TextAlignment.CENTER);
        messageArea.setFont(new Font(14));

        totalWinsLabel = new Label("Total Won: $0.00");
        totalWinsLabel.setFont(new Font(16));
    }

    private void setupLayout() {
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(messageArea, startButton, totalWinsLabel);

        rootLayout = new VBox();
        rootLayout.getChildren().addAll(menuBar, contentBox);
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        scene = new Scene(rootLayout, 800, 600);
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

    private void switchToGamePlay() {
        mainApp.switchToGamePlayScene();
    }

    public Scene getScene() {
        return scene;
    }

    public void updateTotalWins() {
        totalWinsLabel.setText(String.format("Total Won: $%.2f", gameState.getTotalWinnings()));
    }
}