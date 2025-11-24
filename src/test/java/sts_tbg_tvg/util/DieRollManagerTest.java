package sts_tbg_tvg.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Unit tests for DieRollManager.
 */
public class DieRollManagerTest {

    @Before
    public void setUp() {
        // Reset state before each test
        DieRollManager.reset();
        DieRollManager.clearListeners();
    }

    @After
    public void tearDown() {
        // Clean up after each test
        DieRollManager.reset();
        DieRollManager.clearListeners();
    }

    @Test
    public void testRollDieInternal_returnsValueBetween1And6() {
        // Test with multiple random seeds to ensure we get all possible values
        Random rng = new Random(12345);
        boolean[] valuesRolled = new boolean[7]; // indices 1-6 will be used

        // Roll 100 times to get good coverage
        for (int i = 0; i < 100; i++) {
            int roll = DieRollManager.rollDieInternal(rng);
            assertTrue("Die roll should be >= 1", roll >= 1);
            assertTrue("Die roll should be <= 6", roll <= 6);
            valuesRolled[roll] = true;
        }

        // Verify all values 1-6 were rolled at least once (probabilistic test)
        // With 100 rolls, it's extremely likely we'll see all values
        for (int i = 1; i <= 6; i++) {
            assertTrue("Value " + i + " should have been rolled at least once", valuesRolled[i]);
        }
    }

    @Test
    public void testRollDie_updatesCurrentRoll() {
        assertEquals("Initial roll should be 0", 0, DieRollManager.getCurrentRoll());

        int roll = DieRollManager.rollDie();

        assertTrue("Roll should be between 1 and 6", roll >= 1 && roll <= 6);
        assertEquals("getCurrentRoll should match the rolled value", roll, DieRollManager.getCurrentRoll());
    }

    @Test
    public void testRollDie_multipleRolls() {
        int firstRoll = DieRollManager.rollDie();
        int secondRoll = DieRollManager.rollDie();

        // getCurrentRoll should return the most recent roll
        assertEquals("getCurrentRoll should return most recent roll", secondRoll, DieRollManager.getCurrentRoll());
    }

    @Test
    public void testReset_clearsCurrentRoll() {
        DieRollManager.rollDie();
        assertTrue("Roll should be non-zero after rolling", DieRollManager.getCurrentRoll() > 0);

        DieRollManager.reset();

        assertEquals("Roll should be 0 after reset", 0, DieRollManager.getCurrentRoll());
    }

    @Test
    public void testAddListener_notifiesOnRoll() {
        TestListener listener = new TestListener();
        DieRollManager.addListener(listener);

        int roll = DieRollManager.rollDie();

        assertEquals("Listener should be notified once", 1, listener.rollsReceived.size());
        assertEquals("Listener should receive correct roll value", roll, (int) listener.rollsReceived.get(0));
    }

    @Test
    public void testAddListener_multipleListeners() {
        TestListener listener1 = new TestListener();
        TestListener listener2 = new TestListener();
        DieRollManager.addListener(listener1);
        DieRollManager.addListener(listener2);

        int roll = DieRollManager.rollDie();

        assertEquals("First listener should be notified", 1, listener1.rollsReceived.size());
        assertEquals("Second listener should be notified", 1, listener2.rollsReceived.size());
        assertEquals("Both listeners should receive same roll", listener1.rollsReceived.get(0), listener2.rollsReceived.get(0));
    }

    @Test
    public void testAddListener_duplicateListenerNotAddedTwice() {
        TestListener listener = new TestListener();
        DieRollManager.addListener(listener);
        DieRollManager.addListener(listener); // Add same listener again

        DieRollManager.rollDie();

        assertEquals("Listener should only be notified once even if added twice", 1, listener.rollsReceived.size());
    }

    @Test
    public void testAddListener_nullListenerIgnored() {
        // Should not throw an exception
        DieRollManager.addListener(null);

        // Should still work normally
        int roll = DieRollManager.rollDie();
        assertTrue("Roll should still work after adding null listener", roll >= 1 && roll <= 6);
    }

    @Test
    public void testRemoveListener_stopsNotifications() {
        TestListener listener = new TestListener();
        DieRollManager.addListener(listener);

        DieRollManager.rollDie();
        assertEquals("Listener should be notified before removal", 1, listener.rollsReceived.size());

        DieRollManager.removeListener(listener);
        DieRollManager.rollDie();

        assertEquals("Listener should not be notified after removal", 1, listener.rollsReceived.size());
    }

    @Test
    public void testRemoveListener_nonExistentListenerDoesNothing() {
        TestListener listener = new TestListener();

        // Should not throw an exception
        DieRollManager.removeListener(listener);
    }

    @Test
    public void testClearListeners_removesAllListeners() {
        TestListener listener1 = new TestListener();
        TestListener listener2 = new TestListener();
        DieRollManager.addListener(listener1);
        DieRollManager.addListener(listener2);

        DieRollManager.clearListeners();
        DieRollManager.rollDie();

        assertEquals("First listener should not be notified after clear", 0, listener1.rollsReceived.size());
        assertEquals("Second listener should not be notified after clear", 0, listener2.rollsReceived.size());
    }

    @Test
    public void testListener_multipleRolls() {
        TestListener listener = new TestListener();
        DieRollManager.addListener(listener);

        int roll1 = DieRollManager.rollDie();
        int roll2 = DieRollManager.rollDie();
        int roll3 = DieRollManager.rollDie();

        assertEquals("Listener should receive all 3 rolls", 3, listener.rollsReceived.size());
        assertEquals("First roll should match", roll1, (int) listener.rollsReceived.get(0));
        assertEquals("Second roll should match", roll2, (int) listener.rollsReceived.get(1));
        assertEquals("Third roll should match", roll3, (int) listener.rollsReceived.get(2));
    }

    @Test
    public void testListener_exceptionDoesNotBreakRolling() {
        // Add a listener that throws an exception
        DieRollManager.addListener(rollResult -> {
            throw new RuntimeException("Test exception");
        });

        // Add a normal listener that should still be notified
        TestListener normalListener = new TestListener();
        DieRollManager.addListener(normalListener);

        // Should not throw exception and should still notify the normal listener
        int roll = DieRollManager.rollDie();

        assertEquals("Normal listener should still be notified despite other listener's exception",
                1, normalListener.rollsReceived.size());
        assertEquals("Normal listener should receive correct value", roll, (int) normalListener.rollsReceived.get(0));
    }

    @Test
    public void testReset_doesNotClearListeners() {
        TestListener listener = new TestListener();
        DieRollManager.addListener(listener);

        DieRollManager.rollDie();
        DieRollManager.reset();
        DieRollManager.rollDie();

        assertEquals("Listener should still receive rolls after reset", 2, listener.rollsReceived.size());
    }

    /**
     * Test listener implementation that records all die rolls it receives.
     */
    private static class TestListener implements DieRollManager.DieRollListener {
        public final List<Integer> rollsReceived = new ArrayList<>();

        @Override
        public void onDieRolled(int rollResult) {
            rollsReceived.add(rollResult);
        }
    }
}
