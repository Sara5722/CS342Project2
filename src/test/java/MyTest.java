//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.Test;
//
//import org.junit.jupiter.api.DisplayName;
//
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//
//class MyTest {
//
//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}
//
//}

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

/**
 * JUnit 5 Test Suite for GameState
 * Tests game state management, drawing logic, and winnings calculations
 */
class GameStateTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    // ==================== Initial State Tests ====================

    @Test
    @DisplayName("Test initial state has zero winnings")
    void testInitialStateZeroWinnings() {
        assertEquals(0.0, gameState.getTotalWinnings(),
                "Total winnings should be 0.0 initially");
    }

    @Test
    @DisplayName("Test initial state has zero current drawing number")
    void testInitialStateZeroDrawingNumber() {
        assertEquals(0, gameState.getCurrentDrawingNumber(),
                "Current drawing number should be 0 initially");
    }

    @Test
    @DisplayName("Test initial state has zero total drawings")
    void testInitialStateZeroTotalDrawings() {
        assertEquals(0, gameState.getTotalDrawings(),
                "Total drawings should be 0 initially");
    }

    @Test
    @DisplayName("Test initial state has zero player spots")
    void testInitialStateZeroPlayerSpots() {
        assertEquals(0, gameState.getPlayerSpots(),
                "Player spots should be 0 initially");
    }

    @Test
    @DisplayName("Test initial state has empty player numbers")
    void testInitialStateEmptyPlayerNumbers() {
        Set<Integer> playerNumbers = gameState.getPlayerNumbers();
        assertNotNull(playerNumbers, "Player numbers should not be null");
        assertTrue(playerNumbers.isEmpty(), "Player numbers should be empty initially");
    }

    @Test
    @DisplayName("Test initial state has empty drawn numbers")
    void testInitialStateEmptyDrawnNumbers() {
        Set<Integer> drawnNumbers = gameState.getCurrentDrawnNumbers();
        assertNotNull(drawnNumbers, "Drawn numbers should not be null");
        assertTrue(drawnNumbers.isEmpty(), "Drawn numbers should be empty initially");
    }

    @Test
    @DisplayName("Test initial current drawing winnings is zero")
    void testInitialCurrentDrawingWinnings() {
        assertEquals(0.0, gameState.getCurrentDrawingWinnings(),
                "Current drawing winnings should be 0.0 initially");
    }

    // ==================== Reset Tests ====================

    @Test
    @DisplayName("Test reset for new game clears all state")
    void testResetForNewGame() {
        // Set up some state
        gameState.setTotalWinnings(100.0);
        gameState.setCurrentDrawingNumber(3);
        gameState.setTotalDrawings(4);
        gameState.setPlayerSpots(8);
        Set<Integer> numbers = new HashSet<>();
        numbers.add(1);
        numbers.add(2);
        gameState.setPlayerNumbers(numbers);

        // Reset
        gameState.resetForNewGame();

        // Verify everything is reset
        assertEquals(0.0, gameState.getTotalWinnings(), "Total winnings should be reset to 0");
        assertEquals(0, gameState.getCurrentDrawingNumber(), "Drawing number should be reset to 0");
        assertEquals(0, gameState.getTotalDrawings(), "Total drawings should be reset to 0");
        assertEquals(0, gameState.getPlayerSpots(), "Player spots should be reset to 0");
        assertTrue(gameState.getPlayerNumbers().isEmpty(), "Player numbers should be empty");
        assertTrue(gameState.getCurrentDrawnNumbers().isEmpty(), "Drawn numbers should be empty");
        assertEquals(0.0, gameState.getCurrentDrawingWinnings(), "Current winnings should be reset to 0");
    }

    @Test
    @DisplayName("Test start new drawing session resets session data")
    void testStartNewDrawingSession() {
        // Set up some previous state
        gameState.setCurrentDrawingNumber(2);
        Set<Integer> numbers = new HashSet<>();
        numbers.add(5);
        gameState.setPlayerNumbers(numbers);
        gameState.runDrawing();

        // Start new session
        gameState.startNewDrawingSession(4);

        assertEquals(4, gameState.getTotalDrawings(), "Total drawings should be set to 4");
        assertEquals(0, gameState.getCurrentDrawingNumber(), "Drawing number should be reset to 0");
        assertTrue(gameState.getPlayerNumbers().isEmpty(), "Player numbers should be cleared");
        assertTrue(gameState.getCurrentDrawnNumbers().isEmpty(), "Drawn numbers should be cleared");
        assertEquals(0.0, gameState.getCurrentDrawingWinnings(), "Current winnings should be reset");
    }

    // ==================== Drawing Logic Tests ====================

    @Test
    @DisplayName("Test run drawing produces exactly 20 numbers")
    void testRunDrawingProduces20Numbers() {
        Set<Integer> drawnNumbers = gameState.runDrawing();

        assertEquals(20, drawnNumbers.size(),
                "Drawing should produce exactly 20 numbers");
    }

    @Test
    @DisplayName("Test run drawing produces unique numbers")
    void testRunDrawingProducesUniqueNumbers() {
        Set<Integer> drawnNumbers = gameState.runDrawing();

        assertEquals(20, drawnNumbers.size(),
                "All 20 drawn numbers should be unique (Set size = 20)");
    }

    @Test
    @DisplayName("Test run drawing produces numbers in valid range")
    void testRunDrawingValidRange() {
        Set<Integer> drawnNumbers = gameState.runDrawing();

        for (Integer num : drawnNumbers) {
            assertTrue(num >= 1 && num <= 80,
                    "All drawn numbers should be between 1 and 80, found: " + num);
        }
    }

    @Test
    @DisplayName("Test run drawing increments current drawing number")
    void testRunDrawingIncrementsDrawingNumber() {
        assertEquals(0, gameState.getCurrentDrawingNumber(),
                "Should start at 0");

        gameState.runDrawing();
        assertEquals(1, gameState.getCurrentDrawingNumber(),
                "Should increment to 1 after first drawing");

        gameState.runDrawing();
        assertEquals(2, gameState.getCurrentDrawingNumber(),
                "Should increment to 2 after second drawing");
    }

    @Test
    @DisplayName("Test run drawing updates current drawn numbers")
    void testRunDrawingUpdatesCurrentDrawnNumbers() {
        gameState.runDrawing();

        Set<Integer> currentDrawn = gameState.getCurrentDrawnNumbers();
        assertEquals(20, currentDrawn.size(),
                "Current drawn numbers should be updated with 20 numbers");
    }

    @Test
    @DisplayName("Test run drawing clears previous drawn numbers")
    void testRunDrawingClearsPreviousNumbers() {
        Set<Integer> firstDraw = gameState.runDrawing();
        Set<Integer> secondDraw = gameState.runDrawing();

        assertNotEquals(firstDraw, secondDraw,
                "Second drawing should produce different numbers (high probability)");

        Set<Integer> currentDrawn = gameState.getCurrentDrawnNumbers();
        assertEquals(secondDraw, currentDrawn,
                "Current drawn numbers should match most recent drawing");
    }

    @Test
    @DisplayName("Test multiple drawings produce different results")
    void testMultipleDrawingsDifferent() {
        Set<Integer> draw1 = new HashSet<>(gameState.runDrawing());
        Set<Integer> draw2 = new HashSet<>(gameState.runDrawing());
        Set<Integer> draw3 = new HashSet<>(gameState.runDrawing());

        // While theoretically possible they're the same, probability is astronomically low
        assertFalse(draw1.equals(draw2) && draw2.equals(draw3),
                "Multiple drawings should produce different results (extremely high probability)");
    }

    // ==================== Match Calculation Tests ====================

    @Test
    @DisplayName("Test get matches with no matches")
    void testGetMatchesZero() {
        Set<Integer> playerNumbers = new HashSet<>();
        playerNumbers.add(1);
        playerNumbers.add(2);
        playerNumbers.add(3);
        playerNumbers.add(4);
        gameState.setPlayerNumbers(playerNumbers);

        Set<Integer> drawnNumbers = new HashSet<>();
        for (int i = 50; i < 70; i++) {
            drawnNumbers.add(i);
        }
        gameState.setCurrentDrawnNumbers(drawnNumbers);

        Set<Integer> matches = gameState.getMatches();
        assertEquals(0, matches.size(), "Should have no matches");
    }

    @Test
    @DisplayName("Test get matches with all matches")
    void testGetMatchesAll() {
        Set<Integer> playerNumbers = new HashSet<>();
        playerNumbers.add(1);
        playerNumbers.add(2);
        playerNumbers.add(3);
        playerNumbers.add(4);
        gameState.setPlayerNumbers(playerNumbers);

        Set<Integer> drawnNumbers = new HashSet<>();
        for (int i = 1; i <= 20; i++) {
            drawnNumbers.add(i);
        }
        gameState.setCurrentDrawnNumbers(drawnNumbers);

        Set<Integer> matches = gameState.getMatches();
        assertEquals(4, matches.size(), "Should have 4 matches");
        assertTrue(matches.contains(1) && matches.contains(2) &&
                        matches.contains(3) && matches.contains(4),
                "Should match all player numbers");
    }

    @Test
    @DisplayName("Test get matches with partial matches")
    void testGetMatchesPartial() {
        Set<Integer> playerNumbers = new HashSet<>();
        playerNumbers.add(1);
        playerNumbers.add(2);
        playerNumbers.add(50);
        playerNumbers.add(60);
        gameState.setPlayerNumbers(playerNumbers);

        Set<Integer> drawnNumbers = new HashSet<>();
        for (int i = 1; i <= 20; i++) {
            drawnNumbers.add(i);
        }
        gameState.setCurrentDrawnNumbers(drawnNumbers);

        Set<Integer> matches = gameState.getMatches();
        assertEquals(2, matches.size(), "Should have 2 matches");
        assertTrue(matches.contains(1) && matches.contains(2),
                "Should match numbers 1 and 2");
    }

    @Test
    @DisplayName("Test get matches with empty player numbers")
    void testGetMatchesEmptyPlayerNumbers() {
        Set<Integer> drawnNumbers = new HashSet<>();
        for (int i = 1; i <= 20; i++) {
            drawnNumbers.add(i);
        }
        gameState.setCurrentDrawnNumbers(drawnNumbers);

        Set<Integer> matches = gameState.getMatches();
        assertEquals(0, matches.size(), "Should have no matches with empty player numbers");
    }

    @Test
    @DisplayName("Test get matches with empty drawn numbers")
    void testGetMatchesEmptyDrawnNumbers() {
        Set<Integer> playerNumbers = new HashSet<>();
        playerNumbers.add(1);
        playerNumbers.add(2);
        gameState.setPlayerNumbers(playerNumbers);

        Set<Integer> matches = gameState.getMatches();
        assertEquals(0, matches.size(), "Should have no matches with empty drawn numbers");
    }

    // ==================== Winnings Calculation Tests - Spot 1 ====================

    @Test
    @DisplayName("Spot 1: Test 0 matches pays $0")
    void testSpot1ZeroMatches() {
        gameState.setPlayerSpots(1);
        double winnings = gameState.calculateWinnings(0);
        assertEquals(0.0, winnings, "Spot 1 with 0 matches should pay $0");
        assertEquals(0.0, gameState.getCurrentDrawingWinnings(),
                "Current drawing winnings should be $0");
    }

    @Test
    @DisplayName("Spot 1: Test 1 match pays $2")
    void testSpot1OneMatch() {
        gameState.setPlayerSpots(1);
        double winnings = gameState.calculateWinnings(1);
        assertEquals(2.0, winnings, "Spot 1 with 1 match should pay $2");
        assertEquals(2.0, gameState.getCurrentDrawingWinnings(),
                "Current drawing winnings should be $2");
        assertEquals(2.0, gameState.getTotalWinnings(),
                "Total winnings should be $2");
    }

    // ==================== Winnings Calculation Tests - Spot 4 ====================

    @Test
    @DisplayName("Spot 4: Test 0 matches pays $0")
    void testSpot4ZeroMatches() {
        gameState.setPlayerSpots(4);
        double winnings = gameState.calculateWinnings(0);
        assertEquals(0.0, winnings, "Spot 4 with 0 matches should pay $0");
    }

    @Test
    @DisplayName("Spot 4: Test 1 match pays $0")
    void testSpot4OneMatch() {
        gameState.setPlayerSpots(4);
        double winnings = gameState.calculateWinnings(1);
        assertEquals(0.0, winnings, "Spot 4 with 1 match should pay $0");
    }

    @Test
    @DisplayName("Spot 4: Test 2 matches pays $1")
    void testSpot4TwoMatches() {
        gameState.setPlayerSpots(4);
        double winnings = gameState.calculateWinnings(2);
        assertEquals(1.0, winnings, "Spot 4 with 2 matches should pay $1");
    }

    @Test
    @DisplayName("Spot 4: Test 3 matches pays $5")
    void testSpot4ThreeMatches() {
        gameState.setPlayerSpots(4);
        double winnings = gameState.calculateWinnings(3);
        assertEquals(5.0, winnings, "Spot 4 with 3 matches should pay $5");
    }

    @Test
    @DisplayName("Spot 4: Test 4 matches pays $75")
    void testSpot4FourMatches() {
        gameState.setPlayerSpots(4);
        double winnings = gameState.calculateWinnings(4);
        assertEquals(75.0, winnings, "Spot 4 with 4 matches should pay $75");
    }

    // ==================== Winnings Calculation Tests - Spot 8 ====================

    @Test
    @DisplayName("Spot 8: Test 0 matches pays $0")
    void testSpot8ZeroMatches() {
        gameState.setPlayerSpots(8);
        double winnings = gameState.calculateWinnings(0);
        assertEquals(0.0, winnings, "Spot 8 with 0 matches should pay $0");
    }

    @Test
    @DisplayName("Spot 8: Test 4 matches pays $2")
    void testSpot8FourMatches() {
        gameState.setPlayerSpots(8);
        double winnings = gameState.calculateWinnings(4);
        assertEquals(2.0, winnings, "Spot 8 with 4 matches should pay $2");
    }

    @Test
    @DisplayName("Spot 8: Test 5 matches pays $12")
    void testSpot8FiveMatches() {
        gameState.setPlayerSpots(8);
        double winnings = gameState.calculateWinnings(5);
        assertEquals(12.0, winnings, "Spot 8 with 5 matches should pay $12");
    }

    @Test
    @DisplayName("Spot 8: Test 6 matches pays $50")
    void testSpot8SixMatches() {
        gameState.setPlayerSpots(8);
        double winnings = gameState.calculateWinnings(6);
        assertEquals(50.0, winnings, "Spot 8 with 6 matches should pay $50");
    }

    @Test
    @DisplayName("Spot 8: Test 7 matches pays $750")
    void testSpot8SevenMatches() {
        gameState.setPlayerSpots(8);
        double winnings = gameState.calculateWinnings(7);
        assertEquals(750.0, winnings, "Spot 8 with 7 matches should pay $750");
    }

    @Test
    @DisplayName("Spot 8: Test 8 matches pays $10,000")
    void testSpot8EightMatches() {
        gameState.setPlayerSpots(8);
        double winnings = gameState.calculateWinnings(8);
        assertEquals(10000.0, winnings, "Spot 8 with 8 matches should pay $10,000");
    }

    // ==================== Winnings Calculation Tests - Spot 10 ====================

    @Test
    @DisplayName("Spot 10: Test 0 matches pays $5")
    void testSpot10ZeroMatches() {
        gameState.setPlayerSpots(10);
        double winnings = gameState.calculateWinnings(0);
        assertEquals(5.0, winnings, "Spot 10 with 0 matches should pay $5");
    }

    @Test
    @DisplayName("Spot 10: Test 5 matches pays $2")
    void testSpot10FiveMatches() {
        gameState.setPlayerSpots(10);
        double winnings = gameState.calculateWinnings(5);
        assertEquals(2.0, winnings, "Spot 10 with 5 matches should pay $2");
    }

    @Test
    @DisplayName("Spot 10: Test 6 matches pays $15")
    void testSpot10SixMatches() {
        gameState.setPlayerSpots(10);
        double winnings = gameState.calculateWinnings(6);
        assertEquals(15.0, winnings, "Spot 10 with 6 matches should pay $15");
    }

    @Test
    @DisplayName("Spot 10: Test 7 matches pays $100")
    void testSpot10SevenMatches() {
        gameState.setPlayerSpots(10);
        double winnings = gameState.calculateWinnings(7);
        assertEquals(100.0, winnings, "Spot 10 with 7 matches should pay $100");
    }

    @Test
    @DisplayName("Spot 10: Test 8 matches pays $500")
    void testSpot10EightMatches() {
        gameState.setPlayerSpots(10);
        double winnings = gameState.calculateWinnings(8);
        assertEquals(500.0, winnings, "Spot 10 with 8 matches should pay $500");
    }

    @Test
    @DisplayName("Spot 10: Test 9 matches pays $5,000")
    void testSpot10NineMatches() {
        gameState.setPlayerSpots(10);
        double winnings = gameState.calculateWinnings(9);
        assertEquals(5000.0, winnings, "Spot 10 with 9 matches should pay $5,000");
    }

    @Test
    @DisplayName("Spot 10: Test 10 matches pays $25,000")
    void testSpot10TenMatches() {
        gameState.setPlayerSpots(10);
        double winnings = gameState.calculateWinnings(10);
        assertEquals(25000.0, winnings, "Spot 10 with 10 matches should pay $25,000");
    }

    // ==================== Total Winnings Accumulation Tests ====================

    @Test
    @DisplayName("Test total winnings accumulates over multiple calculations")
    void testTotalWinningsAccumulation() {
        gameState.setPlayerSpots(4);

        gameState.calculateWinnings(2); // $1
        assertEquals(1.0, gameState.getTotalWinnings(),
                "Total winnings should be $1 after first calculation");

        gameState.calculateWinnings(3); // $5
        assertEquals(6.0, gameState.getTotalWinnings(),
                "Total winnings should be $6 after second calculation");

        gameState.calculateWinnings(4); // $75
        assertEquals(81.0, gameState.getTotalWinnings(),
                "Total winnings should be $81 after third calculation");
    }

    @Test
    @DisplayName("Test add to total winnings method")
    void testAddToTotalWinnings() {
        gameState.addToTotalWinnings(50.0);
        assertEquals(50.0, gameState.getTotalWinnings(),
                "Total winnings should be $50");

        gameState.addToTotalWinnings(25.0);
        assertEquals(75.0, gameState.getTotalWinnings(),
                "Total winnings should be $75");

        gameState.addToTotalWinnings(100.0);
        assertEquals(175.0, gameState.getTotalWinnings(),
                "Total winnings should be $175");
    }

    @Test
    @DisplayName("Test current drawing winnings is updated")
    void testCurrentDrawingWinningsUpdated() {
        gameState.setPlayerSpots(8);

        gameState.calculateWinnings(5);
        assertEquals(12.0, gameState.getCurrentDrawingWinnings(),
                "Current drawing winnings should be $12");

        gameState.calculateWinnings(7);
        assertEquals(750.0, gameState.getCurrentDrawingWinnings(),
                "Current drawing winnings should be updated to $750");
    }

    // ==================== Has More Drawings Tests ====================

    @Test
    @DisplayName("Test has more drawings returns true when drawings remain")
    void testHasMoreDrawingsTrue() {
        gameState.setTotalDrawings(4);
        gameState.setCurrentDrawingNumber(2);

        assertTrue(gameState.hasMoreDrawings(),
                "Should have more drawings when current (2) < total (4)");
    }

    @Test
    @DisplayName("Test has more drawings returns false when all complete")
    void testHasMoreDrawingsFalse() {
        gameState.setTotalDrawings(4);
        gameState.setCurrentDrawingNumber(4);

        assertFalse(gameState.hasMoreDrawings(),
                "Should have no more drawings when current (4) >= total (4)");
    }

    @Test
    @DisplayName("Test has more drawings with zero drawings")
    void testHasMoreDrawingsZero() {
        gameState.setTotalDrawings(0);
        gameState.setCurrentDrawingNumber(0);

        assertFalse(gameState.hasMoreDrawings(),
                "Should have no more drawings when total is 0");
    }

    @Test
    @DisplayName("Test has more drawings after first drawing")
    void testHasMoreDrawingsAfterFirst() {
        gameState.startNewDrawingSession(3);
        gameState.runDrawing(); // First drawing

        assertTrue(gameState.hasMoreDrawings(),
                "Should have more drawings after first of 3");
        assertEquals(1, gameState.getCurrentDrawingNumber(),
                "Should be on drawing 1");
    }

    // ==================== Setter and Getter Tests ====================

    @Test
    @DisplayName("Test set and get total winnings")
    void testSetGetTotalWinnings() {
        gameState.setTotalWinnings(123.45);
        assertEquals(123.45, gameState.getTotalWinnings(),
                "Should get the total winnings that was set");
    }

    @Test
    @DisplayName("Test set and get current drawing number")
    void testSetGetCurrentDrawingNumber() {
        gameState.setCurrentDrawingNumber(3);
        assertEquals(3, gameState.getCurrentDrawingNumber(),
                "Should get the drawing number that was set");
    }

    @Test
    @DisplayName("Test set and get total drawings")
    void testSetGetTotalDrawings() {
        gameState.setTotalDrawings(4);
        assertEquals(4, gameState.getTotalDrawings(),
                "Should get the total drawings that was set");
    }

    @Test
    @DisplayName("Test set and get player spots")
    void testSetGetPlayerSpots() {
        gameState.setPlayerSpots(8);
        assertEquals(8, gameState.getPlayerSpots(),
                "Should get the player spots that was set");
    }

    @Test
    @DisplayName("Test set and get player numbers")
    void testSetGetPlayerNumbers() {
        Set<Integer> numbers = new HashSet<>();
        numbers.add(5);
        numbers.add(10);
        numbers.add(15);

        gameState.setPlayerNumbers(numbers);

        Set<Integer> retrieved = gameState.getPlayerNumbers();
        assertEquals(3, retrieved.size(), "Should have 3 player numbers");
        assertTrue(retrieved.contains(5) && retrieved.contains(10) && retrieved.contains(15),
                "Should contain the correct numbers");
    }

    @Test
    @DisplayName("Test set and get current drawn numbers")
    void testSetGetCurrentDrawnNumbers() {
        Set<Integer> numbers = new HashSet<>();
        for (int i = 1; i <= 20; i++) {
            numbers.add(i);
        }

        gameState.setCurrentDrawnNumbers(numbers);

        Set<Integer> retrieved = gameState.getCurrentDrawnNumbers();
        assertEquals(20, retrieved.size(), "Should have 20 drawn numbers");
    }

    @Test
    @DisplayName("Test set and get current drawing winnings")
    void testSetGetCurrentDrawingWinnings() {
        gameState.setCurrentDrawingWinnings(50.0);
        assertEquals(50.0, gameState.getCurrentDrawingWinnings(),
                "Should get the current drawing winnings that was set");
    }

    // ==================== Integration Tests ====================

    @Test
    @DisplayName("Test complete game flow simulation")
    void testCompleteGameFlow() {
        // Start a new game session
        gameState.startNewDrawingSession(2);
        assertEquals(2, gameState.getTotalDrawings(), "Should have 2 total drawings");

        // Set up player choices
        gameState.setPlayerSpots(4);
        Set<Integer> playerNumbers = new HashSet<>();
        playerNumbers.add(1);
        playerNumbers.add(2);
        playerNumbers.add(3);
        playerNumbers.add(4);
        gameState.setPlayerNumbers(playerNumbers);

        // Run first drawing
        assertTrue(gameState.hasMoreDrawings(), "Should have drawings available");
        Set<Integer> firstDraw = gameState.runDrawing();
        assertEquals(20, firstDraw.size(), "First drawing should have 20 numbers");
        assertEquals(1, gameState.getCurrentDrawingNumber(), "Should be on drawing 1");

        // Calculate winnings for first drawing
        Set<Integer> matches = gameState.getMatches();
        gameState.calculateWinnings(matches.size());
        double firstWinnings = gameState.getTotalWinnings();

        // Run second drawing
        assertTrue(gameState.hasMoreDrawings(), "Should have one more drawing");
        Set<Integer> secondDraw = gameState.runDrawing();
        assertEquals(20, secondDraw.size(), "Second drawing should have 20 numbers");
        assertEquals(2, gameState.getCurrentDrawingNumber(), "Should be on drawing 2");

        // Calculate winnings for second drawing
        matches = gameState.getMatches();
        gameState.calculateWinnings(matches.size());

        // Verify total winnings accumulated
        assertTrue(gameState.getTotalWinnings() >= firstWinnings,
                "Total winnings should accumulate");

        // No more drawings
        assertFalse(gameState.hasMoreDrawings(), "Should have no more drawings");
    }

    @Test
    @DisplayName("Test winnings with zero matches for non-paying scenarios")
    void testZeroMatchesNonPayingScenarios() {
        // Spot 1 with 0 matches - no pay
        gameState.setPlayerSpots(1);
        assertEquals(0.0, gameState.calculateWinnings(0));

        // Spot 4 with 0 matches - no pay
        gameState.setPlayerSpots(4);
        assertEquals(0.0, gameState.calculateWinnings(0));

        // Spot 8 with 0 matches - no pay
        gameState.setPlayerSpots(8);
        assertEquals(0.0, gameState.calculateWinnings(0));

        // Note: Spot 10 with 0 matches DOES pay $5 (tested elsewhere)
    }

    @Test
    @DisplayName("Test invalid spot count returns zero winnings")
    void testInvalidSpotCountReturnsZero() {
        gameState.setPlayerSpots(5); // Invalid spot count
        double winnings = gameState.calculateWinnings(3);
        assertEquals(0.0, winnings, "Invalid spot count should return $0 winnings");
    }
}