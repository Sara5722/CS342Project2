// GameState.java - Updated with Drawing Logic
import java.util.*;

public class GameState {
    private double totalWinnings;
    private int currentDrawingNumber;
    private int totalDrawings;
    private int playerSpots;
    private Set<Integer> playerNumbers;
    private Set<Integer> currentDrawnNumbers;
    private double currentDrawingWinnings;

    public GameState() {
        resetForNewGame();
    }

    public void resetForNewGame() {
        this.totalWinnings = 0.0;
        this.currentDrawingNumber = 0;
        this.totalDrawings = 0;
        this.playerSpots = 0;
        this.playerNumbers = new HashSet<>();
        this.currentDrawnNumbers = new HashSet<>();
        this.currentDrawingWinnings = 0.0;
    }

    public void startNewDrawingSession(int totalDrawings) {
        this.totalDrawings = totalDrawings;
        this.currentDrawingNumber = 0;
        this.playerNumbers.clear();
        this.currentDrawnNumbers.clear();
        this.currentDrawingWinnings = 0.0;
    }

    public Set<Integer> runDrawing() {
        currentDrawnNumbers.clear();
        Random random = new Random();

        // Draw 20 unique random numbers between 1-80
        while (currentDrawnNumbers.size() < 20) {
            int num = random.nextInt(80) + 1;
            currentDrawnNumbers.add(num);
        }

        currentDrawingNumber++;
        return new HashSet<>(currentDrawnNumbers);
    }

    public Set<Integer> getMatches() {
        Set<Integer> matches = new HashSet<>();
        for (int num : playerNumbers) {
            if (currentDrawnNumbers.contains(num)) {
                matches.add(num);
            }
        }
        return matches;
    }

    public double calculateWinnings(int matches) {
        double winnings = 0.0;

        // Based on North Carolina Lottery payout rules
        switch (playerSpots) {
            case 1:
                if (matches == 1) winnings = 2.0;
                break;
            case 4:
                if (matches == 2) winnings = 1.0;
                else if (matches == 3) winnings = 5.0;
                else if (matches == 4) winnings = 75.0;
                break;
            case 8:
                if (matches == 4) winnings = 2.0;
                else if (matches == 5) winnings = 12.0;
                else if (matches == 6) winnings = 50.0;
                else if (matches == 7) winnings = 750.0;
                else if (matches == 8) winnings = 10000.0;
                break;
            case 10:
                if (matches == 0) winnings = 5.0;
                else if (matches == 5) winnings = 2.0;
                else if (matches == 6) winnings = 15.0;
                else if (matches == 7) winnings = 100.0;
                else if (matches == 8) winnings = 500.0;
                else if (matches == 9) winnings = 5000.0;
                else if (matches == 10) winnings = 25000.0;
                break;
        }

        this.currentDrawingWinnings = winnings;
        this.totalWinnings += winnings;

        return winnings;
    }

    public boolean hasMoreDrawings() {
        return currentDrawingNumber < totalDrawings;
    }

    // Getters and setters
    public double getTotalWinnings() { return totalWinnings; }
    public void setTotalWinnings(double totalWinnings) { this.totalWinnings = totalWinnings; }

    public int getCurrentDrawingNumber() { return currentDrawingNumber; }
    public void setCurrentDrawingNumber(int currentDrawingNumber) { this.currentDrawingNumber = currentDrawingNumber; }

    public int getTotalDrawings() { return totalDrawings; }
    public void setTotalDrawings(int totalDrawings) { this.totalDrawings = totalDrawings; }

    public int getPlayerSpots() { return playerSpots; }
    public void setPlayerSpots(int playerSpots) { this.playerSpots = playerSpots; }

    public Set<Integer> getPlayerNumbers() { return playerNumbers; }
    public void setPlayerNumbers(Set<Integer> playerNumbers) { this.playerNumbers = playerNumbers; }

    public Set<Integer> getCurrentDrawnNumbers() { return currentDrawnNumbers; }
    public void setCurrentDrawnNumbers(Set<Integer> currentDrawnNumbers) { this.currentDrawnNumbers = currentDrawnNumbers; }

    public double getCurrentDrawingWinnings() { return currentDrawingWinnings; }
    public void setCurrentDrawingWinnings(double currentDrawingWinnings) { this.currentDrawingWinnings = currentDrawingWinnings; }

    public void addToTotalWinnings(double amount) {
        this.totalWinnings += amount;
    }
}