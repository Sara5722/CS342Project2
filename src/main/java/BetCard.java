// BetCard.java
// Author: Sara Alaidroos, salai3, salai3@uic.edu
// Author: Teresa Chirayil, tchir3, tchir3@uic.edu
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.util.*;

//BetCard represents a Keno betting card with 80 numbers (1-80) arranged in an 8x10 grid.
//Users can select a specified number of spots (numbers) for their bet, and the card
//provides visual feedback for selections, matches, and game state.

public class BetCard {
    // UI container for the number grid
    private GridPane gridPane;

    // Maximum number of spots the user can select for this bet
    private int maxSpots;

    // Set of currently selected numbers (prevents duplicates)
    private Set<Integer> selectedNumbers;

    // List of all 80 button references for easy access and styling
    private List<Button> numberButtons;

    // Flag indicating whether user can currently select numbers
    private boolean selectionEnabled;

    //Constructor initializes the betting card with default values and creates the grid.
    public BetCard() {
        this.gridPane = new GridPane();
        this.selectedNumbers = new HashSet<>();
        this.numberButtons = new ArrayList<>();
        this.selectionEnabled = false;
        this.maxSpots = 0;

        initializeGrid();
    }

    //Creates the 8x10 grid of number buttons (1-80) with spacing and padding.
     //All buttons are initially disabled until a bet amount is selected.

    private void initializeGrid() {
        // Set spacing between buttons
        gridPane.setHgap(5);  // Horizontal gap
        gridPane.setVgap(5);  // Vertical gap
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

    //Creates a single number button with the specified number.
     //param number The number to display on the button (1-80)
     //return Configured Button object with event handler

    private Button createNumberButton(int number) {
        Button button = new Button(String.valueOf(number));
        button.setPrefSize(60, 40);
        button.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Initial state - disabled and gray (no bet placed yet)
        button.setDisable(true);
        button.setStyle("-fx-background-color: lightgray; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Attach click event handler
        button.setOnAction(e -> handleNumberClick(number, button));

        return button;
    }


     //Handles click events on number buttons.
     //toggles selection state if selection is enabled and within max spots limit.
     //number The number that was clicked

    private void handleNumberClick(int number, Button button) {
        // Ignore clicks if selection is not enabled
        if (!selectionEnabled) return;

        if (selectedNumbers.contains(number)) {
            // Deselect number - change back to light blue
            selectedNumbers.remove(number);
            button.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px; -fx-font-weight: bold;");
        } else {
            // Select number if we haven't reached max spots limit
            if (selectedNumbers.size() < maxSpots) {
                selectedNumbers.add(number);
                button.setStyle("-fx-background-color: gold; -fx-font-size: 14px; -fx-font-weight: bold;");
            }
        }

        // Update button states based on whether max spots reached
        updateButtonStates();
    }

    //Enables number selection with a specified maximum number of spots.
     //Called when user selects how many numbers they want to pick.
    public void enableSelection(int spots) {
        this.maxSpots = spots;
        this.selectionEnabled = true;
        this.selectedNumbers.clear();

        // Enable all buttons and set to light blue (ready for selection)
        for (Button button : numberButtons) {
            button.setDisable(false);
            button.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px; -fx-font-weight: bold;");
        }

        resetAllButtons();
    }

    //Disables number selection (e.g., after drawing or during results display).
     //Prevents user from modifying their selection.

    public void disableSelection() {
        this.selectionEnabled = false;
        for (Button button : numberButtons) {
            button.setDisable(true);
        }
    }

    //Automatically selects random numbers for the user (Quick Pick feature).
     //Selects exactly 'maxSpots' unique random numbers from 1-80.

    public void quickPick() {
        // Only allow quick pick if selection is enabled and spots are set
        if (!selectionEnabled || maxSpots == 0) return;

        // Clear any existing selections
        selectedNumbers.clear();
        resetAllButtons();

        // Generate random unique numbers
        List<Integer> allNumbers = new ArrayList<>();
        for (int i = 1; i <= 80; i++) {
            allNumbers.add(i);
        }
        Collections.shuffle(allNumbers);  // Randomize the order

        // Select first 'maxSpots' numbers from shuffled list
        for (int i = 0; i < maxSpots; i++) {
            int number = allNumbers.get(i);
            selectedNumbers.add(number);

            // Find and highlight the corresponding button in gold
            for (Button button : numberButtons) {
                if (Integer.parseInt(button.getText()) == number) {
                    button.setStyle("-fx-background-color: gold; -fx-font-size: 14px; -fx-font-weight: bold;");
                    break;
                }
            }
        }

        updateButtonStates();
    }

    //Resets the card by clearing all selections.
    //Keeps selection enabled if it was already enabled.

    public void reset() {
        selectedNumbers.clear();
        resetAllButtons();
        updateButtonStates();
    }

    //Resets all buttons to their default state based on whether selection is enabled.
     //Light blue if enabled, light gray if disabled.

    private void resetAllButtons() {
        for (Button button : numberButtons) {
            if (selectionEnabled) {
                button.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px; -fx-font-weight: bold;");
            } else {
                button.setStyle("-fx-background-color: lightgray; -fx-font-size: 14px; -fx-font-weight: bold;");
            }
        }
    }

    //Updates button states based on current selection count.
     //If max spots reached, disables unselected buttons to prevent over-selection.

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

    //Highlights matches between selected numbers and drawn numbers after a draw.
     //Color coding:
     //Lime green: Numbers that were selected AND drawn (winning matches)
     // Gold: Numbers that were selected but NOT drawn
     // Orange: Numbers that were drawn but NOT selected
     // Light blue: Numbers that were neither selected nor drawn

    public void highlightMatches(Set<Integer> drawnNumbers) {
        for (Button button : numberButtons) {
            int buttonNumber = Integer.parseInt(button.getText());

            if (selectedNumbers.contains(buttonNumber) && drawnNumbers.contains(buttonNumber)) {
                // This number was selected AND drawn - highlight as match (WIN)
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

    //@return The GridPane containing all number buttons

    public GridPane getGridPane() {
        return gridPane;
    }

    //@return A copy of the set of currently selected numbers

    public Set<Integer> getSelectedNumbers() {
        return new HashSet<>(selectedNumbers);
    }

    //@return true if the user has selected exactly the required number of spots

    public boolean isSelectionValid() {
        return selectedNumbers.size() == maxSpots;
    }

    //@return The current count of selected numbers

    public int getSelectedCount() {
        return selectedNumbers.size();
    }

    //@return The maximum number of spots allowed for selection

    public int getMaxSpots() {
        return maxSpots;
    }
}