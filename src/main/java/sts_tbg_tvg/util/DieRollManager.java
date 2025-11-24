package sts_tbg_tvg.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages the per-turn die roll for board game mechanics.
 * At the start of each round, a 6-sided die is rolled and the result
 * is made available to all game systems.
 */
public class DieRollManager {
    private static int currentRoll = 0;
    private static final Random random = new Random();
    private static final List<DieRollListener> listeners = new ArrayList<>();

    /**
     * Interface for objects that want to be notified when a new die is rolled.
     */
    public interface DieRollListener {
        /**
         * Called when a new die roll occurs.
         * @param rollResult The result of the die roll (1-6)
         */
        void onDieRolled(int rollResult);
    }

    /**
     * Rolls a 6-sided die and returns the result.
     * Also notifies all registered listeners of the new roll.
     *
     * @return The result of the die roll (1-6)
     */
    public static int rollDie() {
        currentRoll = rollDieInternal(random);
        notifyListeners(currentRoll);
        return currentRoll;
    }

    /**
     * Internal die roll logic that can be tested with a custom Random instance.
     * Package-private for testing purposes.
     *
     * @param rng The random number generator to use
     * @return A random number between 1 and 6 (inclusive)
     */
    static int rollDieInternal(Random rng) {
        return rng.nextInt(6) + 1;
    }

    /**
     * Gets the current die roll result.
     * Returns 0 if no die has been rolled yet this combat.
     *
     * @return The current die roll (1-6), or 0 if none
     */
    public static int getCurrentRoll() {
        return currentRoll;
    }

    /**
     * Resets the die roll state. Should be called when combat ends or a new run starts.
     */
    public static void reset() {
        currentRoll = 0;
        // Note: We don't clear listeners on reset, as they're registered for the entire session
    }

    /**
     * Registers a listener to be notified when a die is rolled.
     *
     * @param listener The listener to register
     */
    public static void addListener(DieRollListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a previously registered listener.
     *
     * @param listener The listener to remove
     */
    public static void removeListener(DieRollListener listener) {
        listeners.remove(listener);
    }

    /**
     * Clears all registered listeners.
     * Should typically only be used during testing or mod cleanup.
     */
    public static void clearListeners() {
        listeners.clear();
    }

    /**
     * Notifies all registered listeners of a new die roll.
     *
     * @param rollResult The result of the die roll
     */
    private static void notifyListeners(int rollResult) {
        // Create a copy to avoid concurrent modification if a listener modifies the list
        List<DieRollListener> listenersCopy = new ArrayList<>(listeners);
        for (DieRollListener listener : listenersCopy) {
            try {
                listener.onDieRolled(rollResult);
            } catch (Exception e) {
                // Log but don't crash if a listener throws an exception
                System.err.println("Error notifying die roll listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
