// GameState.java - Updated with Drawing Logic
// Author: Sara Alaidroos, salai3, salai3@uic.edu
// Author: Teresa Chirayil, tchir3, tchir3@uic.edu

import java.util.*;

//GameState manages all state data for a Keno game session.
//Handles drawing logic, match calculations, winnings computation,
//and tracking of multiple consecutive drawings.

public class GameState {
    // Total winnings accumulated across all drawings in the current session
    private double totalWinnings;

    // Current drawing number in the sequence (1-based index)
    private int currentDrawingNumber;

    // Total number of drawings selected for this game session
    private int totalDrawings;

    // Number of spots (numbers) the player chose to play (1, 4, 8, or 10)
    private int playerSpots;

    // Set of numbers selected by the player (size matches playerSpots)
    private Set<Integer> playerNumbers;

    // Set of 20 numbers drawn in the current drawing
    private Set<Integer> currentDrawnNumbers;

    // Winnings from the most recent drawing
    private double currentDrawingWinnings;

    //Constructor initializes the game state to default values.
    public GameState() {
        resetForNewGame();
    }

    //Resets all game state variables to initial values.
     //Used when starting a brand new game session.

    public void resetForNewGame() {
        this.totalWinnings = 0.0;
        this.currentDrawingNumber = 0;
        this.totalDrawings = 0;
        this.playerSpots = 0;
        this.playerNumbers = new HashSet<>();
        this.currentDrawnNumbers = new HashSet<>();
        this.currentDrawingWinnings = 0.0;
    }

    //Initializes a new drawing session with the specified number of drawings.
     //Clears player numbers and drawn numbers but preserves player spots selection.
     //Resets drawing counter and winnings for the new session.

    public void startNewDrawingSession(int totalDrawings) {
        this.totalDrawings = totalDrawings;
        this.currentDrawingNumber = 0;
        this.playerNumbers.clear();
        this.currentDrawnNumbers.clear();
        this.currentDrawingWinnings = 0.0;
    }

    //Executes a single Keno drawing by randomly selecting 20 unique numbers from 1-80.
     //Increments the current drawing counter.

    public Set<Integer> runDrawing() {
        currentDrawnNumbers.clear();
        Random random = new Random();

        // Draw 20 unique random numbers between 1-80
        while (currentDrawnNumbers.size() < 20) {
            int num = random.nextInt(80) + 1;  // Generate number 1-80
            currentDrawnNumbers.add(num);  // Set automatically handles duplicates
        }

        currentDrawingNumber++;  // Increment drawing counter
        return new HashSet<>(currentDrawnNumbers);  // Return copy of drawn numbers
    }

    //Calculates which player numbers match the currently drawn numbers.

    public Set<Integer> getMatches() {
        Set<Integer> matches = new HashSet<>();

        // Check each player number against drawn numbers
        for (int num : playerNumbers) {
            if (currentDrawnNumbers.contains(num)) {
                matches.add(num);
            }
        }
        return matches;
    }

    //Calculates winnings based on the number of matches and player spots.
     //Uses North Carolina Lottery payout table rules.
     //Updates currentDrawingWinnings and adds to totalWinnings.


    public double calculateWinnings(int matches) {
        double winnings = 0.0;

        // Based on North Carolina Lottery payout rules
        switch (playerSpots) {
            case 1:
                // 1-spot game: only pays if you match the 1 number
                if (matches == 1) winnings = 2.0;
                break;

            case 4:
                // 4-spot game: pays for 2, 3, or 4 matches
                if (matches == 2) winnings = 1.0;
                else if (matches == 3) winnings = 5.0;
                else if (matches == 4) winnings = 75.0;
                break;

            case 8:
                // 8-spot game: pays for 4-8 matches
                if (matches == 4) winnings = 2.0;
                else if (matches == 5) winnings = 12.0;
                else if (matches == 6) winnings = 50.0;
                else if (matches == 7) winnings = 750.0;
                else if (matches == 8) winnings = 10000.0;
                break;

            case 10:
                // 10-spot game: pays for 0, 5-10 matches
                // Note: Matching 0 numbers in a 10-spot game is rare and pays out
                if (matches == 0) winnings = 5.0;
                else if (matches == 5) winnings = 2.0;
                else if (matches == 6) winnings = 15.0;
                else if (matches == 7) winnings = 100.0;
                else if (matches == 8) winnings = 500.0;
                else if (matches == 9) winnings = 5000.0;
                else if (matches == 10) winnings = 25000.0;
                break;
        }

        // Update both current drawing winnings and cumulative total
        this.currentDrawingWinnings = winnings;
        this.totalWinnings += winnings;

        return winnings;
    }

    //Checks if there are more drawings remaining in the current session.


    public boolean hasMoreDrawings() {
        return currentDrawingNumber < totalDrawings;
    }

    //getters and setters

    //Total winnings accumulated across all drawings

    public double getTotalWinnings() {
        return totalWinnings;
    }

    //New total winnings value

    public void setTotalWinnings(double totalWinnings) {
        this.totalWinnings = totalWinnings;
    }

    //Current drawing number (1-based)

    public int getCurrentDrawingNumber() {
        return currentDrawingNumber;
    }

    // currentDrawingNumber New current drawing number

    public void setCurrentDrawingNumber(int currentDrawingNumber) {
        this.currentDrawingNumber = currentDrawingNumber;
    }

    //Total number of drawings in this session

    public int getTotalDrawings() {
        return totalDrawings;
    }

    //totalDrawings New total drawings count

    public void setTotalDrawings(int totalDrawings) {
        this.totalDrawings = totalDrawings;
    }

    // Number of spots (numbers) player selected

    public int getPlayerSpots() {
        return playerSpots;
    }

    //playerSpots New player spots value (1, 4, 8, or 10)

    public void setPlayerSpots(int playerSpots) {
        this.playerSpots = playerSpots;
    }

    //Set of numbers selected by the player

    public Set<Integer> getPlayerNumbers() {
        return playerNumbers;
    }

    //playerNumbers New set of player-selected numbers

    public void setPlayerNumbers(Set<Integer> playerNumbers) {
        this.playerNumbers = playerNumbers;
    }

    // Set of numbers drawn in the current drawing

    public Set<Integer> getCurrentDrawnNumbers() {
        return currentDrawnNumbers;
    }

    //currentDrawnNumbers New set of drawn numbers

    public void setCurrentDrawnNumbers(Set<Integer> currentDrawnNumbers) {
        this.currentDrawnNumbers = currentDrawnNumbers;
    }

    //Winnings from the most recent drawing

    public double getCurrentDrawingWinnings() {
        return currentDrawingWinnings;
    }

    //currentDrawingWinnings New current drawing winnings value

    public void setCurrentDrawingWinnings(double currentDrawingWinnings) {
        this.currentDrawingWinnings = currentDrawingWinnings;
    }

    //Adds the specified amount to the total winnings.
     //Alternative to setTotalWinnings for incremental updates.

    public void addToTotalWinnings(double amount) {
        this.totalWinnings += amount;
    }
}