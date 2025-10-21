// GamePlayScene.java - Temporary Stub
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
    private VBox rootLayout;

    public GamePlayScene(KenoGame mainApp, GameState gameState) {
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
        MenuItem newLookMenuItem = new MenuItem("New Look");
        MenuItem exitMenuItem = new MenuItem("Exit");

        rulesMenuItem.setOnAction(e -> showRules());
        oddsMenuItem.setOnAction(e -> showOdds());
        newLookMenuItem.setOnAction(e -> applyNewLook());
        exitMenuItem.setOnAction(e -> System.exit(0));

        menu.getItems().addAll(rulesMenuItem, oddsMenuItem, newLookMenuItem, exitMenuItem);
        menuBar = new MenuBar(menu);
    }

    private void createContent() {
        Label placeholder = new Label("Game Play Scene - Under Construction\n\n" +
                "This is where the bet card, spot selection, and drawing will be implemented.\n\n" +
                "← Use the Menu to go back to Welcome Screen");
        placeholder.setFont(new Font(16));
        placeholder.setWrapText(true);
        placeholder.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back to Welcome Screen");
        backButton.setOnAction(e -> mainApp.switchToWelcomeScene());

        VBox contentBox = new VBox(20, placeholder, backButton);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));

        rootLayout = new VBox(menuBar, contentBox);
    }

    private void setupLayout() {
        scene = new Scene(rootLayout, 800, 600);
    }

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
        // Temporary New Look implementation
        rootLayout.setStyle("-fx-background-color: lightblue;");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New Look");
        alert.setHeaderText("New Look Applied!");
        alert.setContentText("This is a temporary implementation. Full New Look functionality coming soon.");
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}