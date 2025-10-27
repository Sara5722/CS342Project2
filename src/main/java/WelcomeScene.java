// WelcomeScene.java
// Author: Sara Alaidroos, salai3, salai3@uic.edu
// Author: Teresa Chirayil, tchir3, tchir3@uic.edu
//
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// WelcomeScene creates and manages the welcome/splash screen for the Keno game.
// This is the first screen users see when launching the application.
// It provides game information, navigation options, and displays total winnings.

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
        Menu mainMenu = new Menu("Menu");
        mainMenu.setStyle("-fx-background-color:" + GOLD + ";");

        // Game actions
        MenuItem startMenuItem = new MenuItem("Start Game");
        MenuItem exitMenuItem = new MenuItem("Exit");

        // Info actions
        MenuItem rulesMenuItem = new MenuItem("Rules");
        MenuItem oddsMenuItem = new MenuItem("Odds");

        // Add actions
        startMenuItem.setOnAction(e -> switchToGamePlay());
        exitMenuItem.setOnAction(e -> System.exit(0));
        rulesMenuItem.setOnAction(e -> showRules());
        oddsMenuItem.setOnAction(e -> showOdds());

        // Organize visually
        mainMenu.getItems().addAll(
                startMenuItem,
                new SeparatorMenuItem(),
                rulesMenuItem,
                oddsMenuItem,
                new SeparatorMenuItem(),
                exitMenuItem
        );

        // Create menu bar
        menuBar = new MenuBar(mainMenu);

        // Style menu bar
        menuBar.setStyle(
                "-fx-background-color: " + DARK_PURPLE + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 5px;" +
                        "-fx-selection-bar: " + BRIGHT_PURPLE + ";" +
                        "-fx-text-fill: white;"
        );
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
        // Title box – adds spacing below title
        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10, 0, 10, 0));

        // Message area container – adds padding
        VBox messageBox = new VBox(messageArea);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(10, 20, 10, 20));

        // Button box – centers and adds spacing
        VBox buttonBox = new VBox(startButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 10, 0));

        // Wins label box
        VBox winsBox = new VBox(totalWinsLabel);
        winsBox.setAlignment(Pos.CENTER);
        winsBox.setPadding(new Insets(5, 0, 10, 0));

        // Main content frame
        VBox contentBox = new VBox(20); // spacing between elements
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));
        contentBox.setStyle(
                "-fx-background-color: " + DARK_PURPLE + ";" +
                        "-fx-border-color: " + BRIGHT_PURPLE + ";" +
                        "-fx-border-width: 5;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;"
        );
        contentBox.setMaxWidth(600);

        contentBox.getChildren().addAll(titleBox, messageBox, buttonBox, winsBox);

        // Root layout
        rootLayout = new VBox(10);
        rootLayout.setStyle("-fx-background-color: " + DARK_PURPLE + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);
        rootLayout.setPadding(new Insets(10, 0, 0, 0));
        rootLayout.getChildren().addAll(menuBar, contentBox);
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