// KenoGame.java - Main Application Class
import javafx.application.Application;
import javafx.stage.Stage;

public class KenoGame extends Application {
    private Stage primaryStage;
    private GameState gameState;
    private WelcomeScene welcomeScene;
    private GamePlayScene gamePlayScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.gameState = new GameState();

        initializeScenes();
        switchToWelcomeScene();

        primaryStage.setTitle("Keno Lottery Game");
        primaryStage.show();
    }

    private void initializeScenes() {
        welcomeScene = new WelcomeScene(this, gameState);
        gamePlayScene = new GamePlayScene(this, gameState);
    }

    public void switchToWelcomeScene() {
        primaryStage.setScene(welcomeScene.getScene());
    }

    public void switchToGamePlayScene() {
        primaryStage.setScene(gamePlayScene.getScene());
    }
}