package de.rayzs.tacticalmonsters.api;

public class TacticalMonsters {

    private static TacticalMonstersAPI instance;

    /**
     * Gets the instance of the TacticalMonstersAPI.
     *
     * @return The current TacticalMonstersAPI instance.
     * @throws IllegalStateException If TacticalMonsters isn't initialized yet.
     */
    public static TacticalMonstersAPI get() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("TacticalMonsters isn't initialized yet!");
        }

        return instance;
    }

    /**
     * Initializes the TacticalMonstersAPI instance.
     * Only works if it's not initialized yet.
     *
     * @param api The TacticalMonstersAPI instance to set.
     */
    public static void setInstance(final TacticalMonstersAPI api) {
        if (instance != null) {
            throw new IllegalStateException("TacticalMonsters is already initialized!");
        }

        instance = api;
    }
}
