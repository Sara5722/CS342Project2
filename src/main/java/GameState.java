// GameState.java - Data Manager
import java.util.HashSet;
import java.util.Set;

public class GameState {
    private double totalWinnings;
    private int currentDrawingNumber;
    private int totalDrawings;
    private int playerSpots;
    private Set<Integer> playerNumbers;
    private Set<Integer> currentDrawnNumbers;

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

    public void addToTotalWinnings(double amount) {
        this.totalWinnings += amount;
    }
}
