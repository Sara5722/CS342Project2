// BetCard.java
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.util.*;

public class BetCard {
    private GridPane gridPane;
    private int maxSpots;
    private Set<Integer> selectedNumbers;
    private List<Button> numberButtons;
    private boolean selectionEnabled;

    public BetCard() {
        this.gridPane = new GridPane();
        this.selectedNumbers = new HashSet<>();
        this.numberButtons = new ArrayList<>();
        this.selectionEnabled = false;
        this.maxSpots = 0;

        initializeGrid();
    }

    private void initializeGrid() {
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new javafx.geometry.Insets(10));

        // Create 8x10 grid of buttons (numbers 1-80)
        int number = 1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                Button button = createNumberButton(number);
                numberButtons.add(button);
                gridPane.add(button, col, row);
                number++;
            }
        }
    }

    private Button createNumberButton(int number) {
        Button button = new Button(String.valueOf(number));
        button.setPrefSize(60, 40);
        button.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Initial state - disabled and gray
        button.setDisable(true);
        button.setStyle("-fx-background-color: lightgray; -fx-font-size: 14px; -fx-font-weight: bold;");

        button.setOnAction(e -> handleNumberClick(number, button));

        return button;
    }

    private void handleNumberClick(int number, Button button) {
        if (!selectionEnabled) return;

        if (selectedNumbers.contains(number)) {
            // Deselect number
            selectedNumbers.remove(number);
            button.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px; -fx-font-weight: bold;");
        } else {
            // Select number if we haven't reached max spots
            if (selectedNumbers.size() < maxSpots) {
                selectedNumbers.add(number);
                button.setStyle("-fx-background-color: gold; -fx-font-size: 14px; -fx-font-weight: bold;");
            }
        }

        updateButtonStates();
    }

    public void enableSelection(int spots) {
        this.maxSpots = spots;
        this.selectionEnabled = true;
        this.selectedNumbers.clear();

        // Enable all buttons
        for (Button button : numberButtons) {
            button.setDisable(false);
            button.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px; -fx-font-weight: bold;");
        }

        resetAllButtons();
    }

    public void disableSelection() {
        this.selectionEnabled = false;
        for (Button button : numberButtons) {
            button.setDisable(true);
        }
    }

    public void quickPick() {
        if (!selectionEnabled || maxSpots == 0) return;

        selectedNumbers.clear();
        resetAllButtons();

        // Generate random unique numbers
        List<Integer> allNumbers = new ArrayList<>();
        for (int i = 1; i <= 80; i++) {
            allNumbers.add(i);
        }
        Collections.shuffle(allNumbers);

        // Select first 'maxSpots' numbers
        for (int i = 0; i < maxSpots; i++) {
            int number = allNumbers.get(i);
            selectedNumbers.add(number);
            // Find and highlight the corresponding button
            for (Button button : numberButtons) {
                if (Integer.parseInt(button.getText()) == number) {
                    button.setStyle("-fx-background-color: gold; -fx-font-size: 14px; -fx-font-weight: bold;");
                    break;
                }
            }
        }

        updateButtonStates();
    }

    public void reset() {
        selectedNumbers.clear();
        resetAllButtons();
        updateButtonStates();
    }

    private void resetAllButtons() {
        for (Button button : numberButtons) {
            if (selectionEnabled) {
                button.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px; -fx-font-weight: bold;");
            } else {
                button.setStyle("-fx-background-color: lightgray; -fx-font-size: 14px; -fx-font-weight: bold;");
            }
        }
    }

    private void updateButtonStates() {
        // If we've reached max spots, disable unselected buttons
        if (selectedNumbers.size() >= maxSpots) {
            for (Button button : numberButtons) {
                int buttonNumber = Integer.parseInt(button.getText());
                if (!selectedNumbers.contains(buttonNumber)) {
                    button.setDisable(true);
                }
            }
        } else {
            // Enable all buttons if we haven't reached max
            for (Button button : numberButtons) {
                button.setDisable(false);
            }
        }
    }

    // Add this method to BetCard.java
    public void highlightMatches(Set<Integer> drawnNumbers) {
        for (Button button : numberButtons) {
            int buttonNumber = Integer.parseInt(button.getText());
            if (selectedNumbers.contains(buttonNumber) && drawnNumbers.contains(buttonNumber)) {
                // This number was selected AND drawn - highlight as match
                button.setStyle("-fx-background-color: limegreen; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
            } else if (selectedNumbers.contains(buttonNumber)) {
                // This number was selected but NOT drawn
                button.setStyle("-fx-background-color: gold; -fx-font-size: 14px; -fx-font-weight: bold;");
            } else if (drawnNumbers.contains(buttonNumber)) {
                // This number was drawn but NOT selected
                button.setStyle("-fx-background-color: orange; -fx-font-size: 14px; -fx-font-weight: bold;");
            } else {
                // Neither selected nor drawn
                button.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px; -fx-font-weight: bold;");
            }
        }
    }

    // Getters
    public GridPane getGridPane() { return gridPane; }
    public Set<Integer> getSelectedNumbers() { return new HashSet<>(selectedNumbers); }
    public boolean isSelectionValid() { return selectedNumbers.size() == maxSpots; }
    public int getSelectedCount() { return selectedNumbers.size(); }
    public int getMaxSpots() { return maxSpots; }
}