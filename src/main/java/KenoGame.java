// KenoGame.java - Main Application Class
// Author: Sara Alaidroos, salai3, salai3@uic.edu
// Author: Teresa Chirayil, tchir3, tchir3@uic.edu
//
import javafx.application.Application;
import javafx.stage.Stage;

// KenoGame is the main application class for the Keno Lottery Game.
// It extends JavaFX Application and manages the primary window (Stage),
// game state, and scene transitions between welcome and gameplay screens.


public class KenoGame extends Application {
    // Primary window/stage for the application
    private Stage primaryStage;

    // Shared game state object passed to all scenes
    private GameState gameState;

    // Welcome/splash screen scene
    private WelcomeScene welcomeScene;

    // Main gameplay screen scene
    private GamePlayScene gamePlayScene;

    // Main entry point for the Java application.
    // Calls JavaFX launch() which initializes the JavaFX runtime and calls start().
    public static void main(String[] args) {
        launch(args);
    }


    // This is where the primary stage is configured and the UI is initialized.
    @Override
    public void start(Stage primaryStage) {
        // Store reference to primary stage for scene switching
        this.primaryStage = primaryStage;

        // Create shared game state object
        this.gameState = new GameState();

        // Initialize both scenes (welcome and gameplay)
        initializeScenes();

        // Start by showing the welcome screen
        switchToWelcomeScene();

        // Set window title
        primaryStage.setTitle("Keno Lottery Game");

        // Display the window
        primaryStage.show();
    }

    // Initializes both the welcome scene and gameplay scene.
    // Both scenes receive references to this main app and the shared game state,
    // allowing them to trigger scene transitions and access/modify game data.
    private void initializeScenes() {
        welcomeScene = new WelcomeScene(this, gameState);
        gamePlayScene = new GamePlayScene(this, gameState);
    }

    // Switches the primary stage to display the welcome scene.
    // Called at application start and when returning from gameplay.
    public void switchToWelcomeScene() {
        primaryStage.setScene(welcomeScene.getScene());
    }

    // Switches the primary stage to display the gameplay scene.
    // Called when user clicks "Start Game" or similar button from welcome screen.
    public void switchToGamePlayScene() {
        primaryStage.setScene(gamePlayScene.getScene());
    }
}